package files;

import core.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

public class FileAnalyserUtil {

    /**
     * Returns a Map<Integer, Boolean> which indicates at which integer a line separator should be printed.
     * The byte map contains "0d0a" or "0a" as line separators. Other line separations are found by checking
     * the dimension of the window.
     * Used for rendering.
     */
    public static ArrayList<ArrayList<Byte>> getContentLines(byte[] byteContents) {
        String log = "";
        String fileContentFormatted = formatBytes(byteContents);
        String usedLineSeparatorFormatted = formatBytes(System.lineSeparator().getBytes());
        int startOfCurrentLine = 0;
        ArrayList<ArrayList<Byte>> linesArrList = new ArrayList<>();

        // Loop over bytes in String form.
        for(int i = 0; i < fileContentFormatted.length()-1;) {
            String cha = fileContentFormatted.substring(i, i+usedLineSeparatorFormatted.length()-1);
            log += "<i=" + i + "> checking string '" + cha + "'\n";
            if(fileContentFormatted.substring(i, i+usedLineSeparatorFormatted.length()).equals(usedLineSeparatorFormatted)){
                log += " -> found line separator for i = " + i + "\n";
                linesArrList.add(toArrayList(fileContentFormatted.substring(startOfCurrentLine, i)));
                i = i+usedLineSeparatorFormatted.length();
                startOfCurrentLine = i;
                log += " -> skipped " + usedLineSeparatorFormatted.length() + " iterations and i is now: " + i + "\n";
            } else {
                i = i+2;
                log += " < nothing found resuming search at i = " + i + "\n";
            }
        }

        System.out.println(log);
        return linesArrList;
    }
    /**
     * Creates the string representation of the byte[].
     * Used for finding line separators: they will be formatted as 0d0a or 0a
     * Note: every byte will be formatted to 2 string characters in ASCII.
     */
    public static String formatBytes(byte[] bytes) {
        Formatter formatter = new Formatter();
        for(byte b : bytes) formatter.format("%02x", b);
        return formatter.toString();
    }

    public static Byte[] wrapEachByteElem(byte[] byteArr){
        Byte[] wrapperArray = new Byte[byteArr.length];
        for (int i = 0; i < byteArr.length; i++) {
            wrapperArray[i] = byteArr[i];
        }
        return wrapperArray;
    }

    private static ArrayList<Byte> toArrayList(String s){
        return new ArrayList<>(Arrays.<Byte>asList(wrapEachByteElem(s.getBytes())));
    }


}
