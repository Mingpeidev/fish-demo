package Socket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import sensor.Rxtx_fish;

public class SocketHandler extends IoHandlerAdapter{
	
	@Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("exceptionCaught: " + cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("recieve : " + (String) message);
        session.write("hello I am server");
        //if(message.toString().equals("111")) {

            //SendThread map1 = new SendThread(session);

      //  map1.start();

           // }else {

            //new Rxtx_fish().sendMsg( String.valueOf(message));

           // }

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
    
   /* public SendThread(IoSession session) {

    	super();

    	this.session = session;

    	}

    	IoSession session;

    	@Override

    	public void run() {

    	       // 2.从容器中获取mapper

    	while (true) {

    	try {

    	sleep(500);//100ms

    	Set<String> keySet = testRxtx.dataAll.keySet();

    	Iterator<String> it1 = keySet.iterator();

    	while (it1.hasNext()) {

    	String ID = it1.next();

    	if(ID.equals("EE 61 01")) {

    	session.write("02 07 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	//System.out.println("输出："+"02 07 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	}else if(ID.equals("D7 15 01")) {

    	session.write("02 08 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	//System.out.println("输出："+"02 08 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	}else if(ID.equals("00 A0 01")) {

    	session.write("02 07 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	//System.out.println("输出："+"02 07 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	}else if(ID.equals("47 8C 01")) {

    	session.write("02 07 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	//System.out.println("输出："+"02 07 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	}else if(ID.equals("47 8C 02")) {

    	session.write("02 07 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	//System.out.println("输出："+"02 07 18 00 F1 "+ID + " " + testRxtx.dataAll.get(ID)+" 11");

    	}

    	}

    	//session.write(testRxtx.xxx);

    	//System.out.println("输出："+testRxtx.xxx);

    	} catch (InterruptedException e) {

    	// TODO Auto-generated catch block

    	e.printStackTrace();

    	}}}*/



}
