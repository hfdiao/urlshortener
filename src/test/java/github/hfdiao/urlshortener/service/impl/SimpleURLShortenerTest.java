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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import github.hfdiao.urlshortener.config.DAOConfig;
import github.hfdiao.urlshortener.config.PropertyPlaceholderConfig;
import github.hfdiao.urlshortener.config.ServiceConfig;
import github.hfdiao.urlshortener.config.TransactionConfig;
import github.hfdiao.urlshortener.service.URLShortener;

/**
 * @author dhf
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { PropertyPlaceholderConfig.class,
		DAOConfig.class, TransactionConfig.class, ServiceConfig.class })
@Transactional
public class SimpleURLShortenerTest {

	@Autowired
	private URLShortener shortener;

	private static String URL = "http://www.baidu.com";
	private static String SHORTEN_PATH = "baidu";

	/**
	 * Test method for
	 * {@link github.hfdiao.urlshortener.service.impl.SimpleURLShortener#generateShortenedPath(java.lang.String)}
	 * .
	 */
	@Test
	public void testGenerateShortenedPath() {
		String shortenPath = shortener.generateShortenedPath(URL);
		assertNotNull(shortenPath);
		assertTrue(shortenPath.matches("^[a-zA-Z0-9]+$"));
	}

	/**
	 * Test method for
	 * {@link github.hfdiao.urlshortener.service.impl.SimpleURLShortener#createMapping(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateMapping() {
		String url = URL;
		String shortenedPath = SHORTEN_PATH;
		boolean success = shortener.createMapping(url, shortenedPath);
		assertTrue(success);
	}

	/**
	 * Test method for
	 * {@link github.hfdiao.urlshortener.service.impl.SimpleURLShortener#reinstate(java.lang.String)}
	 * .
	 */
	@Test
	public void testReinstate() {
		String url = URL;
		String shortenedPath = SHORTEN_PATH;
		boolean success = shortener.createMapping(url, shortenedPath);
		assertTrue(success);

		String urlReinstated = shortener.reinstate(shortenedPath);
		assertEquals(url, urlReinstated);
	}

}
