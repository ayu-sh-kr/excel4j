package dev.archimeded.excel4j.utils;

import dev.archimeded.excel4j.options.ExcelOption;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class TypeResolver {

    public static <T> void resolveString(T instance, Field field, Cell cell) throws IllegalAccessException {
        field.set(instance, cell.getStringCellValue());
    }

    public static <T> void resolveNumber(T instance, Field field, Cell cell) throws IllegalAccessException, ParseException {
        var parser = NumberFormat.getInstance();
        var value = parser.parse(String.valueOf(cell.getNumericCellValue()));
        field.set(instance, value);
    }

    public static <T> void resolveBoolean(T instance, Field field, Cell cell) throws IllegalAccessException {
        field.setBoolean(instance, cell.getBooleanCellValue());
    }

    public static <T> void resolveInteger(T instance, Field field, Cell cell) {
        try {
            resolveNumber(instance, field, cell);
        } catch (ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void resolveLong(T instance, Field field, Cell cell) {
        try {
            resolveNumber(instance, field, cell);
        } catch (ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void resolveFloat(T instance, Field field, Cell cell) {
        try {
            resolveNumber(instance, field, cell);
        } catch (ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void resolveDouble(T instance, Field field, Cell cell) {
        try {
            resolveNumber(instance, field, cell);
        } catch (ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void resolveList(T instance, Field field, Cell cell, ExcelOption<T> option) throws IllegalAccessException {
        Type genericType = field.getGenericType();
        if(genericType instanceof ParameterizedType parameterizedType){
            Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
            switch (actualTypeArgument.getTypeName()) {
                case "java.lang.Integer", "java.lang.Long", "java.lang.Double", "java.lang.Float" -> field.set(
                        instance, Arrays.stream(cell.getStringCellValue().split(option.getListDelimiter()))
                                        .map(value -> {
                                            try {
                                                return NumberFormat.getInstance().parse(value);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        })
                                        .toList()
                );

                case "java.lang.String" -> field.set(instance, Arrays.stream(cell.getStringCellValue().split(option.getListDelimiter())).toList());
                case "java.util.Date" -> field.set(
                        instance, Arrays.stream(cell.getStringCellValue().split(option.getListDelimiter()))
                                        .map(value -> {
                                            SimpleDateFormat formatter = new SimpleDateFormat(option.getDateTimeRegex());
                                            try {
                                                return formatter.parse(value);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        })
                                .toList()
                );
                case "java.util.LocalDate" -> field.set(
                        instance, Arrays.stream(cell.getStringCellValue().split(option.getListDelimiter()))
                                        .map(value -> {
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(option.getDateRegex());
                                            return LocalDate.parse(value, formatter);
                                        })
                                .toList()
                );
            }
        }
    }

    public static <T> void resolveDate(T instance, Field field, Cell cell, ExcelOption<T> option) throws ParseException, IllegalAccessException {
        switch (cell.getCellType()) {
            case NUMERIC -> field.set(instance, cell.getDateCellValue());
            case STRING -> {
                SimpleDateFormat format = new SimpleDateFormat(option.getDateTimeRegex());
                Date date = format.parse(cell.getStringCellValue());
                field.set(instance, date);
            }
        }
    }


    public static <T> void resolveLocalDate(T instance, Field field, Cell cell, ExcelOption<T> option) throws IllegalAccessException {
        switch (cell.getCellType()) {
            case NUMERIC -> field.set(instance, cell.getDateCellValue());
            case STRING -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(option.getDateRegex());
                LocalDate date = LocalDate.parse(cell.getStringCellValue(), formatter);
                field.set(instance, date);
            }
        }
    }
}
