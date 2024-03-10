import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Formatter;

public class Main {
    public static void main(String[] args) throws IOException {

        File file = new File("res/test.txt");

        /*
        System.out.println("Before:");
        System.out.println("format: 68656c6c6f686466");
        byte[] bytes = Files.readAllBytes(file.toPath());

        try (Formatter formatter = new Formatter()) {
            for (byte b : System.lineSeparator().getBytes())
                formatter.format("%02x", b);
            System.out.println("Line separator: " + formatter.toString());
        }
        String formattedContent = "";
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) formatter.format("%02x", b);
            formattedContent = formatter.toString();
            System.out.println("format: " + formatter.toString());
        }

        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        System.out.println("Delimeter: " + identifyLineDelimiter(formattedContent));*/

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s = "";
            String sub = "";
            while((sub = reader.readLine()) != null) {
                s += sub;
            }

            Formatter formatter = new Formatter();
            for(byte b : fileContent) formatter.format("%02x", b);
            System.out.println("Read out bytes:: " + formatter.toString());

            Formatter formatter2 = new Formatter();
            byte[] stringToBytes = s.getBytes();
            for(byte b : stringToBytes) formatter2.format("%02x", b);
            System.out.println("String to bytes: " + formatter2.toString());


        } catch(IOException e) {

        }

        String alpha = " !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        for(int i = 0; i < alpha.length(); i++) {
            Formatter f = new Formatter();
            byte[] bytesForChar = alpha.substring(i, i+1).getBytes();
            for(byte b : bytesForChar) f.format("%02x", b);
            String byteForm = f.toString();
            System.out.println("Letter: '" + alpha.substring(i, i + 1) + "': " + byteForm);
        }



    }

    public static String identifyLineDelimiter(String str) {
        if (str.matches("(?s).*(\\r\\n).*")) {     //Windows //$NON-NLS-1$
            return "\r\n"; //$NON-NLS-1$
        } else if (str.matches("(?s).*(\\n).*")) { //Unix/Linux //$NON-NLS-1$
            return "\n"; //$NON-NLS-1$
        } else if (str.matches("(?s).*(\\r).*")) { //Legacy mac os 9. Newer OS X use \n //$NON-NLS-1$
            return "\r"; //$NON-NLS-1$
        } else {
            return "\n";  //fallback onto '\n' if nothing matches. //$NON-NLS-1$
        }
    }

}