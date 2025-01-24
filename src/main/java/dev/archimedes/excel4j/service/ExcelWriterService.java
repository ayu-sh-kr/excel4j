package dev.archimedes.excel4j.service;

import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.service.contracts.ExcelWriter;
import dev.archimedes.excel4j.utils.GeneralUtil;
import dev.archimedes.excel4j.utils.WriterUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ExcelWriterService<T> implements ExcelWriter<T> {

    private final List<Exception> exceptions = new ArrayList<>();
    private final ExcelOption<T> option;

    @Override
    public File write(File file, List<T> ts) {

        File temporary;
        try {
            temporary = File.createTempFile("temporary", ".xlsx");
            Files.copy(file.toPath(), temporary.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error creating copy of the file: " + e.getLocalizedMessage(), e.getCause());
        }

        try (ByteArrayInputStream fileInputStream = new ByteArrayInputStream(Files.readAllBytes(temporary.toPath()));
             FileOutputStream fileOutputStream = new FileOutputStream(temporary)
        ) {
            ByteArrayOutputStream outputStream;

            if(option.isOverwrite()){
                outputStream = (ByteArrayOutputStream) write(new ByteArrayInputStream(new byte[0]), ts);
            }
            else {
                outputStream = (ByteArrayOutputStream) write(fileInputStream, ts);
            }

            fileOutputStream.write(outputStream.toByteArray());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean delete = file.delete();
        boolean rename = temporary.renameTo(file);


        if(!delete || !rename) {
            throw new RuntimeException("Failed to replace original file with new file");
        }
        return temporary;
    }

    @Override
    public OutputStream write(InputStream inputStream, List<T> ts) {
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) inputStream;
        byte[] bytes = byteArrayInputStream.readAllBytes();

        try (Workbook workbook = bytes.length > 0 ? new XSSFWorkbook(new ByteArrayInputStream(bytes)) : new XSSFWorkbook()) {

            String sheetName = GeneralUtil.resolveSheetName(option.getClazz());

            Sheet sheet;

            if (option.isOverwrite()) {

                sheet = workbook.createSheet(sheetName);

                Row header = sheet.createRow(0);
                WriterUtils.createHeaderRow(header, option.getClazz(), workbook, sheet);

            } else {
                sheet = workbook.getSheetAt(option.getSheetIndex());
            }

            var map = GeneralUtil.getCellMap(option.getClazz());

            int start = option.isOverwrite() ? 1 : option.getStart();

            for (int i = 0; i < ts.size(); i++) {

                Row row = sheet.createRow(i + start);

                var data = ts.get(i);

                for (Map.Entry<Integer, Field> entry : map.entrySet()) {
                    int idx = entry.getKey();
                    Field field = entry.getValue();
                    WriterUtils.write(field, row, data, idx, option);
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream;

        } catch (Exception e) {
            exceptions.add(e);
        }finally {
            exceptions.forEach(Throwable::printStackTrace);
            exceptions.clear();
        }

        return OutputStream.nullOutputStream();
    }

    @Override
    public void write(File file, T t) {

        try (Workbook workbook = option.isOverwrite() ? new XSSFWorkbook() : new XSSFWorkbook(file);
             FileOutputStream fileOutputStream = new FileOutputStream(file)
        ){

            Sheet sheet;
            if (option.isOverwrite()){
                sheet = workbook.createSheet(GeneralUtil.resolveSheetName(option.getClazz()));

                Row row = sheet.createRow(0);
                WriterUtils.createHeaderRow(row, option.getClazz(), workbook, sheet);

            }
            else {
                sheet = workbook.getSheetAt(option.getSheetIndex());
            }

            Row row = sheet.createRow(option.getStart());

            var map = GeneralUtil.getCellMap(option.getClazz());

            for (Map.Entry<Integer, Field> entry: map.entrySet()){
                int idx = entry.getKey();
                Field field = entry.getValue();
                try {
                    WriterUtils.write(field, row, t, idx, option);
                }catch (Exception e) {
                    exceptions.add(e);
                }
            }

            workbook.write(fileOutputStream);

        } catch (Exception e) {
            exceptions.add(e);
        }
        finally {
            exceptions.forEach(Throwable::printStackTrace);
            exceptions.clear();
        }
    }

}
