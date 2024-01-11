package fr.ensimag.deca.tools;

import java.io.PrintStream;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class IndentPrintStream {
    private final PrintStream stream;
    private int indent = 0;
    private boolean indented = false;
    public IndentPrintStream(PrintStream stream) {
        this.stream = stream;
    }

    private void printIndent() {
        if (indented) {
            return;
        }
        for (int i = 0; i < indent; i++) {
            stream.print("\t");
        }
        indented = true;
    }

    public void print(String s) {
        printIndent();
        stream.print(s);
    }

    public void print(String s, String color) {
        printIndent();
        stream.print(color(s, color));
    }

    public void println() {
        stream.println();
        indented = false;
    }

    public void println(String s) {
        print(s);
        println();
    }

    public void println(String s, String color) {
        print(s, color);
        println();
    }

    public void indent() {
        indent++;
    }

    public void unindent() {
        indent--;
    }

    public void print(char charAt) {
        printIndent();
        stream.print(charAt);
    }

    private String color(String s, String color) {
        switch (color) {
            case "red":
                return "\033[31m" + s + "\033[0m";
            case "green":
                return "\033[32m" + s + "\033[0m";
            case "orange":
                return "\033[33m" + s + "\033[0m";
            case "blue":
                return "\033[34m" + s + "\033[0m";
            case "purple":
                return "\033[35m" + s + "\033[0m";
            case "cyan":
                return "\033[36m" + s + "\033[0m";
            case "white":
                return "\033[37m" + s + "\033[0m";
            case "gray":
                return "\033[90m" + s + "\033[0m";
            default:
                return s;
        }
    }
}
