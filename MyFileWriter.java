import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MyFileWriter {

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

    private static void printTotalFileSize(String... fileNames) {
        long totalSize = 0;
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (file.exists()) {
                totalSize += file.length();
            }
        }
        System.out.println("Total size of all files: " + totalSize + " bytes");
    }

    private static void toString(String fileName) {
        File f = new File(fileName);
        System.out.println(f.toString());
    }

    // help from geeksforgeeks.com:https://www.geeksforgeeks.org/java/sha-256-hash-in-java
    private static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public static String hashFile(String path) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(path));
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return toHexString(md.digest(data));
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + path);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 not available");
        } catch (java.io.IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
        return null;
    }



    public static void main(String[] args) {
       
        writeHiddenPass(".my_pass.txt", "the safest password:12345");

        writeInHiddenVault(".my_hidden_vault", "my_stuff.txt",
                "pizza, dogs, Matthew");

        // printFileSize("test.txt");
        // printFileSize("test1.txt");
    }
}