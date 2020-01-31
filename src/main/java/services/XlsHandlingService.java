package services;

import entities.Product;
import org.apache.poi.sl.usermodel.ObjectMetaData;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XlsHandlingService {
    private static Logger log = Logger.getLogger(XlsHandlingService.class.getName());
    private String fileName; // название обрабатываемого файла


    private String skuHeaders[];// = headerProperties.getProperty("headers.sku").split(",");//{"sku","шк","штрихкод","ean"}; // массив для поиска столбца со штрихкодом
    private String priceHeaders[];//= headerProperties.getProperty("headers.price").split(","); //{"price","цена","стоимость","руб"}; // массив для поиска столбца с ценой
    private String nameHeaders[];// = headerProperties.getProperty("headers.name").split(","); //{"наименование","название","name","product"}; // массив для поиска столбца с наименованием
    private String labelHeaders[];// = headerProperties.getProperty("headers.label").split(","); //{"лоток","plu","лотка"}; // массив для поиска столбца с номером лотка
    private Workbook workbook;
    private Sheet sheet;
    private Integer skuColPos = null;
    private Integer nameColPos = null;
    private Integer priceColPos = null;
    private Integer labelColPos = null;

    public XlsHandlingService(String fileName) throws IOException{
        this.fileName = fileName;
        workbook = WorkbookFactory.create(new File(fileName));
        sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        try {
            InputStream inputProperties = new FileInputStream("/headers.properties");
            Properties headerProperties = new Properties();
            headerProperties.load(inputProperties);
            skuHeaders = headerProperties.getProperty("headers.sku").split(",");//{"sku","шк","штрихкод","ean"}; // массив для поиска столбца со штрихкодом
            System.out.println(skuHeaders);
            priceHeaders = headerProperties.getProperty("headers.price").split(","); //{"price","цена","стоимость","руб"}; // массив для поиска столбца с ценой
            nameHeaders = headerProperties.getProperty("headers.name").split(","); //{"наименование","название","name","product"}; // массив для поиска столбца с наименованием
            labelHeaders = headerProperties.getProperty("headers.label").split(","); //{"лоток","plu","лотка"}; // массив для поиска столбца с номером лотка

        } catch (IOException e){
            log.log(Level.SEVERE, "Ошибка! " + e.getMessage());
        }
    }

    public void processing() throws IOException{
        getHeaders();
        int lastrow = sheet.getLastRowNum();
        for(int i=1; i<=lastrow; i++){
            Row row = sheet.getRow(i);
            if (prepareCellValue(row.getCell(labelColPos))!=""){
                Product product = new Product();
                product.setId(prepareCellValue(row.getCell(labelColPos)));
                product.setSku(prepareCellValue(row.getCell(skuColPos)));
                product.setName(prepareCellValue(row.getCell(nameColPos)));
                product.setPrice(prepareCellValue(row.getCell(priceColPos)).replace(",", "."));
                System.out.println(product.toString());
            }
        }

    }

    public String prepareCellValue(Cell cell){
        String value;
        DataFormatter formatter = new DataFormatter();
        value = formatter.formatCellValue(cell).replace("\"", "");
        return value;
    }

    public boolean isContains(String arr[], Cell cell){
        boolean returnValue = false;
        String value2 = prepareCellValue(cell).toLowerCase();
        for(String arrValue:arr){
            if(value2.contains(arrValue)){
                returnValue =true;
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
                if (isContains(skuHeaders, cell)) {
                    tmpSkuColPos = j;
                    continue;
                }
                if (isContains(nameHeaders, cell)) {
                    tmpNameColPos = j;
                    continue;
                }
                if (isContains(priceHeaders, cell)) {
                    tmpPriceColPos = j;
                    continue;
                }
                if (isContains(labelHeaders, cell)) {
                    tmpLabelColPos = j;
                    continue;
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

}
