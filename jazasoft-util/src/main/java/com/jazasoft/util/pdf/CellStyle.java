package com.jazasoft.util.pdf;

import lombok.Data;

/**
 * Created by mdzahidraza on 27/11/17.
 */
@Data
public class CellStyle{
    private int verticalAlignment = -1;
    private int horizontalAlignment = -1;
    private float paddingLeft;
    private float paddingRight;
    private float paddingTop;
    private float paddingBottom;
    private float fixedHeight;
    private float calculatedHeight;
    private float minimumHeight;
    private boolean noWrap;
    private int colspan;
    private int rowspan;
    private boolean useDescender;
    private boolean useBorderPadding;
    private int rotation;
    private int border = -1;

    public CellStyle() {
    }

    public CellStyle(int verticalAlignment, float padding) {
        this.verticalAlignment = verticalAlignment;
        this.setPadding(padding);
    }

    public void setPadding(float padding) {
        this.paddingLeft = padding;
        this.paddingRight = padding;
        this.paddingTop = padding;
        this.paddingBottom = padding;
    }
}