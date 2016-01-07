package me.vickychijwani.material.spec.entity;

public class IndexSubsection {

    public final String title;
    public final String href;

    // path to local JSON file containing the contents of this subsection / chapter
    public final String filepath;

    public IndexSubsection(String title, String href, String filepath) {
        this.title = title;
        this.href = href;
        this.filepath = filepath;
    }

}
