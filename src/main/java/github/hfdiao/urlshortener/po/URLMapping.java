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
package github.hfdiao.urlshortener.po;

import java.io.Serializable;

/**
 * @author dhf
 *
 */
public class URLMapping implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4285737342974246444L;

	private long id;
	private String shortenedPath;
	private String reversedURL;
	private long urlHash;
	private long created;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getShortenedPath() {
		return this.shortenedPath;
	}

	public void setShortenedPath(String shortenedPath) {
		this.shortenedPath = shortenedPath;
	}

	public String getReversedURL() {
		return this.reversedURL;
	}

	public void setReversedURL(String reversedURL) {
		this.reversedURL = reversedURL;
	}

	public long getUrlHash() {
		return this.urlHash;
	}

	public void setUrlHash(long urlHash) {
		this.urlHash = urlHash;
	}

	public long getCreated() {
		return this.created;
	}

	public void setCreated(long created) {
		this.created = created;
	}
}
