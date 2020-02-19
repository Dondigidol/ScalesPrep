package services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigurationLoaderService {

    private String importFileName;
    private String exportFileName;
    private Properties properties;
    private String[] lmHeaders;
    private String[] skuHeaders;
    private String[] priceHeaders;
    private String[] nameHeaders;
    private String[] labelHeaders;
    private int groupBy;
    private int buttonsXCount;
    private int buttonsYCount;
    private String scenarioFileName;
    private int buttonMargin;
    private int numberFontSize;
    private int textFontSize;
    private int headerFontSize;
    private String[] ip;
    private boolean autoimport;
    private String adminName;
    private String adminPassword;
    private String userName;
    private String userPassword;
    private String labelName;
    private String labelProjectName;
    private String shopName;
    private String scalesUsername;
    private String scalesPassword;


    public ConfigurationLoaderService() throws IOException{
        InputStream is = new FileInputStream("./config/configuration.properties");
        InputStreamReader inputProperties = new InputStreamReader(is, "cp1251");

        properties = new Properties();
        properties.load(inputProperties);

        this.importFileName=properties.getProperty("import.file.name");
        this.exportFileName = properties.getProperty("export.file.name");
        this.lmHeaders = properties.getProperty("import.file.headers.lm").split(",");
        this.labelHeaders = properties.getProperty("import.file.headers.label").split(",");
        this.nameHeaders = properties.getProperty("import.file.headers.name").split(",");
        this.priceHeaders = properties.getProperty("import.file.headers.price").split(",");
        this.skuHeaders = properties.getProperty("import.file.headers.sku").split(",");
        this.groupBy = Integer.valueOf(properties.getProperty("scenario.screen.matrix.horizontal")) *
                Integer.valueOf(properties.getProperty("scenario.screen.matrix.vertical"));
        this.buttonsXCount = Integer.valueOf(properties.getProperty("scenario.screen.matrix.horizontal"));
        this.buttonsYCount = Integer.valueOf(properties.getProperty("scenario.screen.matrix.vertical"));
        this.scenarioFileName = properties.getProperty("scenario.file.name");
        this.buttonMargin = Integer.valueOf(properties.getProperty("scenario.screen.button.margin"));
        this.numberFontSize = Integer.valueOf(properties.getProperty("scenario.screen.number.font.size"));
        this.textFontSize = Integer.valueOf(properties.getProperty("scenario.screen.text.font.size"));
        this.headerFontSize = Integer.valueOf(properties.getProperty("scenario.screen.header.font.size"));
        this.ip = properties.getProperty("scales.configuration.ip").split(",");
        this.autoimport = Boolean.valueOf(properties.getProperty("scales.configuration.autoimport"));
        this.adminName = properties.getProperty("scales.configuration.admin");
        this.adminPassword = properties.getProperty("scales.configuration.admin.password");
        this.userName = properties.getProperty("scales.configuration.user");
        this.userPassword = properties.getProperty("scales.configuration.user.password");
        this.labelName = properties.getProperty("scales.label.name");
        this.labelProjectName = properties.getProperty("scales.label.project.name");
        this.shopName = properties.getProperty("scales.configuration.shop.name");
        this.scalesUsername = properties.getProperty("scales.configuration.username");
        this.scalesPassword = properties.getProperty("scales.configuration.password");
    }

    public String getImportFileName() {
        return importFileName;
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public String[] getLmHeaders(){
        return this.lmHeaders;
    }

    public String[] getSkuHeaders() {
        return skuHeaders;
    }

    public String[] getPriceHeaders() {
        return priceHeaders;
    }

    public String[] getNameHeaders() {
        return nameHeaders;
    }

    public String[] getLabelHeaders() {
        return labelHeaders;
    }

    public int getGroupBy() {
        return groupBy;
    }

    public int getButtonsXCount() {
        return buttonsXCount;
    }

    public int getButtonsYCount() {
        return buttonsYCount;
    }

    public String getScenarioFileName() {
        return scenarioFileName;
    }

    public int getButtonMargin() {
        return buttonMargin;
    }

    public int getNumberFontSize() {
        return numberFontSize;
    }

    public int getTextFontSize() {
        return textFontSize;
    }

    public int getHeaderFontSize() {
        return headerFontSize;
    }

    public String[] getIp() {
        return ip;
    }

    public boolean isAutoimport() {
        return autoimport;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getLabelName() {
        return labelName;
    }

    public String getLabelProjectName() {
        return labelProjectName;
    }

    public String getShopName() {
        return shopName;
    }

    public String getScalesUsername() {
        return scalesUsername;
    }

    public String getScalesPassword() {
        return scalesPassword;
    }
}
