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
package github.hfdiao.urlshortener.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import github.hfdiao.urlshortener.config.DAOConfig;
import github.hfdiao.urlshortener.config.PropertyPlaceholderConfig;
import github.hfdiao.urlshortener.config.TransactionConfig;
import github.hfdiao.urlshortener.po.URLMapping;
import github.hfdiao.urlshortener.util.Strings;

/**
 * @author dhf
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { PropertyPlaceholderConfig.class,
		DAOConfig.class, TransactionConfig.class })
@Transactional
public class URLMappingDAOTest {

	@Autowired
	private URLMappingDAO urlmappingDAO;

	private static URLMapping TESTING_URLMAPPING;
	private static String URL = "http://www.baidu.com";
	private static String SHORTEN_PATH = "baidu";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String url = URL;
		String shortenedPath = SHORTEN_PATH;
		long urlHash = url.hashCode();

		URLMapping mapping = new URLMapping();
		mapping.setShortenedPath(shortenedPath);
		mapping.setUrlHash(urlHash);
		mapping.setReversedURL(Strings.reverse(url));
		mapping.setCreated(System.currentTimeMillis());

		TESTING_URLMAPPING = mapping;
	}

	/**
	 * Test method for
	 * {@link github.hfdiao.urlshortener.dao.URLMappingDAO#add(github.hfdiao.urlshortener.po.URLMapping)}
	 * .
	 */
	@Test
	public void testAdd() {
		boolean success = urlmappingDAO.add(TESTING_URLMAPPING);
		assertTrue(success);
	}

	/**
	 * Test method for
	 * {@link github.hfdiao.urlshortener.dao.URLMappingDAO#getByShortenedPath(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetByShortenedPath() {
		String url = URL;
		String shortenedPath = SHORTEN_PATH;

		URLMapping mapping = urlmappingDAO.getByShortenedPath(shortenedPath);
		assertNull(mapping);

		urlmappingDAO.add(TESTING_URLMAPPING);

		mapping = urlmappingDAO.getByShortenedPath(SHORTEN_PATH);
		assertNotNull(mapping);
		String reversedURL = mapping.getReversedURL();
		assertEquals(url, Strings.reverse(reversedURL));
	}

	/**
	 * Test method for
	 * {@link github.hfdiao.urlshortener.dao.URLMappingDAO#getByURLHash(long)}.
	 */
	@Test
	public void testGetByURLHash() {
		String url = URL;
		String shortenedPath = SHORTEN_PATH;
		long urlHash = url.hashCode();

		List<URLMapping> mappings = urlmappingDAO.getByURLHash(urlHash);
		assertTrue(mappings == null || mappings.isEmpty());

		urlmappingDAO.add(TESTING_URLMAPPING);

		mappings = urlmappingDAO.getByURLHash(urlHash);
		assertNotNull(mappings);
		assertTrue(mappings.size() == 1);

		URLMapping mapping = mappings.get(0);
		assertNotNull(mapping);
		String reversedURL = mapping.getReversedURL();
		assertEquals(url, Strings.reverse(reversedURL));
		assertEquals(shortenedPath, mapping.getShortenedPath());
	}

}
