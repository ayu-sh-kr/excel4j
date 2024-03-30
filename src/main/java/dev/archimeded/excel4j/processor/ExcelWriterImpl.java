package dev.archimeded.excel4j.processor;

import dev.archimeded.excel4j.annotations.HeaderStyle;
import dev.archimeded.excel4j.processor.contracts.ExcelWriter;
import dev.archimeded.excel4j.processor.utils.GeneralUtil;
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
public class ExcelWriterImpl<T> implements ExcelWriter<T> {

    private final Class<T> clazz;


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

            createHeaderRow(header, clazz, workbook);

            var map = GeneralUtil.getCellMap(clazz);

            for (int i = 0; i < end; i++){

                Row row = sheet.createRow(i + start);

                var data = ts.get(i);

                for(Map.Entry<Integer, Field> entry: map.entrySet()){
                    int idx = entry.getKey();
                    Field field = entry.getValue();

                    Object value = field.get(data);

                    switch (field.getType().getSimpleName()) {
                        case "Integer", "Float", "Double", "int", "float", "double" -> {
                            Cell cell = row.createCell(idx, CellType.NUMERIC);
                            cell.setCellValue((double)value);
                        }

                        case "Boolean", "boolean" -> {
                            Cell cell = row.createCell(idx, CellType.BOOLEAN);
                            cell.setCellValue((boolean)value);
                        }

                        case "Date" -> {
                            Cell cell = row.createCell(idx, CellType.NUMERIC);
                            cell.setCellValue((Date) value);
                        }

                        case "LocalDateTime" -> {
                            Cell cell = row.createCell(idx, CellType.NUMERIC);
                            cell.setCellValue((LocalDateTime) value);
                        }

                        default -> {
                            Cell cell = row.createCell(idx, CellType.STRING);
                            cell.setCellValue((String) value);
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


    private void createHeaderRow(Row row, Class<T> clazz, Workbook workbook){

        var map = GeneralUtil.getColumnMap(clazz);

        System.out.println(map.size());

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
