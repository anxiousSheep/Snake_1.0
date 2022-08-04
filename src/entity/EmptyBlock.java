package entity;

import java.awt.*;

/**
 * 空白方块，不会对蛇的移动和食物的生成产生影响
 */
public class EmptyBlock extends Block {
    public static final Color color = new Color(70, 70, 70);

    public EmptyBlock(int x, int y) {
        super(x, y);
    }
}
