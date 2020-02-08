package entities;

public class ScalesConfiguration {
    private int id;
    private String value;

    public ScalesConfiguration(int id, String value){
        this.id=id;
        this.value=value;
    }

    public ScalesConfiguration(){

    }

    @Override
    public String toString(){
        return "<N " + this.id +
                ";" + this.value;
    }
}
