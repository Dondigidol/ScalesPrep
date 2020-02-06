package entities;

public class Group {
    private int id;
    private String group = "Группа";
    private String name = "";
    private int type = 2; // группа товаров
    private int price = 0;
    private int parent1 = 0;
    private int parent2 = 0;
    private int parent3 = 0;
    private int parent4 = 0;


    @Override
    public String toString(){
        return "<D " + id +
                ";" + group +
                ";" + name +
                ";" + type +
                ";" + price +
                ";" + parent1 +
                ";" + parent2 +
                ";" + parent3 +
                ";" + parent4 +
                ";";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
