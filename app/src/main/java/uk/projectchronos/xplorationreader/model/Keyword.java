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

/**
 * Entity Keyword.
 */
public class Keyword {

    /**
     * TODO
     */
    @Expose
    private int count;

    /**
     * TODO only for now for indexer
     */
    @Expose
    private String keyboard;

    /**
     * RelatedUrls ot the keyword.
     */
    @Expose
    private String relatedUrls;

    /**
     * Slug of the keyword.
     */
    @Expose
    private String slug;

    /**
     * Value of the keyword.
     */
    @Expose
    private String value;

    /**
     * Gets the related URL of the keyword.
     *
     * @return the related URL of the keyword.
     */
    public String getRelatedUrls() {
        return relatedUrls;
    }

    /**
     * Sets the related URL of the keyword.
     *
     * @param relatedUrls the related URL of the keyword.
     */
    public void setRelatedUrls(String relatedUrls) {
        this.relatedUrls = relatedUrls;
    }

    /**
     * Gets the slug of the keyword.
     *
     * @return the slug of the keyword.
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Sets the slug of the keyword.
     *
     * @param slug the slug to set.
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * Gets the value of the keyword.
     *
     * @return the value of the keyword.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the keyword.
     *
     * @param value the value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "relatedUrls='" + relatedUrls + '\'' +
                ", slug='" + slug + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}