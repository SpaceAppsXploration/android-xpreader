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

package uk.projectchronos.xplorationreader;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import uk.projectchronos.xplorationreader.dao.ArticleDao;
import uk.projectchronos.xplorationreader.dao.DaoMaster;
import uk.projectchronos.xplorationreader.dao.DaoSession;
import uk.projectchronos.xplorationreader.dao.KeywordDao;
import uk.projectchronos.xplorationreader.dao.KeywordToArticlesDao;

/**
 * This class mantains all signletons and public state of the app.
 */
public class App extends Application {

    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "articles-db";

    /**
     * Open helper of the database.
     */
    private DaoMaster.DevOpenHelper devOpenHelper;  // TODO use OpenHelper and improve it with onUpgrade

    /**
     * SQLiteDatabase.
     */
    private SQLiteDatabase database;

    /**
     * DaoMaster.
     */
    private DaoMaster daoMaster;

    /**
     * DaoSession.
     */
    private DaoSession daoSession;

    /**
     * Dao for Article.
     */
    private ArticleDao articleDao;

    /**
     * Dao for Keyword.
     */
    private KeywordDao keywordDao;

    /**
     * Dao for KeywordToArticles.
     */
    private KeywordToArticlesDao keywordToArticlesDao;

    /**
     * Singleton for ArticleDao.
     *
     * @return the ArticleDao instance.
     */
    public ArticleDao getArticleDao() {
        if (articleDao == null) {
            articleDao = getDaoSession().getArticleDao();
        }

        return articleDao;
    }

    /**
     * Singleton for KeywordDao.
     *
     * @return the KeywordDao instance.
     */
    public KeywordDao getKeywordDao() {
        if (keywordDao == null) {
            keywordDao = getDaoSession().getKeywordDao();
        }

        return keywordDao;
    }

    /**
     * Singleton for KeywordToArticlesDao.
     *
     * @return the KeywordToArticlesDao instance.
     */
    public KeywordToArticlesDao getKeywordToArticlesDao() {
        if (keywordToArticlesDao == null) {
            keywordToArticlesDao = getDaoSession().getKeywordToArticlesDao();
        }

        return keywordToArticlesDao;
    }

    /**
     * Singleton for DaoSession.
     *
     * @return the DaoSession instance.
     */
    private DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getDaoMaster().newSession();
        }
        return daoSession;
    }

    /**
     * Singleton for DaoMaster.
     *
     * @return the DaoMaster instance.
     */
    private DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            daoMaster = new DaoMaster(getDatabase());
        }
        return daoMaster;
    }

    /**
     * Singleton for writable database.
     *
     * @return the database instance.
     */
    private SQLiteDatabase getDatabase() {
        if (database == null) {
            database = getOpenHelper().getWritableDatabase();
        }
        return database;
    }

    /**
     * Singleton for OpenHelper.
     *
     * @return the OpenHelper instance.
     */
    private DaoMaster.DevOpenHelper getOpenHelper() {
        if (devOpenHelper == null) {
            devOpenHelper = new DaoMaster.DevOpenHelper(this, DATABASE_NAME, null);
        }
        return devOpenHelper;
    }
}