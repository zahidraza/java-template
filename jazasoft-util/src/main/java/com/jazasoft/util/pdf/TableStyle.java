package com.jazasoft.util.pdf;

import com.itextpdf.text.Element;
import lombok.Data;

/**
 * Created by mdzahidraza on 27/11/17.
 */
@Data
public class TableStyle {
    private int horizontalAlignment = Element.ALIGN_CENTER;
    private float widthPercent = 80.0f;
    private float[] widths;
    private CellStyle cellStyle;

    public TableStyle() {
    }
}
