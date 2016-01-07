package me.vickychijwani.material.spec.entity;

public class IndexSection {

    public final String title;
    public final IndexSubsection[] sections;

    public IndexSection(String title, IndexSubsection[] sections) {
        this.title = title;
        this.sections = sections;
    }

}
