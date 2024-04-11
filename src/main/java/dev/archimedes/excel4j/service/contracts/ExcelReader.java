package dev.archimedes.excel4j.service.contracts;


import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ExcelReader<T> {

    List<T> read(File file);

    List<T> read(InputStream inputStream);

}
