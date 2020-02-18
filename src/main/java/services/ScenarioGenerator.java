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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ScenarioGenerator {
    private static Logger log = Logger.getLogger(ScenarioGenerator.class.getName());
    private List<Product> productList;
    private List<Button> buttonList =new ArrayList<>();
    private List<Window> windowList = new ArrayList<>();
    private List<Text> textList = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();
    private List<Font> fontList = new ArrayList<>();
    private ConfigurationLoaderService configurationLoaderService;
    private Writer scenarioWriter;
    private int groupBy;
    private int groupCount;
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 584;
    private static final int MENU_HEIGHT = 120;
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


    public void generate() throws IOException{
        createWindows();
        createFonts();
        createHeaders();

        for (Product product: productList){
            createButton(product);
        }
    }

    public void saveToFile() throws IOException{

        for(Font font: fontList){
            scenarioWriter.append(font.toString());
            scenarioWriter.append("\n");
        }
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
        scenarioWriter.close();


    }

    private void createWindows(){
        for(int i = 1; i<=groupCount; i++){
            Window window = new Window(i);
            windowList.add(window);
            idList.add(window.getId());
        }
    }

    private int createButton(Product product){
        int buttonId = idList.get(idList.size() - 1).intValue() + 1;
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

    private void createFonts(){
        Font font = new Font(999999, configurationLoaderService.getTextFontSize());
        Font font1 = new Font(999998, configurationLoaderService.getNumberFontSize());
        Font font2 = new Font(999997, configurationLoaderService.getHeaderFontSize());
        font2.setStyle(2);
        fontList.add(font);
        fontList.add(font1);
        fontList.add(font2);

    }

    private void createHeaders(){
        int buttonWidth = WINDOW_WIDTH/groupCount - buttonMargin;
        int buttonDelta = 30;
        int buttonHeight;

        for (int i = 1; i<=groupCount; i++ ){
            int parentId = windowList.get(i - 1).getId();
            for (int j = 1; j<=groupCount; j++){
                int buttonId = idList.get(idList.size() - 1) + 1;
                Button button = new Button();
                button.setId(buttonId);
                button.setParent(parentId);
                button.setActionId(2);
                button.setActionValue(j);
                int y;
                if (i == j) {
                    buttonHeight = MENU_HEIGHT-buttonDelta/2;
                    y = buttonDelta/4;
                } else {
                    buttonHeight = MENU_HEIGHT - buttonDelta;
                    y = buttonDelta/2;
                }

                int x = (j - 1)*(buttonMargin + buttonWidth) + buttonMargin + buttonMargin/groupCount;


                button.setSize(x, y, buttonWidth, buttonHeight);

                buttonList.add(button);
                idList.add(button.getId());

                int textId = idList.get(idList.size() - 1) + 1;

                int curGroupStartPos = configurationLoaderService.getGroupBy()*(j - 1) + 1;
                int curGroupEndPos = curGroupStartPos + configurationLoaderService.getGroupBy() - 1;
                String label = curGroupStartPos + " - " + curGroupEndPos;

                Text text = new Text(textId, button.getId(), label, 999997);
                text.setSize(button.getX(), button.getY(), button.getWidth(), button.getHeight());

                textList.add(text);
                idList.add(text.getId());
            }
        }
    }







}
