package com.kolejnik;

import com.kolejnik.blocks.Block;
import com.kolejnik.blocks.Brick;

import java.io.IOException;

public class Board {
    private int board[][];
    private final int width;
    private final int height;

    public static final int EMPTY_FIELD = 0;
    public static final int SOLID_FIELD = 1;
    public static final int MOVING_FIELD = 2;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[41m";
    private static final String ANSI_GREEN = "\u001B[42m";
    private static final String ANSI_YELLOW = "\u001B[43m";
    private static final String ANSI_BLUE = "\u001B[44m";
    private static final String ANSI_PURPLE = "\u001B[45m";
    private static final String ANSI_CYAN = "\u001B[46m";
    private static final String ANSI_WHITE = "\u001B[47m";
    private static final String ANSI_BLACK = "\u001B[40m";

    public Board(int height, int width) {
        this.width = width;
        this.height = height;
        this.board = new int[height][width];
    }

    public boolean moveBlockLeft(Block block) {
        return moveBlock(block, -1, 0);
    }

    public boolean moveBlockRight(Block block) {
        return moveBlock(block, 1, 0);
    }

    public boolean moveBlockDown(Block block) {
        return moveBlock(block, 0, 1);
    }

    public void moveBlockFullDown(Block block) {
        while (canAddBlock(block, block.getX(), block.getY() + 1)) {
            removeBlock(block);
            block.move(0, 1);
            addBlock(block, MOVING_FIELD);
        }
        print();
    }

    public void checkForFullRows() {
        for (int i = 0; i < board.length; i++) {
            if (isRowFull(i)) {
                moveRowsDown(i);
            }
        }
    }

    private boolean isRowFull(int rowNumber) {
        int[] row = board[rowNumber];
        return isRowFull(row);
    }

    private boolean isRowFull(int[] row) {
        for (int field : row) {
            if (field != SOLID_FIELD) {
                return false;
            }
        }
        return true;
    }

    private void moveRowsDown(int rowNumber) {
        for (int i = rowNumber; i > 0; i--) {
            for (int j = 0; j < width; j++) {
                board[i][j] = board[i - 1][j];
            }
        }
    }

    private boolean moveBlock(Block block, int moveX, int moveY) {
        if (canAddBlock(block, block.getX() + moveX, block.getY() + moveY)) {
            removeBlock(block);
            block.move(moveX, moveY);
            addBlock(block, MOVING_FIELD);
            print();
            return true;
        }
        return false;
    }

    public void rotateBlockRight(Block block) {
        rotateBlock(block, -1);
    }

    public void rotateBlockLeft(Block block) {
        rotateBlock(block, 1);
    }

    private void rotateBlock(Block block, int rotation) {
        removeBlock(block);
        block.rotate(rotation);
        if (!canAddBlock(block)) {
            block.rotate(-rotation);
        }
        addBlock(block, MOVING_FIELD);
        print();
    }

    public boolean canAddBlock(Block block) {
        return canAddBlock(block, block.getX(), block.getY());
    }

    public boolean canAddBlock(Block block, int column, int row) {
        for (Brick brick : block.getBricks()) {
            if (brickOutOfBoard(brick, column, row)) {
                return false;
            }
        }
        return true;
    }

    private boolean brickOutOfBoard(Brick brick, int column, int row) {
        return row + brick.getY() >= height
                || row + brick.getY() < 0
                || column + brick.getX() >= width
                || column + brick.getX() < 0
                || board[row + brick.getY()][column + brick.getX()] == SOLID_FIELD;
    }

    public void addBlock(Block block, int value) {
        for (Brick brick : block.getBricks()) {
            if (block.getY() + brick.getY() < 0) {
                continue;
            }
            board[block.getY() + brick.getY()][block.getX() + brick.getX()] = value;
        }
    }

    public void removeBlock(Block block) {
        addBlock(block, EMPTY_FIELD);
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        System.out.print("\u001B[H\u001b[2J");
        System.out.flush();
        for (int[] line : board) {
            printLine(line, sb);
        }
        for (int i = 0; i < width + 2; i++) {
            printBrick(sb, ANSI_WHITE);
        }
        System.out.println(sb.toString());
    }

    private void printLine(int[] line, StringBuilder sb) {
        String color = ANSI_WHITE;
        if (isRowFull(line)) {
            color = ANSI_YELLOW;
        }
        printBrick(sb, ANSI_WHITE);
        for (int block : line) {
            if (block > EMPTY_FIELD) {
                printBrick(sb, color);
            } else {
                printEmpty(sb);
            }
        }
        printBrick(sb, ANSI_WHITE);
        sb.append("\n");
    }

    private void printBrick(StringBuilder sb, String color) {
        //sb.append("[]");
        sb.append(color + "  " + ANSI_RESET);
    }

    private void printEmpty(StringBuilder sb) {
        sb.append("  ");
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
