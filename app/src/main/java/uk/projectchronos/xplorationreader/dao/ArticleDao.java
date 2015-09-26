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

package uk.projectchronos.xplorationreader.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.QueryBuilder;
import uk.projectchronos.xplorationreader.model.Article;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "ARTICLE".
 */
public class ArticleDao extends AbstractDao<Article, Long> {

    public static final String TABLENAME = "ARTICLE";
    private DaoSession daoSession;

    public ArticleDao(DaoConfig config) {
        super(config);
    }


    public ArticleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"ARTICLE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"URL\" TEXT UNIQUE ," + // 2: url
                "\"_ABSTRACT\" TEXT," + // 3: _abstract
                "\"KEYWORDS_URL\" TEXT," + // 4: keywordsUrl
                "\"STORED\" INTEGER," + // 5: stored
                "\"PUBLISHED\" INTEGER);"); // 6: published
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ARTICLE\"";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, Article entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }

        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }

        String _abstract = entity.get_abstract();
        if (_abstract != null) {
            stmt.bindString(4, _abstract);
        }

        String keywordsUrl = entity.getKeywordsUrl();
        if (keywordsUrl != null) {
            stmt.bindString(5, keywordsUrl);
        }

        java.util.Date stored = entity.getStored();
        if (stored != null) {
            stmt.bindLong(6, stored.getTime());
        }

        java.util.Date published = entity.getPublished();
        if (published != null) {
            stmt.bindLong(7, published.getTime());
        }
    }

    @Override
    protected void attachEntity(Article entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset) ? null : cursor.getLong(offset);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Article readEntity(Cursor cursor, int offset) {
        return new Article( //
                cursor.isNull(offset) ? null : cursor.getLong(offset), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // url
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // _abstract
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // keywordsUrl
                cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // stored
                cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)) // published
        );
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, Article entity, int offset) {
        entity.setId(cursor.isNull(offset) ? null : cursor.getLong(offset));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.set_abstract(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setKeywordsUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setStored(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setPublished(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(Article entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(Article entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    /**
     * Gets or inserts the entity provided.
     * It checks using the url property (that is declared into the DB unique).
     *
     * @param entity the entity to insert/retrieve to/from DB.
     * @return the id into DB of the entity.
     */
    public long getOrInsert(Article entity) {
        long id;
        QueryBuilder queryBuilder = this.queryBuilder().where(Properties.Url.eq(entity.getUrl()));

        if (queryBuilder.count() == 1) {
            id = ((Article) queryBuilder.list().get(0)).getId();
        } else {
            id = insert(entity);
        }

        return id;
    }

    /**
     * Properties of entity Article.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property _abstract = new Property(3, String.class, "_abstract", false, "_ABSTRACT");
        public final static Property KeywordsUrl = new Property(4, String.class, "keywordsUrl", false, "KEYWORDS_URL");
        public final static Property Stored = new Property(5, java.util.Date.class, "stored", false, "STORED");
        public final static Property Published = new Property(6, java.util.Date.class, "published", false, "PUBLISHED");
    }
}
