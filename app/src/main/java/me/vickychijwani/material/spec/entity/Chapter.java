package me.vickychijwani.material.spec.entity;

public class Chapter {

    public final String title;
    public final String href;
    public final ChapterPart[] content;

    public Chapter(String title, String href, ChapterPart[] content) {
        this.title = title;
        this.href = href;
        this.content = content;
    }

}
