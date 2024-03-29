package dev.archimeded.excel4j.processor;

import dev.archimeded.excel4j.processor.contracts.ExcelReader;
import dev.archimeded.excel4j.processor.utils.GeneralUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ExcelReaderImpl implements ExcelReader<T> {
    @Override
    public List<T> read(File file, Class<T> clazz) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file)){

            int sheetNo = GeneralUtil.resolveSheetNumber(clazz);
            XSSFSheet sheet = workbook.getSheetAt(sheetNo);

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()){
                Row row = rowIterator.next();

                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()){

                    Cell cell = cellIterator.next();

                    int cellNo = cell.getColumnIndex();

                    switch (cell.getCellType()){

                        case CellType.NUMERIC -> {

                        }

                        case CellType.STRING -> {

                        }

                        case CellType.BOOLEAN -> {

                        }

                        case CellType.FORMULA -> {

                        }

                        case BLANK -> {

                        }

                        case ERROR -> {

                        }

                        case _NONE -> {

                        }

                        case null, default -> {

                        }
                    }
                }
            }

        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<T> read(FileInputStream fileInputStream, Class<T> clazz) {
        return null;
    }
}
