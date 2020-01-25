package entities;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;

public class XlsImportBook {
    private String fileName;
    private final String skuHeaders[] = {"sku","шк","штрихкод","ean"};
    private final String priceHeaders[] = {"price","цена","стоимость","руб"};
    private final String nameHeaders[] = {"наименование","название","name","product"};
    private final String labelHeaders[] = {"лоток","plu","лотка"};
    private Workbook workbook;
    private Sheet sheet;
    private Integer skuColPos = null;
    private Integer nameColPos = null;
    private Integer priceColPos = null;
    private Integer labelColPos = null;

    public XlsImportBook(String fileName) throws IOException{
        this.fileName = fileName;
        workbook = WorkbookFactory.create(new File(fileName));
        sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
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
