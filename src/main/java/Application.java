import services.ConfigurationLoaderService;
import services.XlsHandlingService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Application {
    private static Logger log = Logger.getLogger(Application.class.getName());
    private static Properties importProperties = new Properties();
    private static ConfigurationLoaderService configurationLoaderService;

    public static void main(String[] args){
        try{
            InputStream is = new FileInputStream("./config/logging.properties");
            LogManager.getLogManager().readConfiguration(is);
            configurationLoaderService  = new ConfigurationLoaderService();
        } catch (IOException e){
            log.log(Level.SEVERE, "Проблема с файлами конфигурации! " + e.getMessage(), e);
        }
        log.info("Программа запущена.");
        try {
            if (!Files.exists(Paths.get(configurationLoaderService.getImportFileName()))){
                throw new IOException("Положите файл \"" + configurationLoaderService.getImportFileName() + "\" с актуальными данными в текущую папку и запустите приложение снова!");
            } else {
                System.out.println("Ждите...");
                XlsHandlingService importBook = new XlsHandlingService();
                importBook.proceed();
                importBook.saveToFile();
                System.out.println("Готово!");
            }

        }catch (Exception e){
            log.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }
        log.info("Приложение завершило свою работу.");
    }

}

