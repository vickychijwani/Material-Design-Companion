package me.vickychijwani.material.spec.entity;

public class Article {

    public final ArticleTitle title;
    public final Module[] modules;

    public Article(String title, Module[] modules) {
        this.title = new ArticleTitle(title);
        this.modules = modules;
    }

}
