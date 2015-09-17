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

public class ResponseArticlesList {

    @Expose
    private List<Article> articles;
    @Expose
    private String next;

    /**
     * Gets the articles list.
     *
     * @return the articles list.
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Sets the articles list.
     *
     * @param articles the articles list to set.
     */
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Gets the url for next page of articles.
     *
     * @return the url of the next page.
     */
    public String getNext() {
        return next;
    }

    /**
     * Sets the url of the next page of articles.
     *
     * @param next the ulr of the next page to set.
     */
    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "ResponseArticlesList{" +
                "articles=" + articles +
                ", next='" + next + '\'' +
                '}';
    }
}
