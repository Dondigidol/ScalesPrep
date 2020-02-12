package entities;

public class User {
    private int id;
    private String name;
    private int role; // 0-administrator, 1 - operator
    private String password;
    private String reserved = ";;;;;;;;;;;;;;;;;;";
    private int printZeroCode = 0;
    private int priceChange = 0;
    private int containerChange = 0;
    private int changeProductType = 0;
    private int printZeroPrice = 0;
    private int printNmPV = 0;
    private int autoPrint = 1;
    private int printLabelCopy = 0;
    private int editProductWeight = 0;

    public User(int id, int role, String name, String password){
        this.id = id;
        this.role = role;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString(){
        return "<U " + id +
                ";" + name +
                ";" + role +
                ";" + password +
                ";" + reserved +
                ";" + printZeroCode +
                ";" + priceChange +
                ";" + containerChange +
                ";" + changeProductType +
                ";" + printZeroPrice +
                ";" + printNmPV +
                ";" + autoPrint +
                ";" + printLabelCopy +
                ";" + editProductWeight;
    }

}
