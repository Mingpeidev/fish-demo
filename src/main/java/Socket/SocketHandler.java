package Socket;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import sensor.Rxtx_fish;

public class SocketHandler extends IoHandlerAdapter{
	
private Rxtx_fish testRxtx = new Rxtx_fish();
	
	private Timer timer;

	private int smart = 0;
	private int water;
	private int wendu;
	private int o2;
	
	@Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("exceptionCaught: " + cause);
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
			
			//System.out.println(message.toString());
			
			JSONObject json=new JSONObject(message.toString());
			
			smart=json.getInt("smart");
			wendu=json.getInt("watertemp");
			water=json.getInt("watertime");
			o2=json.getInt("o2");
			
			System.out.println("getJson数据>"+json.getInt("id")+json.getInt("smart")+json.getInt("watertemp")+json.getInt("watertime")+json.getInt("o2"));

			if (smart == 1) {
				
				timer = new Timer();
				
				testRxtx.setSwendu(wendu);
				testRxtx.setSmart(smart);
				O2TimerTask task = new O2TimerTask();
				O2_CloseTimerTask task2 = new O2_CloseTimerTask();
				WaterTimerTask task3 = new WaterTimerTask();
				Water_CloseTimerTask task4 = new Water_CloseTimerTask();
				timer.schedule(task, 0, o2);
				timer.schedule(task2, 10000, o2);
				timer.schedule(task3, 0, water);
				timer.schedule(task4, 10000, water);
			
			}else {
				testRxtx.setSmart(smart);
				timer.cancel();
			}
		}

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

					// session.write(testRxtx.xxx);
					// System.out.println("输出："+testRxtx.xxx);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	public class O2TimerTask extends TimerTask {

		public void run() {

			O2Smart("1");
		}
	}
	
	public class O2_CloseTimerTask extends TimerTask {

		public void run() {

			O2Smart("0");
		}
	}

	public void O2Smart(String x) {
		
		testRxtx.sendO2Msg(x);
		
	}
	
	public class WaterTimerTask extends TimerTask {

		public void run() {

			WaterSmart("1");
		}
	}
	
	public class Water_CloseTimerTask extends TimerTask {

		public void run() {

			WaterSmart("0");
		}
	}

	public void WaterSmart(String x) {
		
		testRxtx.sendWaterMsg(x);
		
	}
}
