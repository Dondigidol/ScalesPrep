package services;

import entities.Group;
import entities.Product;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XlsHandlingServiceImpl implements XlsHandlingService{
    private static Logger log = Logger.getLogger(XlsHandlingServiceImpl.class.getName());
    private String fileName; // название обрабатываемого файла
    private ConfigurationLoaderService configurationLoaderService;
    private Writer exportFileWriter;
    List<Group> groupIds;

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

    public XlsHandlingServiceImpl() throws IOException{
        configurationLoaderService = new ConfigurationLoaderService();
        workbook = WorkbookFactory.create(new File(configurationLoaderService.getImportFileName()));
        sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());



        skuHeaders = configurationLoaderService.getSkuHeaders();
        priceHeaders = configurationLoaderService.getPriceHeaders();
        nameHeaders = configurationLoaderService.getNameHeaders();
        labelHeaders = configurationLoaderService.getLabelHeaders();

        exportFileWriter = new OutputStreamWriter(new FileOutputStream(configurationLoaderService.getExportFileName()), "cp1251");


    }

    public void proceed() throws IOException{
        headerHandling();
        groupHandling();
        productHandling();
    }

    public void headerHandling() throws IOException {
        exportFileWriter.append("##@@&&");
        exportFileWriter.append("\n");
        exportFileWriter.append("#");
        exportFileWriter.append("\n");
        exportFileWriter.flush();
    }

    public void groupHandling() throws IOException{
        getHeaders();
        int lastrow = sheet.getLastRowNum();
        int max = 0;
        for (int i=1; i<lastrow; i++){
            Row row = sheet.getRow(i);
            int curCellValue = Integer.valueOf(prepareCellValue(row.getCell(labelColPos)));
            if(max < curCellValue){
                max = curCellValue;
            }
        }
        System.out.println(max);

        int groupCount = max / configurationLoaderService.getGroupBy();
        System.out.println(groupCount);


        groupIds = new ArrayList<>();

        for (int i = 0; i <= groupCount; i++){
            Group group = new Group();
            group.setId(999999 - i);
            int curGroupStartPos = configurationLoaderService.getGroupBy()*i+1;
            int curGroupEndPos = curGroupStartPos + configurationLoaderService.getGroupBy() - 1;
            group.setName(curGroupStartPos + " - " + curGroupEndPos);
            groupIds.add(group);

            exportFileWriter.append(group.toString());
            exportFileWriter.append("\n");
            exportFileWriter.flush();
        }



    }

    public void productHandling() throws IOException{

        int lastrow = sheet.getLastRowNum();


        for(int i=1; i<=lastrow; i++){
            Row row = sheet.getRow(i);
            if (!prepareCellValue(row.getCell(labelColPos)).equals("")){
                Product product = new Product();
                product.setId(prepareCellValue(row.getCell(labelColPos)));
                product.setSku(prepareCellValue(row.getCell(skuColPos)));
                product.setName(prepareCellValue(row.getCell(nameColPos)));
                product.setPrice(prepareCellValue(row.getCell(priceColPos)).replace(",", "."));

                int groupNum = Integer.valueOf(product.getId())/configurationLoaderService.getGroupBy();
                product.setParent1(groupIds.get(groupNum).getId());


                exportFileWriter.append(product.toString());
                exportFileWriter.append("\n");
            }
        }
        exportFileWriter.flush();

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

}
