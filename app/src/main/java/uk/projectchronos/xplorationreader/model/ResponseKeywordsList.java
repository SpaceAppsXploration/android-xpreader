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
