package me.vickychijwani.material.spec.entity;

public class ChapterIntro extends ChapterPart {

    public static final String TYPE = "intro";

    public final String html;

    public ChapterIntro(String html) {
        super(TYPE);
        this.html = html;
    }

}
