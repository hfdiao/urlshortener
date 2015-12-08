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
package github.hfdiao.urlshortener.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import github.hfdiao.urlshortener.config.DAOConfig;
import github.hfdiao.urlshortener.config.PropertyPlaceholderConfig;
import github.hfdiao.urlshortener.config.ServiceConfig;
import github.hfdiao.urlshortener.config.TransactionConfig;
import github.hfdiao.urlshortener.config.WebMVCConfig;

/**
 * @author dhf
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { PropertyPlaceholderConfig.class,
		DAOConfig.class, TransactionConfig.class, ServiceConfig.class, WebMVCConfig.class })
@WebAppConfiguration
@Transactional
public class URLShortenerWebControllerTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMVC;

	private static String LONG_URL = "http://www.baidu.com";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.mockMVC = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	/**
	 * Test method for
	 * {@link github.hfdiao.urlshortener.web.URLShortenerWebController#generateShortenedPath(java.lang.String)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGenerateShortenedPath() throws Exception {
		String url = LONG_URL;
		this.mockMVC.perform(MockMvcRequestBuilders.post("/api/shorten").param("url", url))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test method for
	 * {@link github.hfdiao.urlshortener.web.URLShortenerWebController#redirect(java.lang.String)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRedirect() throws Exception {
		String url = LONG_URL;

		MvcResult shortenResult = this.mockMVC.perform(MockMvcRequestBuilders.post("/api/shorten").param("url", url))
				.andReturn();
		MockHttpServletResponse shortenResp = shortenResult.getResponse();
		String shortenPath = shortenResp.getContentAsString();

		this.mockMVC.perform(MockMvcRequestBuilders.get("/" + shortenPath)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl(url));
	}

}
