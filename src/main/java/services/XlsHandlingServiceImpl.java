package services;

import entities.Product;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XlsHandlingServiceImpl implements XlsHandlingService{
    private static Logger log = Logger.getLogger(XlsHandlingServiceImpl.class.getName());
    private String fileName; // название обрабатываемого файла
    Properties headerProperties = new Properties();


    private String[] skuHeaders;
    private String[] priceHeaders;
    private String[] nameHeaders;
    private String[] labelHeaders;
    private Workbook workbook;
    private Sheet sheet;
    private Integer skuColPos;
    private Integer nameColPos;
    private Integer priceColPos;
    private Integer labelColPos;

    public XlsHandlingServiceImpl(String fileName) throws IOException{
        this.fileName = fileName;
        workbook = WorkbookFactory.create(new File(fileName));
        sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        try {
            InputStream  is= getClass().getResourceAsStream("/headers.properties");
            InputStreamReader inputProperties = new InputStreamReader(is, "cp1251");
            headerProperties = new Properties();
            headerProperties.load(inputProperties);
            skuHeaders = headerProperties.getProperty("headers.sku").split(",");
            priceHeaders = headerProperties.getProperty("headers.price").split(",");
            nameHeaders = headerProperties.getProperty("headers.name").split(",");
            labelHeaders = headerProperties.getProperty("headers.label").split(",");

        } catch (IOException e){
            log.log(Level.SEVERE, "Ошибка! " + e.getMessage());
        }
    }

    public void processing() throws IOException{
        getHeaders();
        int lastrow = sheet.getLastRowNum();

        FileWriter fileWriter = new FileWriter("import.txt", false);

        for(int i=1; i<=lastrow; i++){
            Row row = sheet.getRow(i);
            if (!prepareCellValue(row.getCell(labelColPos)).equals("")){
                Product product = new Product();
                product.setId(prepareCellValue(row.getCell(labelColPos)));
                product.setSku(prepareCellValue(row.getCell(skuColPos)));
                product.setName(prepareCellValue(row.getCell(nameColPos)));
                product.setPrice(prepareCellValue(row.getCell(priceColPos)).replace(",", "."));
                fileWriter.append(product.toString());
                fileWriter.append("\n");
            }
        }
        fileWriter.flush();
    }

    public String prepareCellValue(Cell cell){
        String value;
        DataFormatter formatter = new DataFormatter();
        value = formatter.formatCellValue(cell).replace("\"", "");
        return value;
    }

    public boolean isCellContains(String[] arr, Cell cell){
        boolean returnValue = false;
        String value2 = prepareCellValue(cell).toLowerCase();
        for(String arrValue:arr){
            if(value2.contains(arrValue)){
                returnValue =true;
                break;
            }
        }
        return returnValue;
    }

    public void getHeaders() throws IOException{
        Row row = sheet.getRow(0);
        int lastcol = row.getLastCellNum();
        int tmpSkuColPos = -1;
        int tmpNameColPos = -1;
        int tmpPriceColPos = -1;
        int tmpLabelColPos = -1;

        for (int j=0; j<=lastcol; j++){
            Cell cell = row.getCell(j);
            if(cell != null){
                if (isCellContains(skuHeaders, cell)) {
                    tmpSkuColPos = j;
                }
                if (isCellContains(nameHeaders, cell)) {
                    tmpNameColPos = j;
                }
                if (isCellContains(priceHeaders, cell)) {
                    tmpPriceColPos = j;
                }
                if (isCellContains(labelHeaders, cell)) {
                    tmpLabelColPos = j;
                }
            }
        }
        if ((tmpSkuColPos != -1) && (tmpNameColPos != -1) && (tmpPriceColPos != -1) && (tmpLabelColPos != -1)){
            this.skuColPos = tmpSkuColPos;
            this.nameColPos = tmpNameColPos;
            this.priceColPos = tmpPriceColPos;
            this.labelColPos = tmpLabelColPos;
        } else {
            throw new IOException("Отсутствуют, либо неверные заголовки столбцов!");
        }
    }

    private void saveToTxt(){

    }

}
