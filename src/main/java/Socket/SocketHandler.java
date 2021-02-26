package Socket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import sensor.Rxtx_fish;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SocketHandler extends IoHandlerAdapter {

    private Rxtx_fish testRxtx = new Rxtx_fish();

    private Timer timer;

    /**
     * 开关标志位
     */
    private boolean O2Flag = true;
    private boolean WaterFlag = true;

    private int smart = 0;
    private int water;
    private int wendu;
    private int o2;

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("exceptionCaught: " + cause);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed");
        new Rxtx_fish().closeSerialPort();
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("sessionOpen");
        new Rxtx_fish().init();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("recieve : " + (String) message);
        //session.write("hello I am server");
        if (message.toString().equals("111")) {
            System.out.println("收到111------------------------------");
            SendThread map1 = new SendThread(session);
            map1.start();
        } else if (message.toString().trim().length() == 2) {
            System.out.println("收到控制------------------------------");
            testRxtx.sendMsg((String) message);

        } else {
            System.out.println("收到json------------------------------");

            JSONObject json = new JSONObject(message.toString());

            //智能控制
            smart = json.getInt("smart");
            //水温
            wendu = json.getInt("watertemp");
            //间隔放水时间
            water = json.getInt("watertime");
            //间隔供氧气时间
            o2 = json.getInt("o2");

            System.out.println("getJson数据>" + json.getInt("id") + json.getInt("smart") + json.getInt("watertemp") + json.getInt("watertime") + json.getInt("o2"));

            testRxtx.setSmart(smart);
            if (smart == 1) {

                timer = new Timer();
                //设置温度阈值
                testRxtx.setSwendu(wendu);

                O2OpenAndCloseTimerTask task = new O2OpenAndCloseTimerTask();
                WaterOpenAndCloseTimerTask task2 = new WaterOpenAndCloseTimerTask();
                //定时开关供氧
                timer.schedule(task, 0, o2);
                //定时开关水
                timer.schedule(task2, 10000, water);
            } else {
                timer.cancel();
            }
        }
    }

    class SendThread extends Thread {

        public SendThread(IoSession session) {
            super();
            this.session = session;
        }

        IoSession session;

        @Override
        public void run() {

            // 2.从容器中获取mapper
            while (true) {
                try {
                    sleep(500);// 100ms

                    Set<String> keySet = testRxtx.dataAll.keySet();
                    Iterator<String> it1 = keySet.iterator();
                    while (it1.hasNext()) {
                        String ID = it1.next();
                        if (ID.equals("EE 61 01")) {
                            session.write("02 07 18 00 F1 " + ID + " " + testRxtx.dataAll.get(ID) + " 11");
                            // System.out.println("输出："+"02 07 18 00 F1 "+ID + " " +
                            // testRxtx.dataAll.get(ID)+" 11");
                        } else if (ID.equals("D7 15 01")) {
                            session.write("02 08 18 00 F1 " + ID + " " + testRxtx.dataAll.get(ID) + " 11");
                            // System.out.println("输出："+"02 08 18 00 F1 "+ID + " " +
                            // testRxtx.dataAll.get(ID)+" 11");
                        } else if (ID.equals("00 A0 01")) {
                            session.write("02 07 18 00 F1 " + ID + " " + testRxtx.dataAll.get(ID) + " 11");
                            // System.out.println("输出："+"02 07 18 00 F1 "+ID + " " +
                            // testRxtx.dataAll.get(ID)+" 11");
                        } else if (ID.equals("47 8C 01")) {
                            session.write("02 07 18 00 F1 " + ID + " " + testRxtx.dataAll.get(ID) + " 11");
                            // System.out.println("输出："+"02 07 18 00 F1 "+ID + " " +
                            // testRxtx.dataAll.get(ID)+" 11");
                        } else if (ID.equals("47 8C 02")) {
                            session.write("02 07 18 00 F1 " + ID + " " + testRxtx.dataAll.get(ID) + " 11");
                            // System.out.println("输出："+"02 07 18 00 F1 "+ID + " " +
                            // testRxtx.dataAll.get(ID)+" 11");
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class O2OpenAndCloseTimerTask extends TimerTask {
        public void run() {
            if (O2Flag) {
                testRxtx.sendO2Msg("1");
                O2Flag = false;
            } else {
                testRxtx.sendO2Msg("0");
                O2Flag = true;
            }
        }
    }

    public class WaterOpenAndCloseTimerTask extends TimerTask {
        public void run() {
            if (WaterFlag) {
                testRxtx.sendWaterMsg("1");
                WaterFlag = false;
            } else {
                testRxtx.sendWaterMsg("0");
                WaterFlag = true;
            }
        }
    }
}
