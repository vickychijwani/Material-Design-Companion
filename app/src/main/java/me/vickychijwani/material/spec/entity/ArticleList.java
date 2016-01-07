package me.vickychijwani.material.spec.entity;

public class ArticleList extends ChapterPart {

    public static final String TYPE = "article-list";

    public final Article[] articles;

    public ArticleList(Article[] articles) {
        super(TYPE);
        this.articles = articles;
    }

}
