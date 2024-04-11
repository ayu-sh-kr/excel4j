package dev.archimedes.excel4j;

import dev.archimedes.excel4j.entity.Product;
import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.processors.ExcelProcessor;
import dev.archimedes.excel4j.service.contracts.ExcelReader;
import dev.archimedes.excel4j.service.contracts.ExcelWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        ExcelOption<Product> options = ExcelOption.<Product>builder()
                .with(Product.class)
                .start(1)
                .sheetIndex(0)
                .listDelimiter(";")
                .build();

        ExcelReader<Product> excelReader = ExcelProcessor.getReaderFromOption(options);
        List<Product> products = excelReader.read(new File("Book1.xlsx"));
        products.forEach(System.out::println);

        ExcelWriter<Product> excelWriter = ExcelProcessor.getWriterFromOption(options);

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) excelWriter
                .write(new ByteArrayOutputStream(), products, 1, products.size(), true);

        File file = new File("product.xlsx");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(outputStream.toByteArray());
            boolean newFile = file.createNewFile();
        }catch (IOException ioException){
            System.out.println(ioException.getLocalizedMessage());
        }
    }
}
