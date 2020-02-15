package entities;

public class Resource {
    private int id;
    private int type; // 1-text, 2-text file, 3-image, 4-video, 5-audio
    private String reserved = ";;;;;;;;;;;;;;;;;;";
    private String value; // text or text file

    public Resource(int id, int type, String value){
        this.id = id;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString(){
        return "<R " + id +
                ";" + type +
                ";" + reserved +
                ";" + value;
    }

    public int getId() {
        return id;
    }
}
