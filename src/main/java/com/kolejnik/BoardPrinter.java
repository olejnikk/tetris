package com.kolejnik;

import java.io.PrintStream;

public class BoardPrinter {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[43m";
    private static final String ANSI_WHITE = "\u001B[47m";
    private static final String ANSI_BLACK = "\u001B[40m";
    private static final String RETURN_AND_CLEAN = "\u001B[H\u001b[2J";
    private static final String BLOCK = "  ";

    private Board board;
    private PrintStream stream = System.out;

    public BoardPrinter(Board board) {
        this.board = board;
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        for (int[] line : board.getFields()) {
            printLine(line, sb);
        }
        for (int i = 0; i < board.getWidth() + 2; i++) {
            printBrick(sb, ANSI_WHITE);
        }
        cleanConsole();
        stream.print(sb.toString());
    }

    private void cleanConsole() {
        stream.print(RETURN_AND_CLEAN);
        stream.flush();
    }

    private void printLine(int[] line, StringBuilder sb) {
        printBrick(sb, ANSI_WHITE);
        for (int field : line) {
            printBrick(sb, fieldColor(field));
        }
        printBrick(sb, ANSI_WHITE);
        sb.append("\n");
    }

    private String fieldColor(int fieldType) {
        switch (fieldType) {
            case Board.SOLID_FIELD:
                return ANSI_WHITE;
            case Board.MOVING_FIELD:
                return ANSI_YELLOW;
            default:
                return ANSI_BLACK;
        }
    }

    private void printBrick(StringBuilder sb, String color) {
        //For non ASCI escape code consoles:
        //sb.append("[]");
        sb.append(color + BLOCK + ANSI_RESET);
    }

    private void printEmpty(StringBuilder sb) {
        sb.append(BLOCK);
    }
}
