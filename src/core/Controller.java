package core;

import files.FileBuffer;
import files.FileHolder;
import io.github.btj.termios.Terminal;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Controller {

    private static String lineSeparator;

    /**
     * Root layout
     */
    private FileBuffer buffer;

    /**
     * Size of the terminal
     */
    private int width, height;

    public Controller(String[] args) {
        this.buffer = new FileBuffer("res/test.txt");
    }

    /**
     * A beautiful start for a beautiful project
     */
    public static void main(String[] args) throws IOException, RuntimeException {
        // If no arguments given
        if(args.length == 0 ||
                (
                        // Or --lf || --crlf is given
                        (args[0].equals("--lf") || (args[0].equals("--crlf")))
                                // But amount of args is 1
                                && args.length == 1
                )
        ) { // Then no path is specified, and we cannot open
            throw new RuntimeException("TextR needs at least one specified file");
        }

        Controller btj = new Controller(args);
        btj.loop();
    }

    /**
     * Contains the main input loop
     */
    public void loop() throws IOException {

        // Start program.
        Terminal.clearScreen();
        // Terminal moet in rawInput staan voor dimensies te kunnen lezen!
        Terminal.enterRawInputMode();

        // Reading terminal dimensions for correct rendering
        retrieveDimensions();

        // Main loop
        for ( ; ; ) {

            int c = Terminal.readByte();

            switch(c) {
                // Control + S
                case 19:
                    saveBuffer();
                    break;
                // Control + P
                case 16:
                    break;
                // Control + N
                case 14:
                    break;
                // Arrow keys
                case 27:
                    Terminal.readByte();
                    moveCursor((char) Terminal.readByte());
                    break;
                // Line separator
                case 13:
                    enterLineSeparator();
                    break;
                // Character input
                default:
                    enterText((char) c);
                    //render();
                    break;

            }

            // Flush stdIn & Recalculate dimensions
            System.in.skipNBytes(System.in.available());
            retrieveDimensions();

            //this.buffer.render(1,1, 40, 20);

        }

    }

    /**
     * Renders the layout with the terminal current height & width
     */
    void render() {

    }

    /**
     * Saves the FileBuffer's content to its file.
     */
    void saveBuffer() {
    }

    /**
     * Moves insertion point in a file buffer
     */
    void moveCursor(char code) {
    }

    /**
     * Handles inputted text and redirects them to the active .
     */
    void enterText(char str) {
        // Silently ignore non-ASCII characters.
        if(Charset.forName("ASCII").newEncoder().canEncode(str)) {
        }
    }

    /**
     * Line separator is non-ASCII, so cannot enter through {@link Controller#enterText(char)}
     */
    void enterLineSeparator() {
    }

    // Test functions


    /**
     * <p>Calculates the dimensions of the terminal
     * Credits to BTJ. This looks very clean and intuïtive.
     * Sets the fields {@link Controller#width} and {@link Controller#height}.</p>
     * <p>Method set to default for unit test access.</p>
     */
    void retrieveDimensions() throws IOException {

        Terminal.reportTextAreaSize();
        int tempByte = 0;
        for(int i = 0; i < 4; i++)
            Terminal.readByte();

        int c = Terminal.readByte();
        int height = c - '0';
        tempByte = Terminal.readByte();

        for(;;) {
            if(tempByte < '0' || '9' < tempByte)
                break;
            if (height > (Integer.MAX_VALUE - (c - '0')) / 10)
                break;
            height = height * 10 + (tempByte - '0');
            tempByte = Terminal.readByte();

        }

        c = Terminal.readByte();
        int width = c - '0';
        tempByte = Terminal.readByte();

        for(;;) {
            if(tempByte < '0' || '9' < tempByte)
                break;
            if (width > (Integer.MAX_VALUE - (c - '0')) / 10)
                break;
            width = width * 10 + (tempByte - '0');
            tempByte = Terminal.readByte();
        }

    }

    public static String setLineSeparator(String[] args) {
        if(args[0].equals("--lf"))
            return "0a";
        else if(args[0].equals("--crlf"))
            return "0d0a";
        else return null;
    }

    public static String getLineSeparator(){
        return Controller.lineSeparator;
    }
}