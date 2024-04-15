package dev.archimedes.excel4j.service.contracts;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ExcelWriter<T> {

    File write(File file, List<T> ts);

    OutputStream write(InputStream inputStream, List<T> ts);

    default void write(File file, T t){}

    default OutputStream write(InputStream inputStream, T t){return null;}
}
