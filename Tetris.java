package hengsha.hwq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public  class Tetris extends JFrame implements KeyListener {
    //游戏的行数26,列数12
    private static final int game_x = 26;
    private static final int game_y = 12;

    //文本域数组
    JTextArea[][] text;
    //二维数组
    int[][] data;

    //显示游戏状态的标签
    JLabel label_state;
    //显示游戏分数的标签
    JLabel label_score;

    //用于判断游戏是否结束
    boolean isRunning;

    //用于存储所有的方块的数组
    int[] allRect;
    //用于存储当前方块的变量
    int rect;

    //线程的休眠时间
    int time = 1000;
    //表示方块坐标
    int x, y;
    //该变量用于计算得分
    int score = 0;

    //定义一个标志变量，用于判断游戏是否暂停
    boolean game_pause = false;
    //定义一个变量用于记录按下暂停键的次数
    int pause_times = 0;

    //初始化窗口
    public void initWindow() {
        //设置窗口大小
        this.setSize(600, 850);
        //设置窗口是否可见
        this.setVisible(true);
        //设置窗口居中
        this.setLocationRelativeTo(null);
        //设置释放窗体
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口大小不可变
        this.setResizable(false);
        //设置标题
        this.setTitle("我的俄罗斯方块游戏");
    }

    //初始化游戏界面
    public void initGamePanel() {
        JPanel game_main = new JPanel();
        game_main.setLayout(new GridLayout(game_x, game_y, 1, 1));
        //初始化面板
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {

                //设置文本域的行列数
                text[i][j] = new JTextArea(game_x, game_y);
                //设置文本域的背景颜色
                text[i][j].setBackground(Color.WHITE);
                //添加键盘监听事件
                text[i][j].addKeyListener(this);

                //初始化游戏边界
                if (j == 0 || j == text[i].length - 1 || i == text.length - 1) {
                    text[i][j].setBackground(Color.MAGENTA);
                    data[i][j] = 1;
                }
                //设置文本区域不可编辑
                text[i][j].setEditable(false);
                //文本区域添加到主面板上
                game_main.add(text[i][j]);
            }
        }
        //添加到窗口中
        this.setLayout(new BorderLayout());
        this.add(game_main, BorderLayout.CENTER);
    }

    //初始化游戏的说明面板
    public void initExplainPanel() {

        //创建游戏的操作说明面板
        JPanel jPanel_operation = new JPanel();
        jPanel_operation.setSize(200,425);
        //创建游戏的状态说明面板
        JPanel jPanel_condition = new JPanel();
        jPanel_condition.setSize(200,425);
        //创建游戏的说明总面板——包括（游戏状态说明面板、游戏操作说明面板、下一方块说明面板）
        JPanel word_right = new JPanel();
        word_right.setSize(200,850);

        //设置布局管理
        jPanel_operation.setLayout(new GridLayout(15,1,0,0));
        jPanel_condition.setLayout(new GridLayout(15, 1,0,0));
        word_right.setLayout(new GridLayout(2,1,0,0));

        //初始化操作说明面板
        //在操作说明面板,添加说明文字标签
        jPanel_operation.add(new JLabel("     操作说明    "));
        jPanel_operation.add(new JLabel("按空格键,方块变形"));
        jPanel_operation.add(new JLabel("按左箭头,方块左移"));
        jPanel_operation.add(new JLabel("按右箭头,方块右移"));
        jPanel_operation.add(new JLabel("按下箭头,方块下落"));
        jPanel_operation.add(new JLabel("按下P键,游戏暂停"));

        //设置状态标签的内容为红色字体
        label_state.setForeground(Color.RED);
        //将游戏状态标签、得分标签加入游戏状态说明面板
        jPanel_condition.add(label_score);
        jPanel_condition.add(label_state);

        //添加说明总面板
        word_right.add(jPanel_condition);
        word_right.add(jPanel_operation);
        this.add(word_right,BorderLayout.EAST);
    }

    //空参构造初始化变量
    public Tetris() {
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];

        //初始化表示游戏状态的标签
        label_state = new JLabel("游戏状态: 正在游戏中!");
        //初始化表示游戏分数的标签
        label_score = new JLabel("游戏得分为: 0");

        initGamePanel();    //初始化游戏的说明面板
        initExplainPanel(); //初始化游戏的说明面板
        initWindow();       //初始化窗口


        //初始化开始游戏的标志
        isRunning = true;

        //初始化存放方块的数组
        allRect = new int[]{0x00cc, 0x8888, 0x000f, 0x888f, 0xf888, 0xf111, 0x111f, 0x0eee, 0xffff, 0x0008, 0x0888, 0x000e, 0x0088, 0x000c, 0x08c8, 0x00e4
                , 0x04c4, 0x004e, 0x08c4, 0x006c, 0x04c8, 0x00c6};
    }

    //开始游戏的方法
    public void game_begin() {
        System.out.println("游戏运行正常！");
        //将游戏循环进行——每产生一个方块就加载一次
        while (true) {
            //判断游戏是否结束
            if (!isRunning) {//游戏结束，退出循环
                break;
            }
            //进行游戏
            game_run();
        }
        //在标签位置显示"游戏结束"
        label_state.setText("游戏状态: 游戏结束!");
    }

    //随机生成下落方块形状的方法
    public void ranRect() {
        //生成随机数——对标方块编号
        Random random = new Random();
        //捕获当前方块
        rect = allRect[random.nextInt(22)];
    }

    //游戏运行的方法
    public void game_run() {
        //生成方块
        ranRect();

        //方块下落位置
        x = 0;
        y = 5;

        for (int i = 0; i < game_x; i++) {
            try {
                //使得方块在每一层产生停顿感
                Thread.sleep(time);
            //判断方块是否可以下落——暂停检测——如果暂停，则进入for死循环
                if (game_pause) {
                    i--;
                    System.out.println("暂停");
                }
            //没有暂停
                else {
                    if (!canFall(x, y)) {  //——不可以下落——下落空间检测

                        //将data置为1,表示有方块占用  ——该位置方块成功落地，重置为1，
                        changData(x, y);

                        //循环遍历4层,看是否有行可以消除
                        for (int j = x; j < x + 4; j++) { //行数
                            int sum = 0;
                            for (int k = 1; k <= (game_y - 2); k++) { //列数
                                if (data[j][k] == 1) {
                                    sum++;
                                }
                            }
                            //判断是否有一行可以被消除
                            if (sum == (game_y - 2)) {
                                //消除j这一行
                                removeRow(j); //——25
                            }
                        }

                        //判断游戏是否失败
                        for (int j = 1; j <= (game_y - 2); j++) {
                            if (data[3][j] == 1) {  //第四行出现方块，游戏结束
                                isRunning = false;
                                break;
                            }
                        }

                        break; //退出循环，重新生成新的方块
                    } else {
                        //层数+1
                        x++;
                        //方块下落一行
                        fall(x, y);
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //判断方块是否可以继续下落的方法
    public boolean canFall(int m, int n) {
        //定义一个变量
        int temp = 0x8000;
        //遍历4 * 4方格
        //——不可以下落
        for (int i = 0; i < 4; i++) {    //行数遍历
            for (int j = 0; j < 4; j++) {   //列数遍历
                if ((temp & rect) != 0) {
                    //判断该位置的下一行是否有方块
                    if (data[m + 1][n] == 1) {
                        return false;
                    }
                }
                n++;  //列数加1
                temp >>= 1;  //右移一位
            }
            m++;  //行数加1
            n = n - 4;  //列数重回首列
        }
        //可以下落
        return true;
    }

    //改变不可下降的方块对应的区域的值的方法
    public void changData(int m, int n) {
        //定义一个变量
        int temp = 0x8000;
        //遍历整个4 * 4的方块
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {
                    data[m][n] = 1;  //将该位置置为1，表示有方块占用
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    //移除某一行的所有方块,令以上方块掉落的方法
    public void removeRow(int row) {
        int temp = 100;
        for (int i = row; i >= 1; i--) {
            for (int j = 1; j <= (game_y - 2); j++) {
                //进行覆盖——将上一层的数值覆盖下一层的数值
                data[i][j] = data[i - 1][j];
            }
        }
        //刷新游戏区域
        refresh(row);

        //方块加速???
        if (time > temp) {
            time = time - temp; //900
        }

        score = score + temp;

        //显示变化后的分数
        label_score.setText("游戏得分为: " + score);
    }

    //刷新移除某一行后的游戏界面的方法
    public void refresh(int row) {
        //遍历row行以上的游戏区域
        for (int i = row; i >= 1; i--) {  //——25
            for (int j = 1; j <= (game_y - 2); j++) {
                if (data[i][j] == 1) {
                    text[i][j].setBackground(Color.BLUE);
                } else {
                    text[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    //方块向下掉落一层的方法
    public void fall(int m, int n) {
        if (m > 0) {
            //清除上一层方块
            clear(m - 1, n);
        }
        //重新绘制方块
        draw(m, n);
    }

    //清除方块掉落后,上一层有颜色的地方的方法
    public void clear(int m, int n) {
        //定义变量
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {
                    //将上一层重新置为白色
                    text[m][n].setBackground(Color.WHITE);
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    //重新绘制掉落后方块的方法
    public void draw(int m, int n) {
        //定义变量
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {
                    //将下一层置为蓝色
                    text[m][n].setBackground(Color.BLUE);
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //控制方块进行变形
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {   //空格键常量
            //判断游戏是否结束
            if (!isRunning) {
                return;
            }
            //判断游戏是否暂停——暂停的话无法变形
            if (game_pause ){
                  return;
            }
            //定义变量,存储目前方块的索引
            int old;
            for (old = 0; old < allRect.length; old++) {
                //判断是否是当前方块
                if (rect == allRect[old]) {
                    break;  //捕获到当前方块索引值立即退出循环
                }
            }

            //定义变量,存储变形后方块
            int next;

            //1.方块无需变形
            //判断是方块
            if (old == 0 || old == 7 || old == 8 || old == 9) {
                return;//不需要变形的方块
            }
            //2.方块需要变形
            //首先清除当前方块
            clear(x, y);
            //1.长单4
            if (old == 1 || old == 2) {
                next = allRect[old == 1 ? 2 : 1];

                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            //2.双边长单4
            if (old >= 3 && old <= 6) {
                next = allRect[old + 1 > 6 ? 3 : old + 1];

                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            //3.长单3
            if (old == 10 || old == 11) {
                next = allRect[old == 10 ? 11 : 10];

                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            //4.长单2
            if (old == 12 || old == 13) {
                next = allRect[old == 12 ? 13 : 12];

                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            //5.山字型
            if (old >= 14 && old <= 17) {
                next = allRect[old + 1 > 17 ? 14 : old + 1];

                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            //6.Z字型
            if (old == 18 || old == 19) {
                next = allRect[old == 18 ? 19 : 18];

                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            //7.Z字型—反型
            if (old == 20 || old == 21) {
                next = allRect[old == 20 ? 21 : 20];

                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }

            //重新绘制变形后方块
            draw(x, y);
        }
    }

    //判断方块此时是否可以变形的方法
    public boolean canTurn(int a, int m, int n) {
        //创建变量
        int temp = 0x8000;
        //遍历整个方块   4*4算法遍历
        for (int i = 0; i < 4; i++) {    //不可以变形
            for (int j = 0; j < 4; j++) {
                if ((a & temp) != 0) {
                    if (data[m][n] == 1) {
                        return false;
                    }
                }
                n++;//变形方块下标右移一位
                temp >>= 1;//单位方块右移一位
            }
            m++;//变形方块下移一行
            n = n - 4;//回到首列
        }
        //可以变形
        return true;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        //1.方块进行左移
        if (e.getKeyCode() == 37) {
            //判断游戏是否结束
            if (!isRunning) {
                return;
            }
            //判断游戏是否暂停——暂停的话无法左移
            if (game_pause ){
                return;
            }
            //方块是否碰到左墙壁
            if (y <= 1) {
                return;
            }
            //方块左移过程中是否与其他方块相撞——无法移动
            //定义一个变量——单位方块
            int temp = 0x8000;
            //遍历方块——确定不能左移
            for (int i = x; i < x + 4; i++) {
                for (int j = y; j < y + 4; j++) {

                    //该位置有方块
                    if ((temp & rect) != 0) {
                        if (data[i][j - 1] == 1) {    //该位置左边有方块
                            return;       //无法左移、返回  方块继续向下掉落
                        }
                    }
                    //该位置无方块，单位方块右移一位
                    temp >>= 1;  //0x0800
                }
            }
            //能左移
            //首先清除目前方块
            clear(x, y);
            //方块y坐标左移
            y--;
            //重新绘制方块—方块左移一位
            draw(x, y);

        }

        //2.方块进行右移
        if (e.getKeyCode() == 39) {
            //判断游戏是否结束
            if (!isRunning) {
                return;
            }
            //判断游戏是否暂停——暂停的话无法右移
            if (game_pause ){
                return;
            }
            //方块是否碰到右墙壁
            if (y >= 10) {
                return;
            }
            //方块左移过程中是否与其他方块相撞——无法移动
            //定义一个变量——单位方块
            int temp = 0x8000;
            //遍历方块
            for (int i = x; i < x + 4; i++) {
                for (int j = y; j < y + 4; j++) {

                    //该位置有方块
                    if ((temp & rect) != 0) {
                        if (data[i][j + 1] == 1) {    //该位置左边有方块
                            return;       //无法右移、返回  方块继续向下掉落
                        }
                    }
                    //该位置无方块，单位方块右移一位
                    temp >>= 1;  //0x0800
                }
            }
            //首先清除目前方块
            clear(x, y);
            //方块y坐标右移一位
            y++;
            //重新绘制方块
            draw(x, y);

        }
        //3.方块进行加速下落——没敲击一次则向下掉落一层——加上原来的一层，则一次敲击掉两层（肉眼可见）
        //按住则为不断敲击——产生加速掉落的感觉
        //方块进行下落
        if (e.getKeyCode() == 40) {
            //判断游戏是否结束
            if (!isRunning) {
                return;
            }
            //判断游戏是否暂停——暂停的话无法下落
            if (game_pause ){
                return;
            }
            //判断方块是否可以下落
            if (!canFall(x, y)) {
                return;
            }
            clear(x, y);
            //改变方块的坐标
            x++;
            draw(x, y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //控制游戏暂停
        if (e.getKeyChar() == 'p') {
            //判断游戏是否结束
            if (!isRunning) {
                return;
            }

            pause_times++;

            //判断按下一次，暂停游戏
            if (pause_times == 1) {
                game_pause = true;
                label_state.setText("游戏状态：暂停中！");
            }

            //判断按下两次，继续游戏
            if (pause_times == 2) {
                game_pause = false;
                pause_times = 0;
                label_state.setText("游戏状态：正在进行中！");
            }

        }
    }


    public static void main(String[] args) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}