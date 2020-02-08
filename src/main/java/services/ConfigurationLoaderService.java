package services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigurationLoaderService {

    private String importFileName;
    private String exportFileName;
    private Properties properties;
    private String[] skuHeaders;
    private String[] priceHeaders;
    private String[] nameHeaders;
    private String[] labelHeaders;
    private int groupBy;
    private int buttonsXCount;
    private int buttonsYCount;
    private String scenarioFileName;
    private int buttonMargin;


    public ConfigurationLoaderService() throws IOException{
        InputStream isProperties = getClass().getResourceAsStream("/configuration.properties");
        InputStreamReader inputProperties = new InputStreamReader(isProperties, "cp1251");

        properties = new Properties();
        properties.load(inputProperties);

        setImportFileName(properties.getProperty("import.file.name"));
        setExportFileName(properties.getProperty("export.file.name"));
        setLabelHeaders(properties.getProperty("import.file.headers.label").split(","));
        setNameHeaders(properties.getProperty("import.file.headers.name").split(","));
        setPriceHeaders(properties.getProperty("import.file.headers.price").split(","));
        setSkuHeaders(properties.getProperty("import.file.headers.sku").split(","));
        setGroupBy(Integer.valueOf(properties.getProperty("scenario.screen.matrix.horizontal")) *
                Integer.valueOf(properties.getProperty("scenario.screen.matrix.vertical")));
        setButtonsXCount(Integer.valueOf(properties.getProperty("scenario.screen.matrix.horizontal")));
        setButtonsYCount(Integer.valueOf(properties.getProperty("scenario.screen.matrix.vertical")));
        setScenarioFileName(properties.getProperty("scenario.file.name"));
        setButtonMargin(Integer.valueOf(properties.getProperty("scenario.screen.button.margin")));
    }

    public String getImportFileName() {
        return importFileName;
    }

    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName;
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public String[] getSkuHeaders() {
        return skuHeaders;
    }

    public void setSkuHeaders(String[] skuHeaders) {
        this.skuHeaders = skuHeaders;
    }

    public String[] getPriceHeaders() {
        return priceHeaders;
    }

    public void setPriceHeaders(String[] priceHeaders) {
        this.priceHeaders = priceHeaders;
    }

    public String[] getNameHeaders() {
        return nameHeaders;
    }

    public void setNameHeaders(String[] nameHeaders) {
        this.nameHeaders = nameHeaders;
    }

    public String[] getLabelHeaders() {
        return labelHeaders;
    }

    public void setLabelHeaders(String[] labelHeaders) {
        this.labelHeaders = labelHeaders;
    }

    public int getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(int groupBy) {
        this.groupBy = groupBy;
    }

    public int getButtonsXCount() {
        return buttonsXCount;
    }

    public void setButtonsXCount(int buttonsXCount) {
        this.buttonsXCount = buttonsXCount;
    }

    public int getButtonsYCount() {
        return buttonsYCount;
    }

    public void setButtonsYCount(int buttonsYCount) {
        this.buttonsYCount = buttonsYCount;
    }

    public String getScenarioFileName() {
        return scenarioFileName;
    }

    public void setScenarioFileName(String scenarioFileName) {
        this.scenarioFileName = scenarioFileName;
    }

    public int getButtonMargin() {
        return buttonMargin;
    }

    public void setButtonMargin(int buttonMargin) {
        this.buttonMargin = buttonMargin;
    }
}
