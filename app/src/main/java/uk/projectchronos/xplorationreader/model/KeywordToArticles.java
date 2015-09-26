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

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * Entity mapped to table "KEYWORD_TO_ARTICLES".
 */
public class KeywordToArticles {

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private long keywordId;

    /**
     *
     */
    private long articleId;

    public KeywordToArticles() {
    }

    public KeywordToArticles(Long id) {
        this.id = id;
    }

    public KeywordToArticles(Long id, long keywordId, long articleId) {
        this.id = id;
        this.keywordId = keywordId;
        this.articleId = articleId;
    }

    /**
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return
     */
    public long getKeywordId() {
        return keywordId;
    }

    /**
     * @param keywordId
     */
    public void setKeywordId(long keywordId) {
        this.keywordId = keywordId;
    }

    /**
     * @return
     */
    public long getArticleId() {
        return articleId;
    }

    /**
     * @param articleId
     */
    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "KeywordToArticles{" +
                "id=" + id +
                ", keywordId=" + keywordId +
                ", articleId=" + articleId +
                '}';
    }
}