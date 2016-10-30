package com.kolejnik;

import com.kolejnik.blocks.Block;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tetris {

    private Board board;
    private Block activeBlock;
    private int stepMilis = 500;
    private Timer timer = new Timer();

    public Tetris(Board board) throws IOException {
        initKeyListener();
        this.board = board;
        nextActiveBlock();
        board.print();
        timer.schedule(new GameStep(), 0, stepMilis);
    }

    private void nextActiveBlock() {
        activeBlock = Block.randomBlock(board.getWidth() / 2, 0);
    }

    private void endOfGame() {
        timer.cancel();
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
        }
        System.exit(0);
    }

    class GameStep extends TimerTask {
        public void run() {
            if (board.canAddBlock(activeBlock, activeBlock.getX(), activeBlock.getY() + 1)) {
                board.moveBlockDown(activeBlock);
            } else {
                if (activeBlock.getY() == 0) {
                    endOfGame();
                }
                board.addBlock(activeBlock, Board.SOLID_FIELD);
                board.checkForFullRows();
                nextActiveBlock();
            }
            board.addBlock(activeBlock, Board.MOVING_FIELD);
            board.print();
        }
    }

    public class GlobalKeyListenerExample implements NativeKeyListener {
        @Override
        public void nativeKeyPressed(NativeKeyEvent e) {
            if (e.getKeyCode() == NativeKeyEvent.VC_LEFT || e.getKeyCode() == NativeKeyEvent.VC_J) {
                board.moveBlockLeft(activeBlock);
            }
            if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT || e.getKeyCode() == NativeKeyEvent.VC_L) {
                board.moveBlockRight(activeBlock);
            }
            if (e.getKeyCode() == NativeKeyEvent.VC_A || e.getKeyCode() == NativeKeyEvent.VC_UP
                    || e.getKeyCode() == NativeKeyEvent.VC_K) {
                board.rotateBlockLeft(activeBlock);
            }
            if (e.getKeyCode() == NativeKeyEvent.VC_D || e.getKeyCode() == NativeKeyEvent.VC_DOWN) {
                board.rotateBlockRight(activeBlock);
            }
            if (e.getKeyCode() == NativeKeyEvent.VC_SPACE || e.getKeyCode() == NativeKeyEvent.VC_S) {
                board.moveBlockFullDown(activeBlock);
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent e) {
        }
    }

    private void initKeyListener() {
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExample());
    }

    public static void main(String[] args) throws IOException {
        Board board = new Board(25, 15);
        Tetris tetris = new Tetris(board);
    }
}
