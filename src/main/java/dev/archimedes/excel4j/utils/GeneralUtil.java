package dev.archimedes.excel4j.utils;

import dev.archimedes.excel4j.annotations.ExcelCell;
import dev.archimedes.excel4j.annotations.ExcelSheet;
import org.apache.poi.util.StringUtil;

import java.lang.reflect.Field;
import java.util.*;

public class GeneralUtil {

    @SuppressWarnings("unused")
    public static <T> Set<String> resolveColumnsName(Class<T> clazz){

        if(!clazz.isAnnotationPresent(ExcelSheet.class)){
            throw new RuntimeException("Class not annotated with @ExcelSheet");
        }

        List<Field> fields = List.of(clazz.getFields());

        Set<String> names = new HashSet<>();

        for (Field field: fields){
            if(field.isAnnotationPresent(ExcelCell.class)){
                String column = field.getAnnotation(ExcelCell.class).name();
                names.add(column);
            }
        }
        return names;
    }

    public static int resolveSheetNumber(Class<?> clazz) {
        if(!clazz.isAnnotationPresent(ExcelSheet.class)){
            throw new RuntimeException("Class not annotated with @ExcelSheet");
        }
        return clazz.getAnnotation(ExcelSheet.class).sheetNumber();
    }


    public static <T> Map<Integer, Field> getCellMap(Class<T> clazz){
        if(!clazz.isAnnotationPresent(ExcelSheet.class)){
            throw new RuntimeException("Class not annotated with @ExcelSheet");
        }

        List<Field> fields = List.of(clazz.getDeclaredFields());

        Map<Integer, Field> columnMap = new HashMap<>();

        fields
                .forEach(field -> {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(ExcelCell.class)
                        && field.getAnnotation(ExcelCell.class).cellNumber() != -1){
                        int idx = field.getAnnotation(ExcelCell.class).cellNumber();
                        columnMap.put(idx, field);
                    }
                });

        return columnMap;
    }

    public static <T> Map<Integer, String> getColumnMap(Class<T> clazz) {

        if(!clazz.isAnnotationPresent(ExcelSheet.class)){
            throw new RuntimeException("Class not annotated with @ExcelSheet");
        }

        Field[] fields = clazz.getDeclaredFields();
        Map<Integer, String> columnMap = new HashMap<>();

        for (Field field: fields){
            if(field.isAnnotationPresent(ExcelCell.class)){
                columnMap.put(
                        field.getAnnotation(ExcelCell.class).cellNumber(),
                        field.getAnnotation(ExcelCell.class).name()
                );
            }
        }
        return columnMap;
    }

    public static <T> String resolveSheetName(Class<T> clazz){
        ExcelSheet sheet = clazz.getAnnotation(ExcelSheet.class);
        if(StringUtil.isNotBlank(sheet.name())){
            return sheet.name();
        }

        String simpleName = clazz.getSimpleName();
        String[] words = simpleName.split("(?<=[a-z])(?=[A-Z])");

        for (int i = 0; i < words.length; i++){
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        return String.join(" ", words);
    }
}
