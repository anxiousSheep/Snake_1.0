package util;

import entity.BodyBlock;
import entity.FoodBlock;
import entity.HeadBlock;
import entity.Snack;
import exception.BlockOutOfFrameException;
import exception.IllegalDirectionIDException;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * 工具类，包括重置食物刷新位置，计算像素点，画蛇，etc. <br>
 * 只有你想不到的，没有它做不到的
 */
public class Utils {
    /**
     * 刷新食物的位置，保证不与蛇重叠，否则重新刷新位置
     * @param current 当前的蛇对象
     * @param random 随机数工具
     * @return 新的食物方块
     */
    public static FoodBlock flushFood(Snack current, Random random) {
        FoodBlock newFood = new FoodBlock(random.nextInt(76), random.nextInt(46));

        while (true) {
            if (newFood.getX() == current.getHead().getX() && newFood.getY() == current.getHead().getY()) {
                newFood = new FoodBlock(random.nextInt(76), random.nextInt(46));
                continue;
            }

            for (BodyBlock singleBody :
                    current.getBody()) {
                if (newFood.getX() == singleBody.getX() && newFood.getY() == singleBody.getY()) {
                    newFood = new FoodBlock(random.nextInt(76), random.nextInt(46));
                    continue;
                }
            }

            break;
        }

        return newFood;
    }

    /**
     * 获取像素点的方法 <br>
     * 为了计算方便，将蛇的活动区域抽象为有限区域的坐标轴，每个格点的坐标都是整数 <br>
     * 这个方法会在实际作图的时候将抽象的坐标转换为像素点，对应格点的左上角坐标
     * @param x 格点横坐标
     * @param y 格点纵坐标
     * @return 两个元素的数组，分别为像素横坐标和像素纵坐标
     * @throws BlockOutOfFrameException 当坐标值超出活动区间，抛出异常
     */
    public static int[] getPixel(int x, int y) throws BlockOutOfFrameException {
        if (y > 45 || y < 0) {
            throw new BlockOutOfFrameException(y, "y");
        } else if (x > 75 || x < 0) {
            throw new BlockOutOfFrameException(x, "x");
        }

        return new int[]{27 + x * 10, 125 + y * 10};
    }

    /**
     * 在按钮初始化的时候，设置按钮的样式（不然太丑了我看不下去）
     * @param button 需要更改样式的按钮
     */
    public static void setButtonStyle(JButton button) {
        button.setBackground(new Color(100, 100, 100));
        button.setForeground(new Color(220, 220, 220));
        button.setFont(new Font("Default", Font.BOLD, 20));
    }

    /**
     * 键盘监听器指定了一个蛇对象，所以如果重置的时候蛇对象改变，监听器不起作用 <br>
     * 这个方法可以在原蛇对象中更改蛇的数据
     * @param old 原本的蛇对象
     * @param headX 新的蛇头横坐标
     * @param headY 新的蛇头纵坐标
     */
    public static void flushSnake(Snack old, int headX, int headY) {
        // 随机数工具
        Random getTowards = new Random();

        old.setHead(new HeadBlock(headX, headY));
        old.setBody(new LinkedList<>());

        int headToNum = getTowards.nextInt(4);
        old.setHeadTo(switch (headToNum) {
            case 0 -> Snack.Towards.LEFT;
            case 1 -> Snack.Towards.UP;
            case 2 -> Snack.Towards.RIGHT;
            case 3 -> Snack.Towards.DOWN;
            default -> {
                try {
                    throw new IllegalDirectionIDException(headToNum);
                } catch (IllegalDirectionIDException e) {
                    e.printStackTrace();
                }

                yield null;
            }
        });

        switch (old.getHeadTo()) {
            case LEFT -> {
                old.getBody().offer(new BodyBlock(old.getHead().getX() + 2, old.getHead().getY()));
                old.getBody().offer(new BodyBlock(old.getHead().getX() + 1, old.getHead().getY()));
            }

            case UP -> {
                old.getBody().offer(new BodyBlock(old.getHead().getX(), old.getHead().getY() + 2));
                old.getBody().offer(new BodyBlock(old.getHead().getX(), old.getHead().getY() + 1));
            }

            case RIGHT -> {
                old.getBody().offer(new BodyBlock(old.getHead().getX() - 2, old.getHead().getY()));
                old.getBody().offer(new BodyBlock(old.getHead().getX() - 1, old.getHead().getY()));
            }

            case DOWN -> {
                old.getBody().offer(new BodyBlock(old.getHead().getX(), old.getHead().getY() - 2));
                old.getBody().offer(new BodyBlock(old.getHead().getX(), old.getHead().getY() - 1));
            }

        }

        old.setUseless(new BodyBlock(0, 0));
        old.setAdded(old.getBody().peek());
        old.setLength(3);
    }
}
