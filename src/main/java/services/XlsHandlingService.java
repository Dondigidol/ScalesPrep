package services;

import entities.*;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class XlsHandlingService {
    private static Logger log = Logger.getLogger(XlsHandlingService.class.getName());
    private String fileName;
    private ConfigurationLoaderService configurationLoaderService;
    private Writer exportFileWriter;
    private List<Group> groupList =new ArrayList<>();
    private List<ScalesConfiguration> configurationList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<Label> labelList = new ArrayList<>();
    private List<Resource> resourceList = new ArrayList<>();
    private String[] lmHeaders;
    private String[] skuHeaders;
    private String[] priceHeaders;
    private String[] nameHeaders;
    private String[] labelHeaders;
    private Workbook workbook;
    private Sheet sheet;
    private int lmColPos;
    private Integer skuColPos;
    private Integer nameColPos;
    private Integer priceColPos;
    private Integer labelColPos;
    private String flagFileName = "PCScale.flz";
    private int groupCount;

    public XlsHandlingService() throws IOException{
        configurationLoaderService = new ConfigurationLoaderService();
        workbook = WorkbookFactory.create(new File(configurationLoaderService.getImportFileName()));
        log.info("Подключение к файлу " + configurationLoaderService.getImportFileName() + " выполнено.");
        sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());

        lmHeaders = configurationLoaderService.getLmHeaders();
        skuHeaders = configurationLoaderService.getSkuHeaders();
        priceHeaders = configurationLoaderService.getPriceHeaders();
        nameHeaders = configurationLoaderService.getNameHeaders();
        labelHeaders = configurationLoaderService.getLabelHeaders();

        exportFileWriter = new OutputStreamWriter(new FileOutputStream(configurationLoaderService.getExportFileName()), "cp1251");


    }

    public void proceed() throws IOException{
        configurationHandling();
        labelHandling();
        userHandling();
        groupHandling();
        productHandling();

        ScenarioGenerator scenarioGenerator = new ScenarioGenerator(productList, groupCount);
        scenarioGenerator.generate();
        scenarioGenerator.saveToFile();
        log.info("Сгенерирован файл сценария " + configurationLoaderService.getScenarioFileName() + ".");
    }

    private void labelHandling(){
        labelList.add(new Label(1, configurationLoaderService.getLabelName(), "Labels\\"+configurationLoaderService.getLabelProjectName()));
    }

    private void userHandling(){
        userList.add(new User(1, 1, configurationLoaderService.getUserName(), configurationLoaderService.getUserPassword()));
        userList.add(new User(2, 0, configurationLoaderService.getAdminName(), configurationLoaderService.getAdminPassword()));
    }

    private void configurationHandling(){
        configurationList.add(new ScalesConfiguration(952, configurationLoaderService.getScenarioFileName())); // указываем в файле экспорта имя файла сценария
        configurationList.add(new ScalesConfiguration(11, "1"));
        configurationList.add(new ScalesConfiguration(213, "1"));
        configurationList.add(new ScalesConfiguration(27, "1"));
        configurationList.add(new ScalesConfiguration(210,"1"));
        configurationList.add(new ScalesConfiguration(213, "1"));
        configurationList.add(new ScalesConfiguration(202, configurationLoaderService.getShopName()));
        configurationList.add(new ScalesConfiguration(7, "1"));
        configurationList.add(new ScalesConfiguration(8,"1"));
        configurationList.add(new ScalesConfiguration(40,"0"));
        configurationList.add(new ScalesConfiguration(402,"1"));
        configurationList.add(new ScalesConfiguration(814, "1"));
        configurationList.add(new ScalesConfiguration(815,"0"));
        configurationList.add(new ScalesConfiguration(816,"1"));
        configurationList.add(new ScalesConfiguration(817, "1"));
        configurationList.add(new ScalesConfiguration(818, "1"));
        configurationList.add(new ScalesConfiguration(803, "Import\\" + this.flagFileName));
        if (configurationLoaderService.isAutoimport()){
            configurationList.add(new ScalesConfiguration(954, "Import\\" + this.flagFileName));
        }

    }

    private void groupHandling() throws IOException{

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

        this.groupCount = (int)Math.ceil((double)max / configurationLoaderService.getGroupBy());
    }

    private void productHandling(){

        int lastrow = sheet.getLastRowNum();

        for(int i=1; i<=lastrow; i++){
            Row row = sheet.getRow(i);
            if (!prepareCellValue(row.getCell(labelColPos)).equals("")){
                Product product = new Product();
                product.setId(Integer.valueOf(prepareCellValue(row.getCell(labelColPos))));
                product.setSku(prepareCellValue(row.getCell(skuColPos)));
                product.setName(prepareCellValue(row.getCell(nameColPos)));
                product.setPrice(prepareCellValue(row.getCell(priceColPos)).replace(",", "."));
                product.setLm(prepareCellValue(row.getCell(lmColPos)));
                Resource resource = new Resource(product.getId(), 1, "ШК: " + product.getSku() + " Артикул: " +product.getLm());
                resourceList.add(resource);

                product.setMessageCode(product.getId());

//                int groupNum = Integer.valueOf(product.getId())/configurationLoaderService.getGroupBy();
//                product.setParent1(2);

                productList.add(product);
            }
        }
    }

    private String prepareCellValue(Cell cell){
        String value;
        DataFormatter formatter = new DataFormatter();
        value = formatter.formatCellValue(cell).replace("\"", "");
        return value;
    }

    private boolean isCellContains(String[] arr, Cell cell){
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

    private void getHeaders() throws IOException{
        Row row = sheet.getRow(0);
        int lastcol = row.getLastCellNum();
        int tmpSkuColPos = -1;
        int tmpNameColPos = -1;
        int tmpPriceColPos = -1;
        int tmpLabelColPos = -1;
        int tmpLmColPos = -1;

        for (int j=0; j<=lastcol; j++){
            Cell cell = row.getCell(j);
            if(cell != null){
                if (isCellContains(lmHeaders, cell)){
                    tmpLmColPos = j;
                }
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
            this.lmColPos = tmpLmColPos;
            this.skuColPos = tmpSkuColPos;
            this.nameColPos = tmpNameColPos;
            this.priceColPos = tmpPriceColPos;
            this.labelColPos = tmpLabelColPos;
            log.info("Информация о заголовках в файле импорта (xlsx) обнаружена, заголовки сопоставлены.");
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
        log.info("Внесены конфигурационные данные в файл импорта.");

        for (User user: userList){
            exportFileWriter.append(user.toString());
            exportFileWriter.append("\n");
        }
        exportFileWriter.flush();
        log.info("Внесена информация о пользователях в файл импорта.");

/*        for(Group group: groupList){
            exportFileWriter.append(group.toString());
            exportFileWriter.append("\n");
        }
        exportFileWriter.flush();
        log.info("");*/

        int productCount=0;
        for (Product product: productList){
            exportFileWriter.append(product.toString());
            exportFileWriter.append("\n");
            productCount++;
        }
        exportFileWriter.flush();
        log.info("Внесены данные о товаре. Внесено " + productCount + " артикулов.");


        for(Label label: labelList){
            exportFileWriter.append(label.toString());
            exportFileWriter.append("\n");
        }
        exportFileWriter.flush();
        log.info("Внесена информация об этикетках.");


        for (Resource resource: resourceList){
            exportFileWriter.append(resource.toString());
            exportFileWriter.append("\n");
        }
        exportFileWriter.flush();
        log.info("Внесена информация о ресурсах.");


        exportFileWriter.append("$$$CLR");
        exportFileWriter.flush();
        log.info("Перед загрузкой БД будет очищена!");
        exportFileWriter.close();

        if(configurationLoaderService.isAutoimport()){
            Writer writer = new OutputStreamWriter(new FileOutputStream(this.flagFileName));
            writer.flush();
            writer.close();
            Path sourcePath = Paths.get(this.flagFileName);

            for (String ip: configurationLoaderService.getIp()){
                ip = ip.replace(" ", "");
                Path targetPath = Paths.get("\\\\" + ip + "\\Shared\\Import\\" + this.flagFileName);
                Files.copy(sourcePath, targetPath, REPLACE_EXISTING);
                log.log(Level.FINE,"Файл автоимпорта (" + this.flagFileName + ") скопирован на (" + ip +") успешно!");
            }
            Files.delete(sourcePath);
        }

        String sourceImportPath = configurationLoaderService.getExportFileName();
        String sourceLabelPath = "Labels\\";

        File[] files = (new File("Labels\\")).listFiles();
        for (String ip: configurationLoaderService.getIp()){
            String remoteImportPath = "\\\\" + ip + "\\Shared\\Import\\";
            String remoteLabelPath = "\\\\" + ip + "\\Shared\\Labels\\";

            ip = ip.replace(" ", "");

            Path targetImportPath = Paths.get(remoteImportPath + configurationLoaderService.getExportFileName());
            Files.copy(Paths.get(sourceImportPath), targetImportPath, REPLACE_EXISTING);
            Files.delete(Paths.get(sourceImportPath));
            log.info("Файл импорта успешно скопирован в весы.");
            for (File file: files){
                deleteFiles(new File(remoteLabelPath+file.getName()));

                Path targetLabelPath = Paths.get(remoteLabelPath + file.getName());
                Files.copy(Paths.get(sourceLabelPath + file.getName()), targetLabelPath, REPLACE_EXISTING);

                if (file.isDirectory()){
                    File[] dirFiles = file.listFiles();
                    for (File dirFile: dirFiles){
                        Path dirSourcePath = Paths.get(sourceLabelPath + "\\"+ file.getName()+"\\" + dirFile.getName());
                        Path dirTargetPath = Paths.get(targetLabelPath + "\\" + dirFile.getName());
                        Files.copy(dirSourcePath, dirTargetPath, REPLACE_EXISTING);
                    }
                }
            }
            log.info("Этикеткы успешно скопированы в весы.");
        }


    }

    private void deleteFiles(File file){
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File dirFile: files){
                dirFile.delete();
            }
        }
        file.delete();
    }

}
