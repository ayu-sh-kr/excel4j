package dev.archimeded.excel4j.processor.utils;

import dev.archimeded.excel4j.annotations.ExcelCell;
import dev.archimeded.excel4j.annotations.ExcelSheet;

import java.lang.reflect.Field;
import java.util.*;

public class GeneralUtil {

    public static Set<String> resolveColumnsName(Class<?> clazz){

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


    public static Map<Integer, String> getCellMap(Class<?> clazz){
        if(!clazz.isAnnotationPresent(ExcelSheet.class)){
            throw new RuntimeException("Class not annotated with @ExcelSheet");
        }

        List<Field> fields = List.of(clazz.getFields());

        Map<Integer, String> columnMap = new HashMap<>();

        fields
                .forEach(field -> {
                    if (field.isAnnotationPresent(ExcelCell.class)
                        && field.getAnnotation(ExcelCell.class).cellNumber() != -1){
                        int idx = field.getAnnotation(ExcelCell.class).cellNumber();
                        String name = field.getAnnotation(ExcelCell.class).name();

                        columnMap.put(idx, name);
                    }
                });

        return columnMap;
    }

}
