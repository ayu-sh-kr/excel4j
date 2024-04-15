package dev.archimedes.excel4j.utils;

import dev.archimedes.excel4j.annotations.HeaderStyle;
import dev.archimedes.excel4j.options.ExcelOption;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WriterUtils {

    private static final int CHARACTER_WIDTH = 256;

    public static void writeList(Cell cell, Object value, String delimiter) {
        if(value instanceof List<?> list){
            StringBuilder builder = new StringBuilder();

            for(Object object: list){
                builder.append(object);
                builder.append(delimiter);
            }

            cell.setCellValue(builder.toString());
            return;
        }
        throw new IllegalStateException("Type not list");
    }

    @SuppressWarnings("unused")
    public static <T> void write(Field field, Row row, Object data, int idx, ExcelOption<T> option) throws IllegalAccessException{
        switch (field.getType().getSimpleName()) {
            case "Integer", "Float", "Double", "int", "float", "double" -> {
                Cell cell = row.createCell(idx, CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(field.get(data).toString()));
            }

            case "Boolean", "boolean" -> {
                Cell cell = row.createCell(idx, CellType.BOOLEAN);
                cell.setCellValue((boolean) field.get(data));
            }

            case "Date" -> {
                Cell cell = row.createCell(idx, CellType.NUMERIC);
                Date date = (Date) field.get(data);
                if (null != date) {
                    cell.setCellValue(date);
                }
            }

            case "LocalDateTime" -> {
                Cell cell = row.createCell(idx, CellType.NUMERIC);
                LocalDateTime dateTime = (LocalDateTime) field.get(data);
                if (null != dateTime) {
                    cell.setCellValue(dateTime.toString());
                }
            }

            case "List" -> {
                Cell cell = row.createCell(idx, CellType.STRING);
                WriterUtils.writeList(cell, field.get(data), option.getListDelimiter());
            }

            case "LocalDate" -> {
                Cell cell = row.createCell(idx, CellType.NUMERIC);
                LocalDate date = (LocalDate) field.get(data);
                if (null != date) {
                    cell.setCellValue(((LocalDate) field.get(data)).toString());
                }
            }

            default -> {
                Cell cell = row.createCell(idx, CellType.STRING);
                cell.setCellValue((String) field.get(data));
            }
        }
    }

    public static <T> void createHeaderRow(Row row, Class<T> clazz, Workbook workbook, Sheet sheet) {

        var map = GeneralUtil.getColumnMap(clazz);

        CellStyle cellStyle;

        if (clazz.isAnnotationPresent(HeaderStyle.class)) {
            HeaderStyle headerStyle = clazz.getAnnotation(HeaderStyle.class);
            cellStyle = getCellStyle(headerStyle, false, workbook);
        } else {
            cellStyle = getCellStyle(null, true, workbook);
        }


        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            int idx = entry.getKey();
            String name = entry.getValue();
            Cell cell = row.createCell(idx, CellType.STRING);
            cell.setCellValue(name);
            cell.setCellStyle(cellStyle);

            sheet.setColumnWidth(idx, name.length() * CHARACTER_WIDTH * 2);
        }
    }


    public static CellStyle getCellStyle(HeaderStyle headerStyle, boolean basic, Workbook workbook) {

        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        if (basic) {
            font.setFontHeightInPoints((short) 14);
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());

            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setIndention((short) 1);
            cellStyle.setWrapText(false);
        } else {
            font.setFontName(headerStyle.font().getValue());
            font.setFontHeightInPoints((short) headerStyle.fontHeight());
            font.setBold(true);
            font.setColor(headerStyle.fontColor().getIndex());

            cellStyle.setFont(font);
            cellStyle.setAlignment(headerStyle.horizontalAlignment());
            cellStyle.setIndention((short) headerStyle.padding());
            cellStyle.setWrapText(headerStyle.wrapText());
        }
        return cellStyle;
    }
}
