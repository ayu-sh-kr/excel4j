package dev.archimeded.excel4j;

import dev.archimeded.excel4j.entity.Product;
import dev.archimeded.excel4j.processor.ExcelOption;
import dev.archimeded.excel4j.processor.ExcelReaderImpl;
import dev.archimeded.excel4j.processor.ExcelWriterImpl;
import dev.archimeded.excel4j.processor.contracts.ExcelReader;
import dev.archimeded.excel4j.processor.contracts.ExcelWriter;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ExcelReader<Product> excelReader = new ExcelReaderImpl<>();
        List<Product> products = excelReader.read(new File("Book1.xlsx"), Product.class);
        products.forEach(System.out::println);

        ExcelOption excelOption = ExcelOption.builder()
                .with(Product.class)
                .build();

        ExcelWriter<Product> excelWriter = new ExcelWriterImpl<>(Product.class);

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) excelWriter.write(new ByteArrayOutputStream(), products, 1, products.size(), true);

        File file = new File("product.xlsx");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(outputStream.toByteArray());
            boolean newFile = file.createNewFile();
            System.out.println(newFile);
        }catch (IOException ioException){
            System.out.println(ioException.getLocalizedMessage());
        }
    }
}
