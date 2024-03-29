package dev.archimeded.excel4j.processor.contracts;


import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ExcelReader<T> {

    List<T> read(File file, Class<T> clazz);

    List<T> read(InputStream inputStream, Class<T> clazz);

}
