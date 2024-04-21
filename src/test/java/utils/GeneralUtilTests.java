package utils;

import dev.archimedes.excel4j.utils.GeneralUtil;
import entities.EmployeeData;
import entities.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneralUtilTests {

    @Test
    public void testClassName(){
        String sheetName = GeneralUtil.resolveSheetName(Product.class);
        assertEquals("Product", sheetName, "Name should be same");
        assertEquals("Employee Data", GeneralUtil.resolveSheetName(EmployeeData.class), "Name should be same");
    }
}
