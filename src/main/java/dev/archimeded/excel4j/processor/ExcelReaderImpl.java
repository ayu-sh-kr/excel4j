package dev.archimeded.excel4j.processor;

import dev.archimeded.excel4j.processor.contracts.ExcelReader;
import dev.archimeded.excel4j.processor.utils.GeneralUtil;
import dev.archimeded.excel4j.processor.utils.TypeResolver;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelReaderImpl<T> implements ExcelReader<T> {
    @Override
    public List<T> read(File file, Class<T> clazz) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return read(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> read(InputStream inputStream, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)){

            int sheetNo = GeneralUtil.resolveSheetNumber(clazz);
            XSSFSheet sheet = workbook.getSheetAt(sheetNo);

            Row headerRow = sheet.getRow(0);

            for (Cell cell : headerRow) {
                int cellIndex = cell.getColumnIndex();
                String headerName = cell.getStringCellValue();
                System.out.println("Header index: " + cellIndex + ", Header name: " + headerName);
            }

            Map<Integer, Field> fieldMap = GeneralUtil.getCellMap(clazz);

            for(int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++){

                Row row = sheet.getRow(rowIdx);
                T instance = clazz.getDeclaredConstructor().newInstance();

                for (Map.Entry<Integer, Field> entry: fieldMap.entrySet()){

                    int cellIndex = entry.getKey();

                    Field field = entry.getValue();

                    Cell cell = row.getCell(cellIndex);

                    TypeResolver<T> typeResolver = new TypeResolver<>();

                    switch (cell.getCellType()) {
                        case STRING -> typeResolver.resolveString(instance, field, cell);
                        case NUMERIC -> typeResolver.resolveNumber(instance, field, cell);
                        case BOOLEAN -> typeResolver.resolveBoolean(instance, field, cell);
                    }
                }
                resultList.add(instance);
            }

            return resultList;

        } catch (IOException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
