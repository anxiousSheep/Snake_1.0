package show;

import entity.*;
import exception.BlockOutOfFrameException;
import util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * 窗口，这里会将窗口创建出来，并覆盖方块
 */
public class Window extends JFrame {
    // 随机数工具
    public static Random random = new Random();

    // 大标题
    private String tit = "上班摸鱼贪吃蛇";

    // 大标题标签
    JLabel title = new JLabel(tit, JLabel.CENTER);

    // 小标题
    private String word = "我倒要看看你能摸多久";

    // 小标题标签
    JLabel smallTitle = new JLabel(word, JLabel.CENTER);

    // 边长
    private static final int SIZE = 8;

    public static void setFood(FoodBlock food) {
        Window.food = food;
    }

    // 食物方块
    private static FoodBlock food = new FoodBlock(random.nextInt(76), random.nextInt(46));

    // 蛇对象
    Snack snack = new Snack(random.nextInt(2, 74), random.nextInt(2, 44));

    public static boolean isRunning() {
        return running;
    }

    // 游戏是否进行
    static boolean running = false;

    // 场地是否构建
    boolean set = false;

    // 上次死亡原因
    private static String death = "---";

    // 上次死亡原因标签
    private final JLabel lastDeath = new JLabel(death);

    // 当前长度
    private static String len = 3 + "";

    public static void setDeath(String death) {
        Window.death = death;
    }

    public static void setLen(int len) {
        Window.len = len + "";
    }

    // 当前长度标签
    private final JLabel currentLength = new JLabel(len);

    // 这个按钮需要单独拿出来，因为要禁用
    JButton pause = new JButton("开 始");

    public Window() {
        this.setTitle("上班摸鱼贪吃蛇 1.0 | 锅铲祖师制造");
        this.setSize(new Dimension(812, 612));
        // 对于swing来说，中间的一段是必需的
        this.getContentPane().setBackground(new Color(48, 48, 48));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.setLocation(400, 100);
    }

    /**
     * 创建按钮的方法，包括开始游戏，暂停游戏，重置游戏 <br>
     * 这些按钮通过标签来操控线程
     */
     void addButton() {
         JButton reset = new JButton("重 置");

         pause.setBounds(550, 20, 100, 40);
         reset.setBounds(665, 20, 100, 40);

         Utils.setButtonStyle(pause);
         Utils.setButtonStyle(reset);

         pause.setFont(new Font("黑体", Font.BOLD, 25));
         reset.setFont(new Font("黑体", Font.BOLD, 25));

         pause.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 if (!pause.isEnabled()) {
                     return;
                 }
                 if (running) {
                     running = false;
                     pause.setText("继 续");
                 } else {
                     running = true;
                     pause.setText("暂 停");
                 }
                 requestFocusInWindow();
             }
         });

         reset.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 running = false;
                 set = false;

                 pause.setEnabled(true);

                 title.setForeground(new Color(220, 220, 220));
                 smallTitle.setForeground(new Color(220, 220, 220));

                 Utils.flushSnake(snack, random.nextInt(2, 74), random.nextInt(2, 44));
                 food = Utils.flushFood(snack, random);
                 len = 3 + "";
                 tit = "上班摸鱼贪吃蛇";
                 word = "我倒要看看你能摸多久";

                 repaint();

                 pause.setText("开 始");
             }
         });

         this.add(pause, 0);
         this.add(reset, 0);

    }

    /**
     * 运行游戏
     */
    public void runGame() {
        while (true) {
            if (running) {
                if (!snack.move(food)) {
                    running = false;

                    pause.setEnabled(false);

                    title.setForeground(new Color(255, 255, 150));
                    smallTitle.setForeground(new Color(255, 255, 150));

                    int titleText = new Random().nextInt(4);
                    int belowText = new Random().nextInt(4);

                    switch (titleText) {
                        case 0 -> {
                            if (death.equals("不看路")) {
                                tit = "来世看路~";
                            } else {
                                tit = "哎。。惨不忍睹";
                            }
                        }
                        case 1 -> tit = "你凉了！";
                        case 2 -> tit = "YOU DIED!";
                        case 3 -> tit = "Q W Q";
                    }

                    switch (belowText) {
                        case 0 -> word = "非要在同事面前装B";
                        case 1 -> word = "去工作转换一下心情？";
                        case 2 -> word = "继续！挑战一下全服第一！";
                        case 3 -> {
                            if (death.equals("肚皮有点痛") || death.equals("感受到了动能")) {
                                word = "别有事没事按着E不放";
                            } else {
                                word = "是老板来了嘛？";
                            }
                        }
                    }
                }
                this.repaint();
                try {
                    Thread.sleep(snack.getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (!set) {
            super.paint(g);

            // the border of texts
            g.setColor(new Color(220, 220, 220));
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(31, 45, 190, 56, 7, 7);

            // empty blocks
            g.setColor(EmptyBlock.color);
            for (int i = 27; i < this.getWidth() - 25; i += 10) {
                for (int j = 125; j < this.getHeight() - 27; j += 10) {
                    g.fillRect(i, j, SIZE, SIZE);
                }
            }

            // wall blocks
            g.setColor(WallBlock.color);
            for (int i = 17; i < this.getWidth() - 15; i += 10) {
                g.fillRect(i, 115, 8, 8);
                g.fillRect(i, this.getHeight() - 27, SIZE, SIZE);
            }
            for (int i = 125; i < this.getHeight() - 17; i += 10) {
                g.fillRect(17, i, SIZE, SIZE);
                g.fillRect(this.getWidth() - 25, i, SIZE, SIZE);
            }

            // 初始化的蛇身，不能用队列构建
            g.setColor(BodyBlock.color);
            for (BodyBlock singleBody :
                    snack.getBody()) {
                try {
                    int[] position = Utils.getPixel(singleBody.getX(), singleBody.getY());
                    g.fillRect(position[0], position[1], SIZE, SIZE);
                } catch (BlockOutOfFrameException e) {
                    e.printStackTrace();
                }
            }

            // 背景已构建
            set = true;
        }

        // food blocks
        g.setColor(FoodBlock.color);
        try {
            int[] position = Utils.getPixel(food.getX(), food.getY());
            g.fillRect(position[0], position[1], SIZE, SIZE);
        } catch (BlockOutOfFrameException e) {
            e.printStackTrace();
        }

        // snake head
        g.setColor(HeadBlock.color);
        try {
            int[] position = Utils.getPixel(snack.getHead().getX(), snack.getHead().getY());
            g.fillRect(position[0], position[1], SIZE, SIZE);
        } catch (BlockOutOfFrameException e) {
            e.printStackTrace();
        }

        // snake body (poll useless)
        g.setColor(EmptyBlock.color);
        BodyBlock uselessBlock = snack.getUseless();
        try {
            int[] uselessPosition = Utils.getPixel(uselessBlock.getX(), uselessBlock.getY());
            g.fillRect(uselessPosition[0], uselessPosition[1], SIZE, SIZE);
        } catch (BlockOutOfFrameException e) {
            e.printStackTrace();
        }

        // snake body (draw new)
        g.setColor(BodyBlock.color);
        BodyBlock newBlock = snack.getAdded();
        try {
            int[] newPosition = Utils.getPixel(newBlock.getX(), newBlock.getY());
            g.fillRect(newPosition[0], newPosition[1], SIZE, SIZE);
        } catch (BlockOutOfFrameException e) {
            e.printStackTrace();
        }

        // texts
        title.setText(tit);
        smallTitle.setText(word);
        lastDeath.setText(death);
        currentLength.setText(len);
    }

    /**
     * 创建大标题，显示游戏名和蛇长，死亡信息，本次游戏最高纪录
     * （在找到控件置顶的办法之前是没用的qwq）
     */
    public void addText() {
        // 大标题
        title.setForeground(new Color(220, 220, 220));
        title.setBounds(257, -5, 250, 80);
        title.setFont(new Font("黑体", Font.BOLD, 34));
        this.add(title);

        // 小标题
        smallTitle.setForeground(new Color(220, 220, 220));
        smallTitle.setBounds(257, 52, 250, 30);
        smallTitle.setFont(new Font("黑体", Font.BOLD, 17));
        this.add(smallTitle);

        // 当前长度 & 最高纪录的文本
        JLabel text1 = new JLabel("当前长度：");
        text1.setForeground(new Color(220, 220, 220));
        text1.setBounds(30, 21, 100, 20);
        text1.setFont(new Font("黑体", Font.BOLD, 15));
        this.add(text1);

        JLabel text2 = new JLabel("上次死因：");
        text2.setForeground(new Color(255, 255, 150));
        text2.setBounds(30, 46, 100, 20);
        text2.setFont(new Font("黑体", Font.BOLD, 15));
        this.add(text2);

        // 当前长度数据
        currentLength.setForeground(new Color(220, 220, 220));
        currentLength.setBounds(110, 21, 100, 20);
        currentLength.setFont(new Font("黑体", Font.BOLD, 15));
        this.add(currentLength);

        // 上次死亡原因
        lastDeath.setForeground(new Color(255, 255, 150));
        lastDeath.setBounds(110, 46, 100, 20);
        lastDeath.setFont(new Font("黑体", Font.BOLD, 15));
        this.add(lastDeath);
    }
}
