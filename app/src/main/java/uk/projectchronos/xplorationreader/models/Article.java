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

package uk.projectchronos.xplorationreader.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import uk.projectchronos.xplorationreader.R;

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
     * URL to the array of keywords connected to this Article object.
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
     * String for date-time of publishing ISO 8601 format, else null.
     */
    @Expose
    private java.util.Date published;

    /**
     * Represent the type of the article.
     */
    @Expose
    @SerializedName("type_of")
    private Type type;

    /**
     * Boolean stating if the object is present in the Graph API.
     */
    @Expose
    @SerializedName("in_graph")
    private boolean inGraph;

    /**
     * The id of the indexed resource.
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
     * @return the keywords list to set.
     */
    public List<Keyword> setKeywordsList(List<Keyword> keywordsList) {
        return this.keywordsList = keywordsList;
    }

    /**
     * Gets type of the article.
     *
     * @return the type of the article.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets type of the article.
     *
     * @param type the type to set.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Gets true if the article is in graph API.
     *
     * @return true if it is in graph API, false otherwise.
     */
    public boolean isInGraph() {
        return inGraph;
    }

    /**
     * Sets if the article is in graph API.
     *
     * @param inGraph true if is in graph API, false otherwise.
     */
    public void setInGraph(boolean inGraph) {
        this.inGraph = inGraph;
    }

    /**
     * Gets the uuid of the article.
     *
     * @return the uuid of the article.
     */
    public long getUuid() {
        return uuid;
    }

    /**
     * Sets the uuid of the article.
     *
     * @param uuid the uuid to set.
     */
    public void setUuid(long uuid) {
        this.uuid = uuid;
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
                ", type=" + type +
                ", inGraph=" + inGraph +
                ", uuid=" + uuid +
                ", keywordsList=" + keywordsList +
                '}';
    }

    /**
     * Type of article.
     */
    public enum Type implements Parcelable {
        /**
         * Article.
         */
        @SerializedName("feed")
        FEED,

        /**
         * Tweet.
         */
        @SerializedName("tweet")
        TWEET,

        /**
         * Image.
         */
        @SerializedName("media")
        MEDIA,

        /**
         * Link.
         */
        @SerializedName("link")
        LINK,

        /**
         * PDF.
         */
        @SerializedName("pdf")
        PDF,

        /**
         * Paper.
         */
        @SerializedName("paper")
        PAPER,

        /**
         * Facebook post.
         */
        @SerializedName("fb")
        FB_POST,

        /**
         * YouTube documentary.
         */
        @SerializedName("movie")
        MOVIE;

        public static final Creator<Type> CREATOR = new Creator<Type>() {
            @Override
            public Type createFromParcel(Parcel in) {
                Type type = Type.values()[in.readInt()];
                type.setId(in.readInt());
                type.setStringResourceId(in.readInt());
                type.setValue(in.readString());
                return type;
            }

            @Override
            public Type[] newArray(int size) {
                return new Type[size];
            }
        };

        // Static initialization
        static {
            FEED.id = 0;
            FEED.stringResourceId = R.string.feeds_filter;
            FEED.value = "feed";

            TWEET.id = 1;
            TWEET.stringResourceId = R.string.tweets_filter;
            TWEET.value = "tweet";

            MEDIA.id = 2;
            MEDIA.stringResourceId = R.string.medias_filter;
            MEDIA.value = "media";

            LINK.id = 3;
            LINK.stringResourceId = R.string.links_filter;
            LINK.value = "link";

            PDF.id = 4;
            PDF.stringResourceId = R.string.pdf_filter;
            PDF.value = "pdf";

            PAPER.id = 5;
            PAPER.stringResourceId = R.string.papers_filter;
            PAPER.value = "paper";

            FB_POST.id = 6;
            FB_POST.stringResourceId = R.string.fb_posts_filter;
            FB_POST.value = "fb";

            MOVIE.id = 7;
            MOVIE.stringResourceId = R.string.movies_filter;
            MOVIE.value = "movie";
        }

        /**
         * The id of the type.
         */
        private int id;
        /**
         * The string resource of the type.
         */
        private int stringResourceId;
        /**
         * The effective value of the type
         */
        private String value;

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
            dest.writeInt(id);
            dest.writeInt(stringResourceId);
            dest.writeString(value);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        /**
         * Gets the id of the type.
         *
         * @return the id of the type.
         */
        public int getId() {
            return id;
        }

        /**
         * Sets id of the type.
         *
         * @param id the value to set.
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * Gets the stringResourceId of the type.
         *
         * @return the stringResourceId of the type.
         */
        public int getStringResourceId() {
            return this.stringResourceId;
        }

        /**
         * Sets stringResourcesId of the type.
         *
         * @param stringResourceId the value to set.
         */
        private void setStringResourceId(int stringResourceId) {
            this.stringResourceId = stringResourceId;
        }

        /**
         * Gets the value of the type.
         *
         * @return the value of the type.
         */
        public String getValue() {
            return this.value;
        }

        /**
         * Sets value of the type.
         *
         * @param value the value to set.
         */
        private void setValue(String value) {
            this.value = value;
        }
    }
}