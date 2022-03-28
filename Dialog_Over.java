package hengsha.hwq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dialog_Over {

    //空参构造调用初始方法
    public Dialog_Over() {
        initDialogConfirmStart();
    }


    public void initDialogConfirmStart() {

        JFrame jFrame = new JFrame("俄罗斯方块");


        //封面设置
        ImageIcon bj = new ImageIcon("over.jpeg");
        JLabel label = new JLabel(bj);
        label.setSize(bj.getIconWidth(),bj.getIconHeight());
        //noinspection removal     ?/??
        jFrame.getLayeredPane().add(label, Integer.valueOf(Integer.MAX_VALUE));

        //窗口设置
        jFrame.setSize(960,520);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //按钮设置
        //创建一个页尾JPanel面板，并将按钮组件加入其中
        JPanel jPanel = new JPanel();
        //两个按钮——寄存于JPanel
        JButton btn1 = new JButton("重新启动游戏");
        JButton btn2 = new JButton("退出游戏");
        JButton btn3 = new JButton("求助消除");
        jPanel.add(btn1);
        jPanel.add(btn2);
        jPanel.add(btn3);
        //添加两个选择按钮
        jFrame.add(jPanel,BorderLayout.PAGE_END);

        //为启动游戏按钮添加单击事件
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 new Test();
            }
        });

        //为退出游戏按钮添加单击事件
        btn2.addActionListener(new ActionListener() {
            @Override                              //强制关闭虚拟机
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        jFrame.setVisible(true);
    }
}
