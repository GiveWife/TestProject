package core;

import files.FileBuffer;
import io.github.btj.termios.Terminal;

import java.io.IOException;
import java.nio.charset.Charset;

public class Controller {

    /**
     * Size of the terminal
     */
    private int width, height;
    private FileBuffer buffer;

    public Controller(String[] args) {
        this.buffer = new FileBuffer("res/test.txt", System.lineSeparator());
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

        this.buffer.render(1, 1, width, height);

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
                    enterSeparator();
                    break;
                // Character input
                default:
                    enterText((char) c);
                    break;

            }

            // Flush stdIn & Recalculate dimensions
            System.in.skipNBytes(System.in.available());
            retrieveDimensions();
            this.buffer.render(1, 1, width, height);

        }

    }

    /**
     * Renders the layout with the terminal current height & width
     */
    public void render() {
        // TODO root layout has to render its children on itself.
        //this.rootLayout.render(this.width, this.height);
    }

    /**
     * Saves the FileBuffer's content to its file.
     */
    public void saveBuffer() {

    }

    /**
     * Moves insertion point in a file buffer
     */
    public void moveCursor(char code) {
        this.buffer.moveInsertionPoint(height, width, code);
    }

    public void enterSeparator() {
        buffer.enterInsertionPoint();
    }

    public void enterText(char str) {
        if(Charset.forName("ASCII").newEncoder().canEncode(str)) {
            this.buffer.write(String.valueOf(str));
        }
    }

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

        this.width = width;
        this.height = height;

    }

    String getLineSeparator(String[] args) {
        if(args[0].equals("--lf"))
            return "0a";
        else if(args[0].equals("--crlf"))
            return "0d0a";
        else return null;
    }

    /**
     * Temporary helper
     */
    private void print(String... s) {
        for(String si : s) {
            System.out.println(si);
        }
    }

    private static String getPrint(String[] s) {
        String out = "";
        for(String l : s) out += l + ", ";
        return out;
    }

}