/*
 * Copyright 2014-2015 Project Chronos and Pramantha Ltd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package uk.projectchronos.xplorationreader.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Response object to getKeywords().
 */
public class ResponseKeywordsList {

    /**
     * The list of keywords.
     */
    @Expose
    private List<Keyword> keywords;

    /**
     * Te URL of the the indexed resource.
     */
    @Expose
    private String url;

    /**
     * The id of the indexed resource.
     */
    @Expose
    private long uuid;

    /**
     * Gets the keywords.
     *
     * @return the keywords.
     */
    public List<Keyword> getKeywords() {
        return keywords;
    }

    /**
     * Sets the keywords.
     */
    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    /**
     * Gets URL of the indexed resource.
     *
     * @return the URL of the indexed resource.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the indexed resource.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the UUID of the resource.
     *
     * @return the UUID of the resource.
     */
    public long getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of the resoruce.
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
