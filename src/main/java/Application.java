import services.ConfigurationLoaderService;
import services.XlsHandlingService;
import services.XlsHandlingServiceImpl;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Application {
    private static Logger log = Logger.getLogger(Application.class.getName());
    private static Properties importProperties = new Properties();
    private static ConfigurationLoaderService configurationLoaderService;

    public static void main(String[] args) throws IOException{
        try{
            LogManager.getLogManager().readConfiguration(Application.class.getResourceAsStream("/logging.properties"));
            configurationLoaderService  = new ConfigurationLoaderService();
        } catch (IOException e){
            log.log(Level.SEVERE, "Проблема с файлами конфигурации! " + e.getStackTrace());
        }


        log.info("Запуск приложения...");
        try {
            boolean toContinue = true;
            while (toContinue) {
                System.out.println("Положите файл \"" + configurationLoaderService.getImportFileName() + "\" с актуальными данными в текущую папку и нажмите клавишу \"Enter\"");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                XlsHandlingService importBook = new XlsHandlingServiceImpl();
                importBook.proceed();
                toContinue = false;
            }
        }catch (Exception e){
            //log.log(Level.SEVERE, "Ошибка: "+e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
        log.info("application stopped");
    }

}

