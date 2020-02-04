import services.XlsHandlingService;
import services.XlsHandlingServiceImpl;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Application {
    private static Logger log = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws IOException{
        try{
            LogManager.getLogManager().readConfiguration(Application.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e){
            System.err.println("Could not setup logger configuration: " + e.toString());
        }


        System.out.println("Запуск приложения...");
        boolean toContinue=true;
        while(toContinue) {
            System.out.println("Положите файл \"data.xlsx\" с актуальными данными в текущую папку и нажмите клавишу \"Enter\"");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            XlsHandlingService importBook = new XlsHandlingServiceImpl("data.xlsx");
            importBook.processing();
            toContinue = false;
        }
        log.info("application stopped");
    }

}

