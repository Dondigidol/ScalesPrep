package services;

import entities.Product;
import org.apache.poi.sl.usermodel.ObjectMetaData;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.Buffer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface XlsHandlingService {

    void getHeaders() throws IOException;
    boolean isCellContains(String[] arr, Cell cell);
    String prepareCellValue(Cell cell);
    void processing() throws IOException;



}
