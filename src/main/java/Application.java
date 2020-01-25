import entities.XlsImportBook;
import org.apache.commons.math3.analysis.function.Log;

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
        boolean isExist = false;
        do {
            System.out.println("Положите файл \"data.xlsx\" с актуальными данными в текущую папку и нажмите клавишу \"Enter\"");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            XlsImportBook importBook = new XlsImportBook("data.xlsx");
            importBook.processing();
            isExist = true;
        } while (!isExist);
        log.info("application stopped");
    }

}

