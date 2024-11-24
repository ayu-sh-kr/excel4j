package dev.archimedes.excel4j.resolvers;

import dev.archimedes.excel4j.enums.DateTypeRegex;
import dev.archimedes.excel4j.options.ExcelOption;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class TypeResolver {

    public static <T> void resolveString(T instance, Field field, Cell cell) {
        try {
            if (cell == null || StringUtil.isBlank(cell.toString())) {
                field.set(instance, null);
            } else {
                field.set(instance, cell.toString());
            }
        } catch (IllegalAccessException | IllegalStateException e) {
            throw new RuntimeException(ResolveExceptionLog.prepare(field, cell, e.getLocalizedMessage()), e.getCause());
        }
    }

    public static Number resolveNumber(Cell cell) throws ParseException {
        if (cell == null) {
            return null;
        }
        var parser = NumberFormat.getInstance();
        return parser.parse(String.valueOf(cell.getNumericCellValue()));
    }

    public static <T> void resolveBoolean(T instance, Field field, Cell cell) throws IllegalAccessException {
        if (cell != null) {
            field.setBoolean(instance, cell.getBooleanCellValue());
        } else {
            field.set(instance, null);
        }
    }

    public static <T> void resolveInteger(T instance, Field field, Cell cell) {
        try {
            var value = resolveNumber(cell);
            if (value != null && StringUtil.isNotBlank(value.toString())) {
                field.set(instance, Integer.parseInt(value.toString()));
            } else {
                field.set(instance, null);
            }
        } catch (IllegalAccessException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void resolveLong(T instance, Field field, Cell cell) {
        try {
            var value = resolveNumber(cell);
            if (value != null && StringUtil.isNotBlank(value.toString())) {
                field.set(instance, value);
            } else {
                field.set(instance, null);
            }
        } catch (ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void resolveFloat(T instance, Field field, Cell cell) {
        try {
            var value = resolveNumber(cell);
            if (value != null && StringUtil.isNotBlank(value.toString())) {
                field.set(instance, Float.parseFloat(value.toString()));
            } else {
                field.set(instance, null);
            }
        } catch (ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void resolveDouble(T instance, Field field, Cell cell) {
        try {
            var value = resolveNumber(cell);
            if (value != null && StringUtil.isNotBlank(value.toString())) {
                field.set(instance, Double.parseDouble(value.toString()));
            } else {
                field.set(instance, null);
            }
        } catch (ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void resolveList(T instance, Field field, Cell cell, ExcelOption<T> option) throws IllegalAccessException {
        if (cell == null || StringUtil.isBlank(cell.toString())) {
            field.set(instance, null);
            return;
        }

        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType parameterizedType) {
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

                case "java.lang.String" -> field.set(instance, Arrays.stream(cell.getStringCellValue().split(option.getListDelimiter())).map(String::trim).toList());
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

    public static <T> void resolveDate(T instance, Field field, Cell cell, int rowIdx, ExcelOption<T> option) throws IllegalAccessException {
        if (cell == null) {
            field.set(instance, null);
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat(option.getDateTimeRegex());
        try {
            switch (cell.getCellType()) {
                case NUMERIC -> {
                    if (StringUtil.isNotBlank(String.valueOf(cell.getNumericCellValue()))) {
                        field.set(instance, cell.getDateCellValue());
                    }
                }
                case STRING -> {
                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                        Date date = format.parse(cell.getStringCellValue());
                        field.set(instance, date);
                    }
                }
            }
        } catch (ParseException | IllegalAccessException | IllegalStateException e) {
            throw new RuntimeException(ResolveExceptionLog.prepare(field, cell, e.getLocalizedMessage()) + " row: " + rowIdx, e.getCause());
        }
    }

    public static <T> void resolveLocalDate(T instance, Field field, Cell cell, ExcelOption<T> option) throws IllegalAccessException {
        if (cell == null) {
            field.set(instance, null);
            return;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC -> {
                    if (StringUtil.isNotBlank(String.valueOf(cell.getNumericCellValue()))) {
                        field.set(
                                instance, cell.getDateCellValue()
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                        );
                    }
                }
                case STRING -> {
                    if (StringUtil.isNotBlank(cell.getStringCellValue()) && DateTypeRegex.validate(cell.getStringCellValue(), DateTypeRegex.LOCAL_DATE)) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(option.getDateRegex());
                        LocalDate date = LocalDate.parse(cell.getStringCellValue(), formatter);
                        field.set(instance, date);
                    } else {
                        field.set(instance, null);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(ResolveExceptionLog.prepare(field, cell, e.getLocalizedMessage()));
        }
    }

    public static <T> void resolveLocalDateTime(T instance, Field field, Cell cell, ExcelOption<T> option) throws IllegalAccessException {
        if (cell == null) {
            field.set(instance, null);
            return;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC -> {
                    if (StringUtil.isNotBlank(String.valueOf(cell.getNumericCellValue()))) {
                        field.set(
                                instance, cell.getDateCellValue()
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime()
                        );
                    }
                }

                case STRING -> {
                    if (StringUtil.isNotBlank(cell.getStringCellValue()) && DateTypeRegex.validate(cell.getStringCellValue(), DateTypeRegex.LOCAL_DATE_TIME)) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(option.getDateTimeRegex());
                        LocalDateTime date = LocalDateTime.parse(cell.getStringCellValue(), formatter);
                        field.set(instance, date);
                    } else {
                        field.set(instance, null);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(ResolveExceptionLog.prepare(field, cell, e.getLocalizedMessage()));
        }
    }

    public static <T> void resolveLocalTime(T instance, Field field, Cell cell, ExcelOption<T> option) throws IllegalAccessException {
        if (cell == null) {
            field.set(instance, null);
            return;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC -> {
                    if (StringUtil.isNotBlank(String.valueOf(cell.getNumericCellValue()))) {
                        field.set(
                                instance, cell.getDateCellValue()
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalTime()
                        );
                    }
                }

                case STRING -> {
                    if (StringUtil.isNotBlank(cell.getStringCellValue()) && DateTypeRegex.validate(cell.getStringCellValue(), DateTypeRegex.LOCAL_TIME)) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(option.getTimeRegex());
                        LocalTime date = LocalTime.parse(cell.getStringCellValue(), formatter);
                        field.set(instance, date);
                    } else {
                        field.set(instance, null);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(ResolveExceptionLog.prepare(field, cell, e.getLocalizedMessage()));
        }
    }
}