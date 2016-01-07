package me.vickychijwani.material.spec.entity;

public class FigureGroup extends ModulePart {

    public static final String TYPE = "figure-group";

    public final Figure[] figures;

    public FigureGroup(Figure[] figures) {
        super(TYPE);
        this.figures = figures;
    }

}
