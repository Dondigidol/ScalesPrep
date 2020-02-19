package services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class CopyService {
    private static final String disk="Z";

    public CopyService(String ip, String username, String password) throws IOException {
        String command = "net use " + disk + ": \\\\" + ip +"\\Shared /user:" + username + " " + password;
        Process p = Runtime.getRuntime().exec(command);
        checkConnection(10);
    }

        private void checkConnection(int checkCount){
            try {
                int i = 1;

                File targetPath = new File(disk);
                while(i<= checkCount){
                    if (targetPath.exists()){
                        break;
                    } else {
                        TimeUnit.SECONDS.sleep(1);
                        i++;
                    }

                }
            } catch (InterruptedException e){
                throw new RuntimeException(e.getMessage(), e);
            }

    }

    public void copyFile(String source, String target) throws IOException{
        target = disk + ":" + target;
        copy(source, target);
    }

    private void copy(String source, String target) throws IOException{
        try {
            Files.copy(Paths.get(source), Paths.get(target), REPLACE_EXISTING);
        } catch (Exception e){
            this.close();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void copyFolder(String source, String target) throws IOException{
        target = disk + ":" + target;
        File[] files = (new File(source)).listFiles();
        for (File file: files){
            if (file.isDirectory()){
                File trgFile = new File (target + "\\" + file.getName());
                if (!trgFile.exists()){
                    trgFile.mkdir();
                }
            }
            if (file.isDirectory()){
                File[] files1 = file.listFiles();
                for (File file1: files1){
                    copy(file1.getPath(), target + "\\" + file.getName() + "\\" + file1.getName());
                }
            } else {
                copy(file.getPath(), target + "\\" + file.getName());
            }
        }
    }

    public void close() throws IOException{
        String command = "net use " + disk + ": /delete";
        Process p = Runtime.getRuntime().exec(command);
    }
}
