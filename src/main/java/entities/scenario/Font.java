package entities.scenario;

public class Font {
    private int id;
    private int size;
    private int style=0; // 1 - italic, 2 - bold
    private String name = "Arial";

    public Font(int id, int size){
        this.id = id;
        this.size = size;
    }

    @Override
    public String toString(){
        return "<F " + id +
                ";" + size +
                ";" + style +
                ";" + name;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
