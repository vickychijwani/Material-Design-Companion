package me.vickychijwani.material.spec.entity;

public class ArticleTitle implements PlainTextEntity {

    public final String text;

    public ArticleTitle(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
