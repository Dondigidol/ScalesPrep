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
    private boolean productGroup;
    private int groupBy = 36;


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
        setProductGroup(Boolean.valueOf(properties.getProperty("scales.product.group")));
        setGroupBy(Integer.valueOf(properties.getProperty("scales.product.groupBy")));
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

    public boolean getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(boolean productGroup) {
        this.productGroup = productGroup;
    }

    public int getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(int groupBy) {
        this.groupBy = groupBy;
    }
}
