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

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;
import uk.projectchronos.xplorationreader.dao.ArticleDao;
import uk.projectchronos.xplorationreader.dao.DaoSession;
import uk.projectchronos.xplorationreader.dao.KeywordDao;
import uk.projectchronos.xplorationreader.dao.KeywordToArticlesDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "KEYWORD".
 */
public class Keyword {

    /**
     *
     */
    private Long id;

    /**
     *
     */
    @Expose
    private String relatedUrls;

    /**
     *
     */
    @Expose
    private String slug;

    /**
     *
     */
    @Expose
    private String value;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient KeywordDao myDao;

    /**
     *
     */
    private List<KeywordToArticles> keywordToArticlesList;

    /**
     *
     */
    private List<Article> articlesList;

    public Keyword() {
    }

    public Keyword(Long id) {
        this.id = id;
    }

    public Keyword(Long id, String relatedUrls, String slug, String value) {
        this.id = id;
        this.relatedUrls = relatedUrls;
        this.slug = slug;
        this.value = value;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getKeywordDao() : null;
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
    public String getRelatedUrls() {
        return relatedUrls;
    }

    /**
     * @param relatedUrls
     */
    public void setRelatedUrls(String relatedUrls) {
        this.relatedUrls = relatedUrls;
    }

    /**
     * @return
     */
    public String getSlug() {
        return slug;
    }

    /**
     * @param slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity.
     */
    public List<KeywordToArticles> getKeywordToArticlesList() {
        if (keywordToArticlesList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            KeywordToArticlesDao targetDao = daoSession.getKeywordToArticlesDao();
            List<KeywordToArticles> keywordToArticlesListNew = targetDao._queryKeyword_KeywordToArticlesList(id);
            synchronized (this) {
                if (keywordToArticlesList == null) {
                    keywordToArticlesList = keywordToArticlesListNew;
                }
            }
        }
        return keywordToArticlesList;
    }

    /**
     * @return
     */
    public List<Article> getArticlesList() {
        if (articlesList == null) {
            articlesList = new ArrayList<>();
            for (KeywordToArticles keywordToArticles : getKeywordToArticlesList()) {
                long articleId = keywordToArticles.getArticleId();

                articlesList.addAll(daoSession.getArticleDao().queryBuilder().where(ArticleDao.Properties.Id.eq(articleId)).list());
            }
        }
        return articlesList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    public synchronized void resetKeywordToArticlesList() {
        keywordToArticlesList = null;
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "id=" + id +
                ", relatedUrls='" + relatedUrls + '\'' +
                ", slug='" + slug + '\'' +
                ", value='" + value + '\'' +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                ", keywordToArticlesList=" + keywordToArticlesList +
                ", articlesList=" + articlesList +
                '}';
    }
}