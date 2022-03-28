package hengsha.hwq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dialog_Start extends JFrame{

    private JFrame dialog_start;
    //跳转变量
    public static boolean jumpStart;

    public void initDialogConfirmStart() {

    }

    //空参构造调用初始方法
    public Dialog_Start() {
        dialog_start = new JFrame("俄罗斯方块");

        //封面设置
        ImageIcon bj = new ImageIcon("start.png");
        JLabel label = new JLabel(bj);
        label.setSize(bj.getIconWidth(), bj.getIconHeight());
        //noinspectionremoval     ?/??
        dialog_start.getLayeredPane().add(label, Integer.valueOf(Integer.MAX_VALUE));

        //窗口设置
        dialog_start.setSize(500, 470);
        dialog_start.setLocationRelativeTo(null);
        dialog_start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //按钮设置
        //创建一个页尾JPanel面板，并将按钮组件加入其中
        JPanel jPanel = new JPanel();
        //两个按钮——寄存于JPanel
        JButton btn1 = new JButton("启动游戏");
        JButton btn2 = new JButton("退出游戏");
        JButton btn3 = new JButton("测试按钮");
        jPanel.add(btn1);
        jPanel.add(btn2);
        jPanel.add(btn3);
        //添加两个选择按钮
        dialog_start.add(jPanel, BorderLayout.PAGE_END);

        //为启动游戏按钮添加单击事件
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("跳转启动！");
                jumpStart = true;
                dialog_start.setVisible(false);  //隐藏掉当前页面
               Tetris tetris = new Tetris();
                try {
                    Dialog_Start.this.wait(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });


        //为退出游戏按钮添加单击事件
        btn2.addActionListener(new ActionListener() {
            @Override                              //强制关闭虚拟机
            public void actionPerformed(ActionEvent e) {
                System.out.println("强制退出正常！");
                System.exit(0);
            }
        });

        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        dialog_start.setVisible(true);
        System.out.println("页面启动正常！");


    }

    public static void main(String[] args) {

    }

}
