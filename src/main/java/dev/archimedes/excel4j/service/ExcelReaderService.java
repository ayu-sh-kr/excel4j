package dev.archimedes.excel4j.service;

import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.service.contracts.ExcelReader;
import dev.archimedes.excel4j.utils.GeneralUtil;
import dev.archimedes.excel4j.utils.ReaderUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ExcelReaderService<T> implements ExcelReader<T> {

    private final ExcelOption<T> option;
    @Override
    public List<T> read(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> read(InputStream inputStream) {
        List<T> resultList = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)){

            int sheetNo = GeneralUtil.resolveSheetNumber(option.getClazz());
            XSSFSheet sheet = workbook.getSheetAt(sheetNo);


            Map<Integer, Field> fieldMap = GeneralUtil.getCellMap(option.getClazz());

            int end = option.getEnd() == -1 ? sheet.getLastRowNum() : option.getEnd();

            for(int rowIdx = option.getStart(); rowIdx <= end; rowIdx++){

                Row row = sheet.getRow(rowIdx);

                T instance = option.getClazz().getConstructor().newInstance();

                for (Map.Entry<Integer, Field> entry: fieldMap.entrySet()){

                    int cellIndex = entry.getKey();

                    Field field = entry.getValue();

                    Cell cell = row.getCell(cellIndex);

                    ReaderUtils.read(rowIdx, field, cell, option, instance);

                }
                resultList.add(instance);
            }

            return resultList;

        } catch (IOException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException | ParseException | ClassCastException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public T readOne(File file){

        try (Workbook workbook = new XSSFWorkbook(file)){

            Sheet sheet = workbook.getSheetAt(option.getSheetIndex());

            Row row = sheet.getRow(option.getStart());

            T instance = option.getClazz().getConstructor().newInstance();

            var map = GeneralUtil.getCellMap(option.getClazz());

            for (Map.Entry<Integer, Field> entry: map.entrySet()){
                int cellIndex = entry.getKey();

                Field field = entry.getValue();

                Cell cell = row.getCell(cellIndex);

                ReaderUtils.read(option.getStart(), field, cell, option, instance);
            }

            return instance;

        } catch (IOException | InvalidFormatException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException | InstantiationException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
