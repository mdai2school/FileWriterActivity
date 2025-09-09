import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class MyFileWriter {
    public static void main(String[] args) {
       
        writeHiddenPass(".my_pass.txt", "the safest password:12345");

        writeInHiddenVault(".my_hidden_vault", "my_stuff.txt",
                "pizza, dogs, Matthew");
    }

    static void writeHiddenPass(String filename, String contents) {
        try (BufferedOutputStream writer =
                 new BufferedOutputStream(new FileOutputStream(filename))) {
            writer.write(contents.getBytes());
        } catch (IOException e) {
        }
    }
    
    static void writeInHiddenVault(String hiddenVault, String filename, String contents) {
        File folder = new File(hiddenVault);
        File file = new File(folder, filename);
        try (BufferedOutputStream writer =
            new BufferedOutputStream(new FileOutputStream(file))) {
            writer.write(contents.getBytes());
        } catch (IOException e) {
        }
    }

    public static void printFileSize(String fileName) {
        File f = new File(fileName);
        System.out.println(f.length());
    }
}