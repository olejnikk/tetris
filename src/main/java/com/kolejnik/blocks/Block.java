package com.kolejnik.blocks;

public class Block {
    private Brick[] bricks;
    private int x;
    private int y;

    private static int[][] L_LEFT_BLOCK = new int[][] {{-1, 0}, {0, 0}, {0, 1}, {0, 2}};
    private static int[][] L_RIGHT_BLOCK = new int[][] {{0, 2}, {0, 1}, {0, 0}, {1, 0}};
    private static int[][] LONG_BLOCK = new int[][] {{-1, 0}, {0, 0}, {1, 0}, {2, 0}};
    private static int[][] S_LEFT_BLOCK = new int[][] {{-1, 1}, {0, 1}, {0, 0}, {1, 0}};
    private static int[][] S_RIGHT_BLOCK = new int[][] {{-1, 0}, {0, 0}, {1, 0}, {1, 1}};
    private static int[][] SQUARE_BLOCK = new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    private static int[][] T_BLOCK = new int[][] {{-1, 0}, {0, 0}, {1, 0}, {0, 1}};

    private static int[][][] POSITIONS = new int[][][] {
        L_LEFT_BLOCK, L_RIGHT_BLOCK, LONG_BLOCK, S_LEFT_BLOCK, S_RIGHT_BLOCK, SQUARE_BLOCK, T_BLOCK
    };

    public Block(int[][] brickPositions) {
        this.bricks = new Brick[brickPositions.length];
        int i = 0;
        for (int[] position : brickPositions) {
            Brick brick = new Brick(position[0], position[1]);
            bricks[i++] = brick;
        }
    }

    public static Block randomBlock() {
        int i = (int)(Math.random() * POSITIONS.length);
        Block block = new Block(POSITIONS[i]);
        return block;
    }

    public static Block randomBlock(int x, int y) {
        Block block = randomBlock();
        block.setX(x);
        block.setY(y);
        return block;
    }

    public void move(int moveX, int moveY) {
        x += moveX;
        y += moveY;
    }

    public void rotate(int rotation) {
        for (Brick brick : bricks) {
            int x = brick.getX();
            brick.setX((int)(rotation * (brick.getY() - 0.5) + 0.5));
            brick.setY((int)(-rotation * (x - 0.5) + 0.5));
        }
    }

    public Brick[] getBricks() {
        return bricks;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
