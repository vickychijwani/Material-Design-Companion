package me.vickychijwani.material.spec.entity;

public class Image implements Figure {

    public final String src;
    public final String caption;

    public Image(String src, String caption) {
        this.src = src;
        this.caption = caption;
    }

}
