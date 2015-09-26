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