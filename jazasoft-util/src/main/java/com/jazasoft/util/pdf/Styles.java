package com.jazasoft.util.pdf;

import com.itextpdf.text.Font;

/**
 * Created by mdzahidraza on 27/11/17.
 */
public class Styles {

    public static Font getBoldFont() {
        return new Font(Font.FontFamily.COURIER, 10.0f, Font.BOLD);
    }

    public static Font getHeaderFont() {
        return new Font(Font.FontFamily.COURIER, 14.0f, Font.BOLD);
    }

    public static Font getNormalFont() {
        return new Font(Font.FontFamily.COURIER, 10.0f, Font.NORMAL);
    }

    public static Font getSmallFont() {
        return new Font(Font.FontFamily.COURIER, 8.0f, Font.NORMAL);
    }

    public static Font getLargeFont() {
        return new Font(Font.FontFamily.COURIER, 12.0f, Font.NORMAL);
    }

    public static CellStyle getNormalCellStyle() {
        CellStyle cellStyle = new CellStyle();
        cellStyle.setPadding(2.0f);
        return cellStyle;
    }
}
