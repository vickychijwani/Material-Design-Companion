package me.vickychijwani.material.spec.entity;

public class Figure extends ChapterPart {

    public static final String TYPE = "figure";

    public final String mediaType;  // "image" or "video"
    public final String src;
    public final String caption;

    public Figure(String mediaType, String src, String caption) {
        super(TYPE);
        this.mediaType = mediaType;
        this.src = src;
        this.caption = caption;
    }

}
