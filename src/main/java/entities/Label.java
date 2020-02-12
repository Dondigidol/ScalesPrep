package entities;

public class Label {
    private int id;
    private String name;
    private String reserved = ";;;;;;;;;;;;;;;;;;";
    private String labelProjectName;

    public Label(int id, String name, String projectName){
        this.id = id;
        this.name = name;
        this.labelProjectName = projectName;
    }

    @Override
    public String toString(){
        return "<L " + id +
                ";" + name +
                ";" + reserved +
                ";" + labelProjectName;
    }

}
