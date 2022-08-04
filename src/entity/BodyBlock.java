package entity;

import java.awt.*;

/**
 * 蛇身体的方块，蛇碰到会死亡
 */
public class BodyBlock extends Block {
    public static final Color color = new Color(70, 175, 70);

    public BodyBlock(int x, int y) {
        super(x, y);
    }

}
