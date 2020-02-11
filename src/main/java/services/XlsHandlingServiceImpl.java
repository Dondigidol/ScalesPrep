package services;

import entities.Group;
import entities.Product;
import entities.ScalesConfiguration;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class XlsHandlingServiceImpl implements XlsHandlingService{
    private static Logger log = Logger.getLogger(XlsHandlingServiceImpl.class.getName());
    private String fileName; // название обрабатываемого файла
    private ConfigurationLoaderService configurationLoaderService;
    private Writer exportFileWriter;
    private List<Group> groupList =new ArrayList<>();
    private List<ScalesConfiguration> configurationList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();


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
        configurationHandling();
        groupHandling();
        productHandling();

        ScenarioGenerator scenarioGenerator = new ScenarioGenerator(productList, groupList.size());
        scenarioGenerator.generate();
        scenarioGenerator.saveToFile();

    }

    private void configurationHandling(){
        configurationList.add(new ScalesConfiguration(952, configurationLoaderService.getScenarioFileName())); // указываем в файле экспорта имя файла сценария
        configurationList.add(new ScalesConfiguration(11, "1"));
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

        int groupCount = (int)Math.ceil((double)max / configurationLoaderService.getGroupBy());

        for (int i = 0; i < groupCount; i++){
            Group group = new Group();
            group.setId(999999 - i);
            int curGroupStartPos = configurationLoaderService.getGroupBy()*i+1;
            int curGroupEndPos = curGroupStartPos + configurationLoaderService.getGroupBy() - 1;
            group.setName(curGroupStartPos + " - " + curGroupEndPos);
            configurationList.add(new ScalesConfiguration(450+i, group.getName()));
            groupList.add(group);
        }
    }

    public void productHandling(){

        int lastrow = sheet.getLastRowNum();

        for(int i=1; i<=lastrow; i++){
            Row row = sheet.getRow(i);
            if (!prepareCellValue(row.getCell(labelColPos)).equals("")){
                Product product = new Product();
                product.setId(Integer.valueOf(prepareCellValue(row.getCell(labelColPos))));
                product.setSku(prepareCellValue(row.getCell(skuColPos)));
                product.setName(prepareCellValue(row.getCell(nameColPos)));
                product.setPrice(prepareCellValue(row.getCell(priceColPos)).replace(",", "."));

                int groupNum = Integer.valueOf(product.getId())/configurationLoaderService.getGroupBy();
                product.setParent1(2);

                productList.add(product);
            }
        }
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

    public void saveToFile() throws IOException{
        exportFileWriter.append("##@@&&");
        exportFileWriter.append("\n");
        exportFileWriter.append("#");
        exportFileWriter.append("\n");
        exportFileWriter.flush();

        for (ScalesConfiguration configuration: configurationList){
            exportFileWriter.append(configuration.toString());
            exportFileWriter.append("\n");
        }
        exportFileWriter.flush();

        for(Group group: groupList){
            exportFileWriter.append(group.toString());
            exportFileWriter.append("\n");
        }
        exportFileWriter.flush();

        for (Product product: productList){
            exportFileWriter.append(product.toString());
            exportFileWriter.append("\n");
        }
        exportFileWriter.flush();

        exportFileWriter.close();

        String exportPath = "\\\\" + configurationLoaderService.getIp() + "\\Shared\\Import\\";

        if(configurationLoaderService.isAutoimport()){
            String flagFileName = "PCScale.flz";
            Writer writer = new OutputStreamWriter(new FileOutputStream(flagFileName));
            writer.flush();
            writer.close();
            Path sourcePath = Path.of(flagFileName);
            Path targetPath = Path.of(exportPath + flagFileName);
            Files.move(sourcePath, targetPath, REPLACE_EXISTING);
            log.log(Level.FINE,"Auto-import file (" + flagFileName + ") is copied to the scales successfully!");
        }
        Path sourcePath = Path.of(configurationLoaderService.getExportFileName());
        Path targetPath = Path.of(exportPath + configurationLoaderService.getExportFileName());
        Files.move(sourcePath, targetPath, REPLACE_EXISTING);
        log.log(Level.FINE, "Import file (" + configurationLoaderService.getExportFileName() + ") is copied to the scales successfully!");
    }

}
