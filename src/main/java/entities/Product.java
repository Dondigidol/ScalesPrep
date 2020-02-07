package entities;

import java.util.Date;

public class Product {
    private String id; //1
    private String sku; //2
    private String name; //3
    private int type = 0; //4
    private String price; //5
    private int parent1 = 0; //6
    private int parent2 = 0; //7
    private int parent3 = 0; //8
    private int parent4 = 0; //9
    private final String reserved1=""; //10
    private final String reserved2=""; //11
    private int shelfLife; //12
    private String realisationDate=""; //13
    private int labelCode = 0; //14
    private int isBlocked = 0; //15
    private int priceChangingFlag = 1; //16
    private String reserved3=""; //17
    private String reserved4=""; //18
    private String reserved5=""; //19
    private String reserved6=""; //20
    private String reserved7=""; //21
    private String certificate = ""; //22
    private float tareWeight1 = 0; //23
    private float tareWeight2 = 0; //24
    private float tareWeight3 = 0; //25
    private int tareNameCode1; //26
    private int tareNameCode2; //27
    private int tareNameCode3; //28
    private int shortNameCode; //29
    private int messageCode; //30
    private int pictureCode; //31
    private int messageFileCode; //32
    private int videoCode; //33
    private int audioCode; //34


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getShortNameCode() {
        return shortNameCode;
    }

    public void setShortNameCode(int shortNameCode) {
        this.shortNameCode = shortNameCode;
    }

    public int getPictureCode() {
        return pictureCode;
    }

    public void setPictureCode(int pictureCode) {
        this.pictureCode = pictureCode;
    }

    public int getParent1() {
        return parent1;
    }

    public void setParent1(int parent1) {
        this.parent1 = parent1;
    }

    public int getParent2() {
        return parent2;
    }

    public void setParent2(int parent2) {
        this.parent2 = parent2;
    }

    @Override
    public String toString() {
        return "<D " +
                id +
                ";" + sku +
                ";" + name +
                ";" + type +
                ";" + price +
                ";" + parent1 +
                ";" + parent2 +
                ";" + parent3 +
                ";" + parent4 +
                ";" + reserved1 +
                ";" + reserved2 +
                ";" + shelfLife +
                ";" + realisationDate +
                ";" + labelCode +
                ";" + isBlocked +
                ";" + priceChangingFlag +
                ";" + reserved3 +
                ";" + reserved4 +
                ";" + reserved5 +
                ";" + reserved6 +
                ";" + reserved7 +
                ";" + certificate +
                ";" + tareWeight1 +
                ";" + tareWeight2 +
                ";" + tareWeight3 +
                ";" + tareNameCode1 +
                ";" + tareNameCode2 +
                ";" + tareNameCode3 +
                ";" + shortNameCode +
                ";" + messageCode +
                ";" + pictureCode +
                ";" + messageFileCode +
                ";" + videoCode +
                ";" + audioCode +
                ";";
    }
}
