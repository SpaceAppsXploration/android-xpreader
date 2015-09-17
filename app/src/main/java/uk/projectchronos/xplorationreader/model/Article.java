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
 * Class that represent an article.
 */
public class Article {

    @Expose
    private String title;
    @Expose
    private String url;
    @SerializedName("abstract")
    @Expose
    private String _abstract;
    @Expose
    private String stored;
    @Expose
    private Object published;
    @SerializedName("keywords")
    @Expose
    private String keywordsUrl;
    private List<Keyword> keywordList;

    /**
     * Gets the title of the article.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the url of the article.
     *
     * @return the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url of the article.
     *
     * @param url the url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the abstract of the article.
     *
     * @return the abstract.
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * Sets the abstract of the article.
     *
     * @param _abstract the abstract to set.
     */
    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    /**
     * Gets the stored date (in string) of the the article.
     *
     * @return the stored date.
     */
    public String getStored() {
        return stored;
    }

    /**
     * Sets the stored date of the article.
     *
     * @param stored the stored date to set.
     */
    public void setStored(String stored) {
        this.stored = stored;
    }

    /**
     * Gets the published date (in string) of the the article.
     *
     * @return the published date.
     */
    public Object getPublished() {
        return published;
    }

    /**
     * Sets the published date of the article.
     *
     * @param published the published date to set.
     */
    public void setPublished(Object published) {
        this.published = published;
    }

    /**
     * Gets the keywordList url of the article.
     *
     * @return the keywordList url.
     */
    public String getKeywordsUrl() {
        return keywordsUrl;
    }

    /**
     * Sets the keywordList url of the article.
     *
     * @param keywordsUrl the keywordList url to set.
     */
    public void setKeywordsUrl(String keywordsUrl) {
        this.keywordsUrl = keywordsUrl;
    }

    /**
     * Gets the keywordList list of the article.
     *
     * @return the keywordList list.
     */
    public List<Keyword> getKeywordList() {
        return keywordList;
    }

    /**
     * Sets the keywordList list of the article.
     *
     * @param keywordList the keywordList to add.
     */
    public void setKeywordList(List<Keyword> keywordList) {
        this.keywordList = keywordList;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", _abstract='" + _abstract + '\'' +
                ", stored='" + stored + '\'' +
                ", published=" + published +
                ", keywordsUrl='" + keywordsUrl + '\'' +
                ", keywordList=" + keywordList +
                '}';
    }
}