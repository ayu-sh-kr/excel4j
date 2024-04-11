package dev.archimedes.excel4j.service.contracts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

public interface ExcelWriter<T> {

    File write(File file, List<T> ts, int start, int end, boolean overwrite);

    OutputStream write(ByteArrayOutputStream outputStream, List<T> ts, int start, int end, boolean overwrite);
}
