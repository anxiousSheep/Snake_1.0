package entity;

import java.awt.*;

/**
 * 墙体方块，蛇碰到会死亡
 */
public class WallBlock extends Block {
    public static final Color color = new Color(70, 110, 255);

    public WallBlock(int x, int y) {
        super(x, y);
    }
}
