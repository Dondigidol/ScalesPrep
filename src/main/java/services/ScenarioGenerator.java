package services;

import entities.scenario.Button;
import entities.scenario.Window;

import java.util.Properties;

public class ScenarioGenerator {
    Properties properties = new Properties();

    public ScenarioGenerator(){

    }

    public void createButton(){
        Button button = new Button();

    }
    public void createWindow(){
        Window window = new Window(1);
    }





}
