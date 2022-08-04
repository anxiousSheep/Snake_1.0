package util;

import entity.Snack;
import show.Main;
import show.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 键盘监听器，指定了蛇的行为
 */
public class Listener implements KeyListener {
    Snack snack;

    public Listener(Snack snack) {
        this.snack = snack;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Window.isRunning()) {
            if (KeyEvent.VK_E == e.getKeyCode()) {
                snack.setDejaVu(true);
                snack.setSpeed((short) 40);
            } else if (KeyEvent.VK_A == e.getKeyCode()) {
                switch (snack.getHeadTo()) {
                    case UP, DOWN -> {
                        if (!(snack.getAdded().getX() == snack.getHead().getX() - 1)) {
                            snack.setHeadTo(Snack.Towards.LEFT);
                        }
                    }
                }
            } else if (KeyEvent.VK_D == e.getKeyCode()) {
                switch (snack.getHeadTo()) {
                    case UP, DOWN -> {
                        if (!(snack.getAdded().getX() == snack.getHead().getX() + 1)) {
                            snack.setHeadTo(Snack.Towards.RIGHT);
                        }
                    }
                }
            } else if (KeyEvent.VK_W == e.getKeyCode()) {
                switch (snack.getHeadTo()) {
                    case LEFT, RIGHT -> {
                        if (!(snack.getAdded().getY() == snack.getHead().getY() - 1)) {
                            snack.setHeadTo(Snack.Towards.UP);
                        }
                    }
                }
            } else if (KeyEvent.VK_S == e.getKeyCode()) {
                switch (snack.getHeadTo()) {
                    case LEFT, RIGHT -> {
                        if (!(snack.getAdded().getY() == snack.getHead().getY() + 1)) {
                            snack.setHeadTo(Snack.Towards.DOWN);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (KeyEvent.VK_E == e.getKeyCode()) {
            snack.setDejaVu(false);
            snack.setSpeed((short) 500);
        }
    }
}
