package uk.projectchronos.xplorationreader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class that represent article.
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
    @Expose
    private String keywords;

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
     * Gets the keywords url of the article.
     *
     * @return the keywords url.
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Sets the keywords url of the article.
     *
     * @param keywords the keywords url to set.
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", _abstract='" + _abstract + '\'' +
                ", stored='" + stored + '\'' +
                ", published=" + published +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}