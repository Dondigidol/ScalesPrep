package entities.scenario;

public class Button {
    private int id;
    private int parent;
    private int x;
    private int y;
    private int width;
    private int height;
    private int actionId;
    private int actionValue;
    private int resourceId;
    private int resourceValue;

    public Button(int id, int parent, int actionId, int actionValue, int resourceId, int resourceValue){
        this.id = id;
        this.parent = parent;
        this.actionId = actionId;
        this.actionValue = actionValue;
        this.resourceId = resourceId;
        this.resourceValue = resourceValue;
    }

    public Button(){

    }

    public void setSize(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString(){
        return "<B " + id +
                ";" + parent +
                ";" + x +
                ";" + y +
                ";" + width +
                ";" + height +
                ";" + actionId +
                ";" + actionValue +
                ";" + resourceId +
                ";" + resourceValue +
                ";";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}
