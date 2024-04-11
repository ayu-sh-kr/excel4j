package dev.archimeded.excel4j;

import dev.archimeded.excel4j.entity.Product;
import dev.archimeded.excel4j.options.ExcelOption;
import dev.archimeded.excel4j.options.ExcelOption.ExcelOptionBuilder;
import dev.archimeded.excel4j.processors.ExcelProcessor;
import dev.archimeded.excel4j.service.contracts.ExcelReader;
import dev.archimeded.excel4j.service.contracts.ExcelWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        ExcelOptionBuilder<Product> builder = new ExcelOptionBuilder<>();

        ExcelOption<Product> options = builder
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
