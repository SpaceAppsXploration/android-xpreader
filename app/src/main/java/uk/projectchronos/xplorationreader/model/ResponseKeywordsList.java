/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Claudio Pastorini
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.projectchronos.xplorationreader.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 *
 */
public class ResponseKeywordsList {

    /**
     *
     */
    @Expose
    private List<Keyword> keywords;

    /**
     *
     */
    @Expose
    private String url;

    /**
     *
     */
    @Expose
    private long uuid;

    /**
     * @return
     */
    public List<Keyword> getKeywords() {
        return keywords;
    }

    /**
     * @return
     */
    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    /**
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return
     */
    public long getUuid() {
        return uuid;
    }

    /**
     * @return
     */
    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "ResponseKeywordsList{" +
                "keywords=" + keywords +
                ", url='" + url + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
