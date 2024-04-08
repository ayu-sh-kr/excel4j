package dev.archimeded.excel4j.service;

import dev.archimeded.excel4j.service.contracts.ExcelReader;
import dev.archimeded.excel4j.service.utils.GeneralUtil;
import dev.archimeded.excel4j.service.utils.TypeResolver;
import lombok.RequiredArgsConstructor;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ExcelReaderService<T> implements ExcelReader<T> {

    private final ExcelOption option;
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


            Map<Integer, Field> fieldMap = GeneralUtil.getCellMap(clazz);

            int end = option.getEnd() == -1 ? sheet.getLastRowNum() : option.getEnd();

            for(int rowIdx = option.getStart(); rowIdx <= end; rowIdx++){

                Row row = sheet.getRow(rowIdx);

                T instance = clazz.getConstructor().newInstance();

                for (Map.Entry<Integer, Field> entry: fieldMap.entrySet()){

                    int cellIndex = entry.getKey();

                    Field field = entry.getValue();

                    Cell cell = row.getCell(cellIndex);

                    Type genericType = field.getGenericType();

                    if(genericType instanceof ParameterizedType parameterizedType){
                        System.out.println("Simple field name: " + field.getType().getSimpleName());
                        System.out.println("Parameterized field name: " + parameterizedType.getActualTypeArguments()[0]);
                    }


                    switch (cell.getCellType()) {
                        case STRING -> TypeResolver.resolveString(instance, field, cell, option.getListDelimiter());
                        case NUMERIC -> TypeResolver.resolveNumber(instance, field, cell, option.getListDelimiter());
                        case BOOLEAN -> TypeResolver.resolveBoolean(instance, field, cell);
                    }
                }
                resultList.add(instance);
            }

            return resultList;

        } catch (IOException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException | ParseException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e.getCause());
        }
    }
}
