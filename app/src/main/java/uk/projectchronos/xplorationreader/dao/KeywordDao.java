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

package uk.projectchronos.xplorationreader.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.QueryBuilder;
import uk.projectchronos.xplorationreader.model.Keyword;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "KEYWORD".
 */
public class KeywordDao extends AbstractDao<Keyword, Long> {

    public static final String TABLENAME = "KEYWORD";
    private DaoSession daoSession;

    public KeywordDao(DaoConfig config) {
        super(config);
    }


    public KeywordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"KEYWORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"RELATED_URLS\" TEXT," + // 1: relatedUrls
                "\"SLUG\" TEXT," + // 2: slug
                "\"VALUE\" TEXT UNIQUE );"); // 3: value
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"KEYWORD\"";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, Keyword entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String relatedUrls = entity.getRelatedUrls();
        if (relatedUrls != null) {
            stmt.bindString(2, relatedUrls);
        }

        String slug = entity.getSlug();
        if (slug != null) {
            stmt.bindString(3, slug);
        }

        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(4, value);
        }
    }

    @Override
    protected void attachEntity(Keyword entity) {
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
    public Keyword readEntity(Cursor cursor, int offset) {
        return new Keyword( //
                cursor.isNull(offset) ? null : cursor.getLong(offset), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // relatedUrls
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // slug
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // value
        );
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, Keyword entity, int offset) {
        entity.setId(cursor.isNull(offset) ? null : cursor.getLong(offset));
        entity.setRelatedUrls(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSlug(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setValue(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(Keyword entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(Keyword entity) {
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
     * It checks using the value property (that is declared into the DB unique).
     *
     * @param entity the entity to insert/retrieve to/from DB.
     * @return the id into DB of the entity.
     */
    public long getOrInsert(Keyword entity) {
        long id;
        QueryBuilder queryBuilder = this.queryBuilder().where(Properties.Value.eq(entity.getValue()));

        if (queryBuilder.count() == 1) {
            id = ((Keyword) queryBuilder.list().get(0)).getId();
        } else {
            id = insert(entity);
        }

        return id;
    }

    /**
     * Properties of entity Keyword.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property RelatedUrls = new Property(1, String.class, "relatedUrls", false, "RELATED_URLS");
        public final static Property Slug = new Property(2, String.class, "slug", false, "SLUG");
        public final static Property Value = new Property(3, String.class, "value", false, "VALUE");
    }
}