package com.jazasoft.util.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mdzahidraza on 27/11/17.
 */
public class PdfUtils {


    public static PdfPTable getTable(@NotNull List<List<String>> data, List<String> header, TableStyle style) throws DocumentException {
        List<List<Cell>> data2 = data.stream().map(row -> row.stream().map(content -> new Cell(content)).collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<Cell> headerList = null;
        if (header != null) {
            headerList = header.stream().map(h -> new Cell(h)).collect(Collectors.toList());
        }
        return getTable(style, data2, headerList);
    }

    public static PdfPTable getTable(TableStyle style, @NotNull List<List<Cell>> data, List<Cell> header) throws DocumentException {
        int noOfColumns = data.get(0).size();
        Font boldFont = Styles.getBoldFont();
        Font normalFont = Styles.getNormalFont();
        PdfPTable table = new PdfPTable(noOfColumns);

        CellStyle cellStyle = null;
        if (style != null) {
            setTableStyle(table, style);
            cellStyle = style.getCellStyle();
        }

        if (header != null) {
            List<Float> widthList = header.stream().map(Cell::getWidth).collect(Collectors.toList());
            float[] width = new float[widthList.size()];
            for (int i = 0; i < widthList.size(); i++) {
                width[i] = widthList.get(i);
            }
            table.setWidths(width);

            for (Cell cell: header) {
                PdfPCell c = getCell(new Phrase(cell.getContent(), boldFont),cellStyle);
                c.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(c);
            }
            table.setHeaderRows(1);
        }

        for (List<Cell> row : data) {
            for (Cell cell : row) {
                if (cell.getPhrase() != null) {
                    table.addCell(getCell(cell.getPhrase(), cellStyle, cell.getRowspan(), cell.getColspan()));
                }else {
                    table.addCell(getCell(new Phrase(cell.getContent(), normalFont), cellStyle, cell.getRowspan(), cell.getColspan()));
                }

            }
        }

        return table;
    }

    public static PdfPCell getCell(String content, CellStyle style) {
        return getCell(new Phrase(content), style);
    }

    public static PdfPCell getCell(Phrase phrase, CellStyle style) {
        return getCell(phrase, style, 1, 1);
    }

    public static PdfPCell getCell(Phrase phrase, CellStyle style, int rowspan, int colspan) {
        PdfPCell cell = new PdfPCell(phrase);
        if (style != null) {
            setCellStyle(cell, style);
        }
        cell.setRowspan(rowspan);
        cell.setColspan(colspan);
        return cell;
    }

    public static void setTableStyle(@NotNull PdfPTable table, @NotNull TableStyle style) throws DocumentException {
        table.setHorizontalAlignment(style.getHorizontalAlignment());
        table.setWidthPercentage(style.getWidthPercent());
        if (style.getWidths() != null) {
            table.setWidths(style.getWidths());
        }
    }

    public static void setCellStyle(PdfPCell cell, CellStyle style) {
        if (style.getVerticalAlignment() != -1) {
            cell.setVerticalAlignment(style.getVerticalAlignment());
        }
        if (style.getHorizontalAlignment() != -1) {
            cell.setHorizontalAlignment(style.getHorizontalAlignment());
        }
        if (style.getPaddingLeft() != 0) {
            cell.setPaddingLeft(style.getPaddingLeft());
        }
        if (style.getPaddingRight() != 0) {
            cell.setPaddingRight(style.getPaddingRight());
        }
        if (style.getPaddingTop() != 0) {
            cell.setPaddingTop(style.getPaddingTop());
        }
        if (style.getPaddingBottom() != 0) {
            cell.setPaddingBottom(style.getPaddingBottom());
        }
        if (style.getFixedHeight() != 0) {
            cell.setFixedHeight(style.getFixedHeight());
        }
        if (style.getCalculatedHeight() != 0) {
            cell.setCalculatedHeight(style.getCalculatedHeight());
        }
        if (style.getMinimumHeight() != 0) {
            cell.setMinimumHeight(style.getMinimumHeight());
        }
        if (style.getColspan() != 0) {
            cell.setColspan(style.getColspan());
        }
        if (style.getRowspan() != 0) {
            cell.setRowspan(style.getRowspan());
        }
        if (style.getRotation() != 0) {
            cell.setRotation(style.getRotation());
        }
        if (style.getBorder() != -1) {
            cell.setBorder(style.getBorder());
        }
        cell.setNoWrap(style.isNoWrap());
        cell.setUseDescender(style.isUseDescender());
        cell.setUseBorderPadding(style.isUseBorderPadding());
    }

}
