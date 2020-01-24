import entities.XlsImportBook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Application {



    public static void main(String[] args) throws IOException{
        System.out.println("Запуск приложения...");
        boolean isExist = false;
        do {
            System.out.println("Положите файл \"data.xlsx\" с актуальными данными в текущую папку и нажмите клавишу \"Enter\"");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            XlsImportBook importBook = new XlsImportBook("data.xlsx");
            importBook.processing();

        } while (!isExist);


    }

}

