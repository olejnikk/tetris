package com.kolejnik;

import com.kolejnik.blocks.Block;
import com.kolejnik.blocks.Brick;

public class Board {
    private int fields[][];
    private final int width;
    private final int height;
    private BoardPrinter printer;

    public static final int EMPTY_FIELD = 0;
    public static final int SOLID_FIELD = 1;
    public static final int MOVING_FIELD = 2;

    public Board(int height, int width) {
        this.width = width;
        this.height = height;
        this.fields = new int[height][width];
        this.printer = new BoardPrinter(this);
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
        printer.print();
    }

    public void checkForFullRows() {
        for (int i = 0; i < fields.length; i++) {
            if (isRowFull(i)) {
                moveRowsDown(i);
            }
        }
    }

    public void print() {
        printer.print();
    }

    private boolean isRowFull(int rowNumber) {
        int[] row = fields[rowNumber];
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
                fields[i][j] = fields[i - 1][j];
            }
        }
    }

    private boolean moveBlock(Block block, int moveX, int moveY) {
        if (canAddBlock(block, block.getX() + moveX, block.getY() + moveY)) {
            removeBlock(block);
            block.move(moveX, moveY);
            addBlock(block, MOVING_FIELD);
            printer.print();
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

    public void addBlock(Block block, int value) {
        for (Brick brick : block.getBricks()) {
            if (block.getY() + brick.getY() < 0) {
                continue;
            }
            fields[block.getY() + brick.getY()][block.getX() + brick.getX()] = value;
        }
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

    public void removeBlock(Block block) {
        addBlock(block, EMPTY_FIELD);
    }

    private void rotateBlock(Block block, int rotation) {
        removeBlock(block);
        block.rotate(rotation);
        if (!canAddBlock(block)) {
            block.rotate(-rotation);
        }
        addBlock(block, MOVING_FIELD);
        printer.print();
    }

    private boolean brickOutOfBoard(Brick brick, int column, int row) {
        return row + brick.getY() >= height
                || row + brick.getY() < 0
                || column + brick.getX() >= width
                || column + brick.getX() < 0
                || fields[row + brick.getY()][column + brick.getX()] == SOLID_FIELD;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getFields() {
        return fields;
    }
}
