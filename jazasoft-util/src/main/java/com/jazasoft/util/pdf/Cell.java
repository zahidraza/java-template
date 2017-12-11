package com.jazasoft.util.pdf;

import com.itextpdf.text.Phrase;
import lombok.Data;

/**
 * Created by mdzahidraza on 02/12/17.
 */
@Data
public class Cell {
    private String content;
    private Phrase phrase;
    private int rowspan = 1;
    private int colspan = 1;
    private float width = 1.0f;

    public Cell() {
    }

    public Cell(String content) {
        this.content = content;
    }

    public Cell(String content, int rowspan) {
        this.content = content;
        this.rowspan = rowspan;
    }

    public Cell(String content, float width) {
        this.content = content;
        this.width = width;
    }

    public Cell(String content, int rowspan, int colspan) {
        this.content = content;
        this.rowspan = rowspan;
        this.colspan = colspan;
    }

    public Cell(Phrase content) {
        this.phrase = content;
    }

    public Cell(Phrase content, int rowspan) {
        this.phrase = content;
        this.rowspan = rowspan;
    }

    public Cell(Phrase content, float width) {
        this.phrase = content;
        this.width = width;
    }

    public Cell(Phrase content, int rowspan, int colspan) {
        this.phrase = content;
        this.rowspan = rowspan;
        this.colspan = colspan;
    }

}
