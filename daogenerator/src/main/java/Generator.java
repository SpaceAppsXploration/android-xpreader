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

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Run this class in order to generate code for DAO.
 * <p/>
 * This code does not generate all the stuff that we need, so, use with caution!
 */
public class Generator {

    /**
     * Database version.
     */
    private static final int VERSION = 1;

    /**
     * Default java package location.
     */
    private static final String DEFAULT_JAVA_PACKAGES = "uk.projectchronos.xplorationreader.model";

    public static void main(String[] args) throws Exception {
        // Creates new schema
        Schema schema = new Schema(VERSION, DEFAULT_JAVA_PACKAGES);

        // Creates Keyword entity with autoincrement id key and unique keyword
        Entity keyword = schema.addEntity("Keyword");
        keyword.addIdProperty().autoincrement();
        keyword.addStringProperty("relatedUrls");
        keyword.addStringProperty("slug");
        keyword.addStringProperty("value").unique();

        // Creates KeywordToArticles entity that binds Keyowrds and Articles
        Entity keywordToArticles = schema.addEntity("KeywordToArticles");
        keywordToArticles.addIdProperty().autoincrement().getProperty();

        // Creates Article entity with autoincrement id key, title, unique url, abastract, keywordsUrl, stored and published date
        Entity article = schema.addEntity("Article");
        article.addIdProperty().autoincrement();
        article.addStringProperty("title");
        article.addStringProperty("url").unique();
        article.addStringProperty("_abstract");
        article.addStringProperty("keywordsUrl");
        article.addDateProperty("stored");
        article.addDateProperty("published");

        // Creates property keywordId to insert into keywordToArticles with toMany referring to Keyword
        Property keywordId = keywordToArticles.addLongProperty("keywordId").notNull().getProperty();
        keyword.addToMany(keywordToArticles, keywordId);

        // Creates property articleId to insert into keywordToArticles with toMany referring to Article
        Property articleId = keywordToArticles.addLongProperty("articleId").notNull().getProperty();
        article.addToMany(keywordToArticles, articleId);

        // Creates unique on keywordId and articleId couple in KeywordToArticle entity
        Index indexUnique = new Index();
        indexUnique.addProperty(keywordId);
        indexUnique.addProperty(articleId);
        indexUnique.makeUnique();
        keywordToArticles.addIndex(indexUnique);

        // Generates all!
        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }
}