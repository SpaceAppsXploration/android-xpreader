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
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Entity Article.
 */
public class Article {

    /**
     * Title of the article.
     */
    @Expose
    private String title;

    /**
     * URL to the article.
     */
    @Expose
    private String url;
    /**
     * Abstract (if present, else "" void string).
     */
    @Expose
    @SerializedName("abstract")
    private String _abstract;

    /**
     * URL to the array of keywords connected to this Article object, [] if no keyword.
     */
    @Expose
    @SerializedName("keywords_url")
    private String keywordsUrl;

    /**
     * String for date-time of storage, ISO 8601 format.
     */
    @Expose
    private java.util.Date stored;

    /**
     * Sring for date-time of publishing ISO 8601 format, else null.
     */
    @Expose
    private java.util.Date published;

    /**+
     *
     */
    @Expose
    @SerializedName("type_of")
    private String type;

    /**
     *
     */
    @Expose
    @SerializedName("in_graph")
    private boolean inGraph;

    /**
     *
     */
    @Expose
    private long uuid;

    /**
     * List of Keywords.
     */
    private List<Keyword> keywordsList;

    /**
     * Gets the title of the article.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the article.
     *
     * @param title the title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the URL of the article.
     *
     * @return the URL of the article.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the article.
     *
     * @param url the URL to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the abastract of the article.
     *
     * @return the abastract of the article.
     */
    public String get_abstract() {
        return _abstract;
    }

    /**
     * Sets the abstract of the article.
     *
     * @param _abstract the abstract to set.
     */
    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    /**
     * Gets the keywords URL of the article.
     *
     * @return the keywords URL of the article.
     */
    public String getKeywordsUrl() {
        return keywordsUrl;
    }

    /**
     * Sets the keywords URL of the article.
     *
     * @param keywordsUrl the URL of the keywords to set.
     */
    public void setKeywordsUrl(String keywordsUrl) {
        this.keywordsUrl = keywordsUrl;
    }

    /**
     * Gets the stored date of the article.
     *
     * @return the stored date of the article.
     */
    public java.util.Date getStored() {
        return stored;
    }

    /**
     * Sets the stored date of the article.
     *
     * @param stored the stored date to set.
     */
    public void setStored(java.util.Date stored) {
        this.stored = stored;
    }

    /**
     * Gets the publised date of the article.
     *
     * @return the publised date of the article.
     */
    public java.util.Date getPublished() {
        return published;
    }

    /**
     * Sets the published date of the article.
     *
     * @param published the published date to set.
     */
    public void setPublished(java.util.Date published) {
        this.published = published;
    }

    /**
     * Gets keywords of the article.
     *
     * @return the keywords list of the article.
     */
    public List<Keyword> getKeywordsList() {
        return keywordsList;
    }

    /**
     * Sets keywords of the article.
     *
     * @return the keywords list of the article.
     */
    public List<Keyword> setKeywordsList(List<Keyword> keywordsList) {
        return this.keywordsList = keywordsList;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", _abstract='" + _abstract + '\'' +
                ", keywordsUrl='" + keywordsUrl + '\'' +
                ", stored=" + stored +
                ", published=" + published +
                ", type='" + type + '\'' +
                ", inGraph=" + inGraph +
                ", uuid=" + uuid +
                ", keywordsList=" + keywordsList +
                '}';
    }

    /**
     *
     */
    private enum Type {
        feed,
        tweets,
        fb
    }
}