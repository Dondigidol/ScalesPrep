package services;

import org.apache.poi.ss.usermodel.Cell;

import java.io.IOException;

public interface XlsHandlingService {

    void getHeaders() throws IOException;
    boolean isCellContains(String[] arr, Cell cell);
    String prepareCellValue(Cell cell);

    void proceed() throws IOException;
    void groupHandling() throws IOException;
    void productHandling();
    void saveToFile() throws IOException;



}
