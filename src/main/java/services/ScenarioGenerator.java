package services;

import entities.Product;
import entities.scenario.Button;
import entities.scenario.Font;
import entities.scenario.Text;
import entities.scenario.Window;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ScenarioGenerator {
    private List<Product> productList;
    private List<Button> buttonList =new ArrayList<>();
    private List<Window> windowList = new ArrayList<>();
    private List<Text> textList = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();
    private ConfigurationLoaderService configurationLoaderService;
    private Writer scenarioWriter;
    private int groupBy;
    private int groupCount;
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 584;
    private static final int MENU_HEIGHT = 100;
    private static final int FIELDS_WIDTH = 100;
    private int buttonXCount;
    private int buttonYCount;
    private int buttonHeight;
    private int buttonWidth;
    private int buttonMargin;

    public ScenarioGenerator(List<Product> productList, int groupCount) throws IOException {
        this.productList = productList;
        this.groupCount = groupCount;

        configurationLoaderService = new ConfigurationLoaderService();
        groupBy = configurationLoaderService.getGroupBy();
        buttonYCount = configurationLoaderService.getButtonsYCount();
        buttonXCount = configurationLoaderService.getButtonsXCount();
        buttonMargin = configurationLoaderService.getButtonMargin();

        this.buttonWidth = (WINDOW_WIDTH-FIELDS_WIDTH-buttonXCount * buttonMargin)/buttonXCount;
        this.buttonHeight = (WINDOW_HEIGHT-MENU_HEIGHT-buttonYCount * buttonMargin)/buttonYCount;

        scenarioWriter = new OutputStreamWriter(new FileOutputStream(configurationLoaderService.getScenarioFileName()), "cp1251");
    }


    public void generate(){
        createWindows();

        for (Product product: productList){
            createButton(product);
        }
    }

    public void saveToFile() throws IOException{
        Font font = new Font(999999, 12);
        Font font1 = new Font(999998, 36);
        font1.setStyle(2);

        scenarioWriter.append(font.toString());
        scenarioWriter.append("\n");
        scenarioWriter.append(font1.toString());
        scenarioWriter.append("\n");
        scenarioWriter.flush();


        for(Window window: windowList){
            scenarioWriter.append(window.toString());
            scenarioWriter.append("\n");
        }
        scenarioWriter.flush();

        for(Button button: buttonList){
            scenarioWriter.append(button.toString());
            scenarioWriter.append("\n");
        }
        scenarioWriter.flush();

        for(Text text: textList){
            scenarioWriter.append(text.toString());
            scenarioWriter.append("\n");
        }
        scenarioWriter.flush();


    }

    private void createWindows(){
        for(int i = 1; i<=groupCount; i++){
            Window window = new Window(i);
            windowList.add(window);
            idList.add(window.getId());
        }
    }

    private int createButton(Product product){
        int buttonId = idList.get(idList.size() - 1) + 1;
        int parent =  (product.getId() - 1)/groupBy;

        int positionOnWindow = product.getId() - (product.getId()-1)/groupBy * groupBy;
        int positionX = (positionOnWindow -1)%buttonXCount + 1;
        int positionY = (positionOnWindow -1)/buttonXCount + 1;


        Button button = new Button();
        button.setId(buttonId);
        button.setParent(windowList.get(parent).getId());
        int x = FIELDS_WIDTH/2 + (positionX - 1) * buttonWidth + buttonMargin*positionX;
        int y = MENU_HEIGHT + (positionY - 1) * buttonHeight + buttonMargin*positionY;
        button.setSize(x, y, buttonWidth, buttonHeight);
        button.setActionId(1);
        button.setActionValue(product.getId());

        buttonList.add(button);
        idList.add(button.getId());

        createText(button, product);

        return buttonId;
    }

    private int createText(Button button, Product product){
        int textId = idList.get(idList.size() - 1) + 1;

        Text text = new Text(textId, button.getId(), String.valueOf(product.getId()), 999998);
        text.setSize(button.getX(), button.getY(), button.getWidth(), button.getHeight());
        textList.add(text);

        idList.add(text.getId());

        return 0;
    }







}
