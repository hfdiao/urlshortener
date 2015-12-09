/*******************************************************************************
 * Copyright (c) 2015, dhf
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
/**
 * 
 */
package github.hfdiao.urlshortener.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import github.hfdiao.urlshortener.config.CacheConfig;
import github.hfdiao.urlshortener.dao.URLMappingDAO;
import github.hfdiao.urlshortener.exception.ResourceNotFoundException;
import github.hfdiao.urlshortener.exception.URLShortenException;
import github.hfdiao.urlshortener.po.URLMapping;
import github.hfdiao.urlshortener.service.URLShortener;
import github.hfdiao.urlshortener.util.Asserts;
import github.hfdiao.urlshortener.util.Exceptions;
import github.hfdiao.urlshortener.util.Strings;

/**
 * @author dhf
 *
 */
@Service
public class SimpleURLShortener implements URLShortener {

	@Autowired
	private URLMappingDAO urlMappingDAO;

	private int maxTryCnt = 10;

	private static final char[] ALPHANUMERICS;

	static {
		StringBuilder alphanumerics = new StringBuilder(62);
		for (char i = 'a'; i <= 'z'; ++i) {
			alphanumerics.append(i);
		}
		for (char i = 'A'; i <= 'Z'; ++i) {
			alphanumerics.append(i);
		}
		for (char i = '0'; i <= '9'; ++i) {
			alphanumerics.append(i);
		}

		ALPHANUMERICS = alphanumerics.toString().toCharArray();
	}

	@Override
	public String generateShortenedPath(String url) throws URLShortenException {
		Asserts.check(Strings.isValidURL(url), "no a valid url: " + url);

		// generate mapping every time, even if exists url
		URLMapping mapping = generateURLMapping(url);
		return mapping.getShortenedPath();
	}

	@Override
	public boolean createMapping(String url, String shortenedPath) throws URLShortenException {
		Asserts.check(Strings.isValidURL(url), "no a valid url: " + url);
		Asserts.check(Strings.isAlphanumerics(shortenedPath), "no a valid shortenedPath: " + shortenedPath);

		URLMapping mapping = new URLMapping();
		mapping.setShortenedPath(shortenedPath);
		mapping.setReversedURL(Strings.reverse(url));
		mapping.setUrlHash(hash(url));
		mapping.setCreated(System.currentTimeMillis());

		try {
			if (urlMappingDAO.add(mapping)) {
				return true;
			}
		} catch (Exception e) {
			if (!Exceptions.isDupliacatedKeyException(e)) {
				throw e;
			}
		}
		return false;
	}

	@Override
	@Cacheable(CacheConfig.CACHE_NAME)
	public String reinstate(String shortenedPath) throws ResourceNotFoundException {
		Asserts.check(Strings.isAlphanumerics(shortenedPath), "no a valid shortenedPath: " + shortenedPath);

		URLMapping mapping = urlMappingDAO.getByShortenedPath(shortenedPath);
		if (mapping == null || mapping.getReversedURL() == null) {
			throw new ResourceNotFoundException("shortenedPath not found: " + shortenedPath);
		}
		String reversedURL = mapping.getReversedURL();
		String url = Strings.reverse(reversedURL);
		return url;
	}

	private URLMapping generateURLMapping(String url) {
		long urlHash = hash(url);

		URLMapping mapping = new URLMapping();
		mapping.setReversedURL(Strings.reverse(url));
		mapping.setUrlHash(urlHash);
		mapping.setCreated(System.currentTimeMillis());

		long dividend = urlHash;
		long divisor = ALPHANUMERICS.length;
		int tryCnt = 0;
		while (tryCnt < this.maxTryCnt) {
			StringBuilder sb = new StringBuilder(8);
			while (dividend != 0) {
				int remainder = (int) (dividend % divisor);
				dividend = dividend / divisor;
				sb.append(ALPHANUMERICS[remainder]);
			}
			String shortenedPath = sb.reverse().toString();
			mapping.setShortenedPath(shortenedPath);

			try {
				if (urlMappingDAO.add(mapping)) {
					return mapping;
				}
			} catch (Exception e) {
				if (!Exceptions.isDupliacatedKeyException(e)) {
					throw e;
				}
			}

			++tryCnt;
			dividend = urlHash + System.currentTimeMillis();
		}

		throw new URLShortenException("can't generate in limited time");
	}

	private static long hash(String str) {
		long hash = str.hashCode();
		if (hash <= 0) {
			hash = -hash + Integer.MAX_VALUE + 1;
		}
		return hash;
	}

	public void setUrlMappingDAO(URLMappingDAO urlMappingDAO) {
		this.urlMappingDAO = urlMappingDAO;
	}

	public void setMaxTryCnt(int maxTryCnt) {
		this.maxTryCnt = maxTryCnt;
	}
}
