package dev.archimedes.excel4j.service;

import dev.archimedes.excel4j.annotations.HeaderStyle;
import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.service.contracts.ExcelWriter;
import dev.archimedes.excel4j.utils.GeneralUtil;
import dev.archimedes.excel4j.utils.WriterUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ExcelWriterService<T> implements ExcelWriter<T> {

    private final ExcelOption<T> option;

    private static final int CHARACTER_WIDTH = 256;

    private static final int DEFAULT_CELL_PADDING = 5;


    @Override
    public File write(File file, List<T> ts, int start, int end, boolean overwrite) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            OutputStream fileStream =  write(byteArrayOutputStream, ts, start, end, overwrite);
            if (!(fileStream instanceof ByteArrayOutputStream)) {
                throw new IllegalArgumentException("The OutputStream should be an instance of ByteArrayOutputStream");
            }
            byte[] data =  ((ByteArrayOutputStream) fileStream).toByteArray();
            fileOutputStream.write(data);
            return file;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OutputStream write(ByteArrayOutputStream outputStream, List<T> ts, int start, int end, boolean overwrite) {
        try (Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Sheet 1");
            Row header = sheet.createRow(0);

            createHeaderRow(header, option.getClazz(), workbook, sheet);

            var map = GeneralUtil.getCellMap(option.getClazz());

            for (int i = 0; i < end; i++){

                Row row = sheet.createRow(i + start);

                var data = ts.get(i);

                for(Map.Entry<Integer, Field> entry: map.entrySet()){
                    int idx = entry.getKey();
                    Field field = entry.getValue();


                    switch (field.getType().getSimpleName()) {
                        case "Integer", "Float", "Double", "int", "float", "double" -> {
                            Cell cell = row.createCell(idx, CellType.NUMERIC);
                            cell.setCellValue(Double.parseDouble(field.get(data).toString()));
                        }

                        case "Boolean", "boolean" -> {
                            Cell cell = row.createCell(idx, CellType.BOOLEAN);
                            cell.setCellValue((boolean)field.get(data));
                        }

                        case "Date" -> {
                            Cell cell = row.createCell(idx, CellType.NUMERIC);
                            cell.setCellValue((Date) field.get(data));
                        }

                        case "LocalDateTime" -> {
                            Cell cell = row.createCell(idx, CellType.NUMERIC);
                            cell.setCellValue((LocalDateTime) field.get(data));
                        }

                        case "List" -> {
                            Cell cell = row.createCell(idx, CellType.STRING);
                            WriterUtils.writeList(cell, field.get(data), option.getListDelimiter());
                        }

                        default -> {
                            Cell cell = row.createCell(idx, CellType.STRING);
                            cell.setCellValue((String) field.get(data));
                        }
                    }
                }
            }

            workbook.write(outputStream);

        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage(), e.getCause());
        }
        return outputStream;
    }


    private void createHeaderRow(Row row, Class<T> clazz, Workbook workbook, Sheet sheet){

        var map = GeneralUtil.getColumnMap(clazz);

        CellStyle cellStyle;

        if(clazz.isAnnotationPresent(HeaderStyle.class)){
            HeaderStyle headerStyle = clazz.getAnnotation(HeaderStyle.class);
            cellStyle = getCellStyle(headerStyle, false, workbook);
        }else {
            cellStyle = getCellStyle(null, true, workbook);
        }


        for (Map.Entry<Integer, String> entry: map.entrySet()){
            int idx = entry.getKey();
            String name = entry.getValue();
            Cell cell = row.createCell(idx, CellType.STRING);
            cell.setCellValue(name);
            cell.setCellStyle(cellStyle);

            sheet.setColumnWidth(idx, name.length() * CHARACTER_WIDTH * 2);
        }

    }


    private CellStyle getCellStyle(HeaderStyle headerStyle, boolean basic, Workbook workbook){

        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        if(basic) {
            font.setFontHeightInPoints((short) 14);
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());

            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setIndention((short) 1);
            cellStyle.setWrapText(false);
        }else {
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
