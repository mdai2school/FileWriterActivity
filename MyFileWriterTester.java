import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class MyFileWriterTester {

    public static void main(String[] args) throws Exception {
        int pass = 0;
        int fail = 0;

        if (testEmpty()){
            pass++;
        } else{
            fail++;
        }

        if (testSpecialChars()){
            pass++;
        } else{
            fail++;
        }

        if (testLarge()){
            pass++;
        } else{
            fail++;
        }

        if (testNonExistent()){
            pass++;
        } else{
            fail++;
        }

        System.out.println();
        System.out.println("Passed: " + pass + ", Failed: " + fail);
    }
    
// Tests hashing of a 0-byte file.
// Expect: hash equals known SHA-256 of empty input (e3b0...b855).
// Result: PASS on my run.
    static boolean testEmpty() throws IOException {
        Path path = Files.createTempFile("empty_", ".txt");
        String got = MyFileWriter.hashFile(path.toString());
        String expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        boolean ok = expected.equals(got);
        if (ok) {
            System.out.println("Empty file: PASS");
        } else {
            System.out.println("Empty file: FAIL");
            System.out.println("  expected: " + expected);
            System.out.println("  got     : " + got);
        }
        Files.deleteIfExists(path);
        return ok;
    }

// Tests UTF-8 handling (special: "cafe é").
// Expect: file hash matches SHA-256 of the same bytes.
// Result: PASS on my run.
    static boolean testSpecialChars() throws Exception {
        Path p = Files.createTempFile("unicode_", ".txt");
        String s = "cafe é";
        Files.write(p, s.getBytes(StandardCharsets.UTF_8));
        String got = MyFileWriter.hashFile(p.toString());
        String expected = sha256Hex(s.getBytes(StandardCharsets.UTF_8));
        boolean ok = expected.equals(got);
        if (ok) {
            System.out.println("Special chars: PASS");
        } else {
            System.out.println("Special chars: FAIL");
            System.out.println("  expected: " + expected);
            System.out.println("  got     : " + got);
        }
        Files.deleteIfExists(p);
        return ok;
    }

// Tests hashing a ~large file to check performance/memory.
// Expect: file hash equals SHA-256 computed in memory (same data).
// Result: PASS on my run.
    static boolean testLarge() throws Exception {
        Path p = Files.createTempFile("large_", ".bin");
        int size = 5 * 1024 * 1024;
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = (byte) (i % 251);
        }
        Files.write(p, data);
        String got = MyFileWriter.hashFile(p.toString());
        String expected = sha256Hex(data);
        boolean ok = expected.equals(got);
        if (ok) {
            System.out.println("Large file: PASS");
        } else {
            System.out.println("Large file: FAIL");
            System.out.println("  expected: " + expected);
            System.out.println("  got     : " + got);
        }
        Files.deleteIfExists(p);
        return ok;
    }

// Tests behavior for a missing file.
// Expect: hashFile returns null and prints a clear error.
// Result: PASS on my run.
    static boolean testNonExistent() {
        String got = MyFileWriter.hashFile("__does_not_exist__.txt");
        boolean ok = (got == null);
        if (ok) {
            System.out.println("Non-existent file: PASS");
        } else {
            System.out.println("Non-existent file: FAIL");
            System.out.println("  expected: null");
            System.out.println("  got     : " + got);
        }
        return ok;
    }

    static String sha256Hex(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(bytes);
        BigInteger n = new BigInteger(1, d);
        StringBuilder hex = new StringBuilder(n.toString(16));
        while (hex.length() < 64) {
            hex.insert(0, '0');
        }
        return hex.toString();
    }
}
