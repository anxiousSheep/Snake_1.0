package entity;

import exception.IllegalDirectionIDException;
import show.Window;
import util.Utils;

import java.util.LinkedList;
import java.util.Random;

/**
 * 蛇类，包括头部方块和身体方块 <br>
 * 类中包含一个记录了蛇运动方向的枚举类型
 */
public class Snack {
    public short getSpeed() {
        return speed;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    /**
     * 蛇的朝向（蛇不能反向行驶，同向可以加速）
     */
    public enum Towards {
        LEFT, UP, RIGHT, DOWN
    }

    private short speed = 500;

    public HeadBlock getHead() {
        return head;
    }

    public LinkedList<BodyBlock> getBody() {
        return body;
    }

    public Towards getHeadTo() {
        return headTo;
    }

    // 蛇头
    private HeadBlock head;

    // 蛇身
    private LinkedList<BodyBlock> body = new LinkedList<>();

    // 蛇长
    private int length;

    public void setDejaVu(boolean dejaVu) {
        this.dejaVu = dejaVu;
    }

    // 正在加速？
    private boolean dejaVu = false;

    public BodyBlock getUseless() {
        return useless;
    }

    // 上一次操作中废弃的蛇身
    private BodyBlock useless;

    public BodyBlock getAdded() {
        return added;
    }

    // 本次操作中新增的蛇身
    private BodyBlock added;

    public void setHeadTo(Towards headTo) {
        this.headTo = headTo;
    }

    private Towards headTo;

    public void setHead(HeadBlock head) {
        this.head = head;
    }

    public void setBody(LinkedList<BodyBlock> body) {
        this.body = body;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setUseless(BodyBlock useless) {
        this.useless = useless;
    }

    public void setAdded(BodyBlock added) {
        this.added = added;
    }



    /**
     * 生成蛇并指定头部的位置 <br>
     * 这个方法同时还会随机指定一个方向，根据这个方向生成长度为2的蛇身
     * @param x 蛇头的横坐标
     * @param y 蛇头的纵坐标
     */
    public Snack(int x, int y) {
        this.head = new HeadBlock(x, y);

        // 随机数工具
        Random getTowards = new Random();
        int headToNum = getTowards.nextInt(4);
        this.headTo = switch (headToNum) {
            case 0 -> Towards.LEFT;
            case 1 -> Towards.UP;
            case 2 -> Towards.RIGHT;
            case 3 -> Towards.DOWN;
            default -> {
                try {
                    throw new IllegalDirectionIDException(headToNum);
                } catch (IllegalDirectionIDException e) {
                    e.printStackTrace();
                }

                yield null;
            }
        };

        switch (this.headTo) {
            case LEFT -> {
                body.offer(new BodyBlock(head.getX() + 2, head.getY()));
                body.offer(new BodyBlock(head.getX() + 1, head.getY()));
            }

            case UP -> {
                body.offer(new BodyBlock(head.getX(), head.getY() + 2));
                body.offer(new BodyBlock(head.getX(), head.getY() + 1));
            }

            case RIGHT -> {
                body.offer(new BodyBlock(head.getX() - 2, head.getY()));
                body.offer(new BodyBlock(head.getX() - 1, head.getY()));
            }

            case DOWN -> {
                body.offer(new BodyBlock(head.getX(), head.getY() - 2));
                body.offer(new BodyBlock(head.getX(), head.getY() - 1));
            }

        }

        useless = new BodyBlock(0, 0);
        added = body.peek();
        length = 3;
    }

    /**
     * 蛇移动的方法，分四类，分别考虑前方空，前方墙，前方饭
     * @param food 由于食物对象不共享，所以导入
     */
    public boolean move(FoodBlock food) {
        switch (headTo) {
            case LEFT -> {
                if (head.getX() - 1 == food.getX() && head.getY() == food.getY()) {
                    eat(food);
                } else if (head.getX() - 1 < 0) {
                    dieWall();
                    return false;
                } else if (body.contains(new BodyBlock(head.getX() - 1, head.getY()))) {
                    dieSelf(new BodyBlock(head.getX() - 1, head.getY()));
                    return false;
                } else {
                    useless = body.poll();
                    added = new BodyBlock(head.getX(), head.getY());
                    body.offer(added);
                    head = new HeadBlock(head.getX() - 1, head.getY());
                }
            }

            case UP -> {
                if (head.getX() == food.getX() && head.getY() - 1 == food.getY()) {
                    eat(food);
                } else if (head.getY() - 1 < 0) {
                    dieWall();
                    return false;
                } else if (body.contains(new BodyBlock(head.getX(), head.getY() - 1))) {
                    dieSelf(new BodyBlock(head.getX(), head.getY() - 1));
                    return false;
                } else {
                    useless = body.poll();
                    added = new BodyBlock(head.getX(), head.getY());
                    body.offer(added);
                    head = new HeadBlock(head.getX(), head.getY() - 1);
                }
            }

            case RIGHT -> {
                if (head.getX() + 1 == food.getX() && head.getY() == food.getY()) {
                    eat(food);
                } else if (head.getX() + 1 > 75) {
                    dieWall();
                    return false;
                } else if (body.contains(new BodyBlock(head.getX() + 1, head.getY()))) {
                    dieSelf(new BodyBlock(head.getX() + 1, head.getY()));
                    return false;
                } else {
                    useless = body.poll();
                    added = new BodyBlock(head.getX(), head.getY());
                    body.offer(added);
                    head = new HeadBlock(head.getX() + 1, head.getY());
                }
            }

            case DOWN -> {
                if (head.getX() == food.getX() && head.getY() + 1 == food.getY()) {
                    eat(food);
                } else if (head.getY() + 1 > 45) {
                    dieWall();
                    return false;
                } else if (body.contains(new BodyBlock(head.getX(), head.getY() + 1))) {
                    dieSelf(new BodyBlock(head.getX(), head.getY() + 1));
                    return false;
                } else {
                    useless = body.poll();
                    added = new BodyBlock(head.getX(), head.getY());
                    body.offer(added);
                    head = new HeadBlock(head.getX(), head.getY() + 1);
                }
            }
        }

        return true;
    }

    /**
     * 吃食物的方法 <br>
     * 蛇在前方有食物的时候，会吃饭，即身体最前端新增方块，后方不移动，头覆盖原食物节点，并刷新食物位置
     * @param food 蛇前方的食物
     */
    private void eat(FoodBlock food) {
        added = new BodyBlock(head.getX(), head.getY());
        body.offer(added);
        head = new HeadBlock(food.getX(), food.getY());
        Window.setFood(Utils.flushFood(this, Window.random));
        length++;
        Window.setLen(this.length);
    }

    /**
     * 更改蛇在墙上撞死的提示信息
     */
    private void dieWall() {
        if (dejaVu) {
            Window.setDeath("感受到了动能");
        } else {
            Window.setDeath("不看路");
        }
    }

    /**
     * 更改蛇死于自己的提示信息
     * @param block 前方的方块
     */
    private void dieSelf(BodyBlock block) {
        BodyBlock tail = body.peek();
        assert tail != null;
        if (block.getX() == tail.getX() && block.getY() == tail.getY()) {
            Window.setDeath("尾巴拍到头");
        } else {
            if (dejaVu) {
                Window.setDeath("肚皮有点痛");
            } else {
                Window.setDeath("打了个死结");
            }
        }
    }
}
