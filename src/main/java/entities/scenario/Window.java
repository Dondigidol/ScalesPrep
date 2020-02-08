package entities.scenario;

public class Window {
    private int id;
    private int parent;
    private static final String WINDOW = "0;0;1024;584";
    private int actionId = 0;
    private int actionValue=0;
    private int resourceId=0;
    private int resourceValue=0;

    public Window(int id, int parent, int actionId, int actionValue, int resourceId, int resourceValue){
        this.id = id;
        this.parent = parent;
        this.actionId = actionId;
        this.actionValue = actionValue;
        this.resourceId = resourceId;
        this.actionValue =resourceValue;
    }

    public Window(int id){
        this.id=id;
    }

    @Override
    public String toString(){
        return "<W " + this.id +
                ";" + this.parent +
                ";"+ WINDOW +
                ";" + this.actionId +
                ";" + this.actionValue +
                ";" + this.resourceId +
                ";" + this.resourceValue +
                ";";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
