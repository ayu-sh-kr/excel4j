import entities.Product;
import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.processors.ExcelProcessor;
import dev.archimedes.excel4j.service.contracts.ExcelReader;
import entities.EmployeeData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadTest {
    @Test
    public void read(){

        ExcelOption<Product> option = ExcelOption.<Product>builder()
                .with(Product.class)
                .start(1)
                .dateRegex("dd/MM/yyyy")
                .listDelimiter(";")
                .sheetIndex(0)
                .build();

        ExcelReader<Product> reader = ExcelProcessor.getReaderFromOption(option);

        List<Product> products = reader.read(new File("Book1.xlsx"));

        assertEquals(3, products.size(), "Products size should be 4");
    }

    @Test
    public void readEmployee(){
        ExcelOption<EmployeeData> option = ExcelOption.<EmployeeData>builder()
                .sheetIndex(0)
                .start(1)
                .with(EmployeeData.class)
                .build();

        ExcelReader<EmployeeData> reader = ExcelProcessor.getReaderFromOption(option);
        List<EmployeeData> employeeData = reader.read(new File("src/main/resources/Assignment_Timecard.xlsx"));
        assertEquals(1484, employeeData.size());
    }
}
