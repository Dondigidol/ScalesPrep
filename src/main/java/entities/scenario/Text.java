package entities.scenario;

public class Text {
    private int id;
    private  int parent;
    private int x; //положение относительно окна
    private int y; //положение относительно окна
    private int width;
    private int height;
    private int actionId = 0;
    private int actionValue = 0;
    private int resourceId = 2;
    private String resourceValue = "";
    private String value = "";
    private int textColor = 0;
    private int backgroundColor = 0;
    private int alignX = 0;
    private int alignY = 0;
    private static final int RESERVED = 0;
    private int fontId;

    public Text(int id, int parent, String value, int fontId){
        this.id = id;
        this.parent = parent;
        this.value = value;
        this.fontId = fontId;
    }

    public void setSize(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setColor(int textColor, int backgroundColor){
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public void setAlignment(int x, int y){
        this.alignX = x;
        this.alignY = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return "<T " + id +
                ";" + parent +
                ";" + x +
                ";" + y +
                ";" + width +
                ";" + height +
                ";" + actionId +
                ";" + actionValue +
                ";" + resourceId +
                ";" + resourceValue +
                ";" + value +
                ";" + textColor +
                ";" + backgroundColor +
                ";" + alignX +
                ";" + alignY +
                ";" + RESERVED +
                ";" + fontId;

    }

}
