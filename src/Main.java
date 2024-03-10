import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Formatter;

public class Main {
    public static void main(String[] args) throws IOException {

        File file = new File("res/test.txt");
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

        System.out.println("Delimeter: " + identifyLineDelimiter(formattedContent));


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