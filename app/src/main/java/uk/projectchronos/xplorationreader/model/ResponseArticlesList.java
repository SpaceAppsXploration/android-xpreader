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
