package show;

import util.Listener;

/**
 * 运行主方法的位置 <br><br>
 *
 * 这是一个贪吃蛇的程序（可能会知识不足而弃坑）<br>
 * 这里会创建窗口，划分方块，创建蛇对象和食物对象，运行程序 <br>
 * 而由于添加了按钮，所有操作在并行线程中完成（不知道为啥，但这样能跑），这个主方法创建对象
 */
public class Main {
    public static void main(String[] args) {
        Window window = new Window();

        // 添加按钮
        window.addButton();

        // 添加文本
        window.addText();

        // 开启游戏线程
        new Thread(() -> {
            // 添加键盘监听器
            window.addKeyListener(new Listener(window.snack));
            window.requestFocusInWindow();

            // 当线程启动时游戏运行
            window.runGame();
        }).start();

        // 最后设置可见以确保组件显示
        window.setVisible(true);
    }
}
