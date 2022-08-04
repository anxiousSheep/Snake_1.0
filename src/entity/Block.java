package entity;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 所有方块的父类，包括贪吃蛇体，空白方块，食物方块 <br>
 * 这里的x和y指的是格点的坐标，而不是像素的坐标
 */
public abstract class Block {
    public static Color color;
    private int x;
    private int y;

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

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return x == block.x && y == block.y;
    }

}
