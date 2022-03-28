package hengsha.hwq;

public class Test extends Dialog_Start {


    public static void main(String[] args) {


        //对话——确认是否开启游戏、导入游戏封面
        Dialog_Start dialog_start = new Dialog_Start();
        dialog_start.initDialogConfirmStart();


        //启动游戏

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        Tetris tetris = new Tetris();
        tetris.game_begin();

            if (!tetris.isRunning) {
                Dialog_Over dialog_over = new Dialog_Over();


            //对话——确认是否暂停游戏


            //对话——是否保存游戏进度

            //结束游戏

            //读档——开始游戏


        }


    }
}
