package sensor;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@SuppressWarnings("restriction")
public class Rxtx_fish implements SerialPortEventListener {

    String light_control = "";
    String water_control = "";
    String addo2_control = "";
    String heating_control = "";

    private int Swendu = 26;
    private int Smart = 0;


    private static final String DEMONAME = "串口测试";
    /**
     * 检测系统中可用的端口
     */
    private CommPortIdentifier portId = null;

    //输入输出流
    private static InputStream inputStream;
    private static OutputStream outputStream;

    //RS-232的串行口
    private SerialPort serialPort;

    // 地址
    public Map<String, String> dataAll = new HashMap<String, String>();

    public String xxx = new String("000");

    /**
     * 初始化串口
     *
     * @param baudRate 波特率
     */

    public void init() {

        try {
            portId = CommPortIdentifier.getPortIdentifier("COM6");
            System.out.println("打开端口：" + portId.getName());
            serialPort = (SerialPort) portId.open(DEMONAME, 2000);
            //设置串口监听
            serialPort.addEventListener(this);
            //设置开启监听
            serialPort.notifyOnDataAvailable(true);
            //设置波特率、数据位、停止位、检验位
            serialPort.setSerialPortParams(115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //获取输入流
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (PortInUseException e) {
            System.out.println("端口被占用");
            e.printStackTrace();
        } catch (TooManyListenersException e) {
            System.out.println("串口监听类数量过多！添加操作失败！");
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("输入出错");
            e.printStackTrace();
        } catch (NoSuchPortException e) {
            System.out.println("没有该端口");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 监听函数
     */
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.BI: // 通讯中断
            case SerialPortEvent.OE: // 溢位错误
            case SerialPortEvent.FE: // 帧错误
            case SerialPortEvent.PE: // 奇偶校验错误
            case SerialPortEvent.CD: // 载波检测
            case SerialPortEvent.CTS: // 清除发送
            case SerialPortEvent.DSR: // 数据设备准备好
            case SerialPortEvent.RI: // 响铃侦测
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            //获取到有效信息
            case SerialPortEvent.DATA_AVAILABLE:
                //readPort();
                readComm();

                ErrorControl();

                break;

            default:
                break;
        }
    }

    public int getSmart() {
        return Smart;
    }

    public void setSmart(int smart) {
        Smart = smart;
    }

    /**
     * 读取串口信息，并在服务器端显示解析后数据
     */
    public void readPort() {
        byte[] readBuffer = new byte[30];
        int availableBytes = 0;

        String rain = "";
        String light = "";
        String temp = "";
        String humi = "";
        String control = "";

        try {
            while (true) {
                availableBytes = inputStream.available();
                while (availableBytes > 0) {
                    inputStream.read(readBuffer);

                    String x = bytesToHexString(readBuffer).substring(0, 10);
                    String y = bytesToHexString(readBuffer).substring(10, 14);


                    if (x.equals("02071800f1") & y.equals("ee61")) {
                        rain = bytesToHexString(readBuffer).substring(16, 20);
                        System.out.println("rain:" + rain);
                    }
                    if (x.equals("02071800f1") & y.equals("00a0")) {
                        control = bytesToHexString(readBuffer).substring(16, 18);
                        System.out.println("control:" + control);
                    }
                    if (x.equals("02081800f1") && y.equals("d715")) {
                        light = bytesToHexString(readBuffer).substring(16, 20);
                        System.out.println("light:" + exchange(light));
                    }

                    if (x.equals("02081800f1") && y.equals("478c")) {
                        if (bytesToHexString(readBuffer).substring(14, 16).equals("01")) {
                            temp = bytesToHexString(readBuffer).substring(16, 20);
                            System.out.println("temp:" + exchange(temp));
                        }
                        if (bytesToHexString(readBuffer).substring(14, 16).equals("02")) {
                            humi = bytesToHexString(readBuffer).substring(16, 20);
                            System.out.println("humi:" + exchange(humi));
                        }
                    }

                    System.out.println(byte2HexStr(readBuffer));

                    //更新循环条件
                    availableBytes = inputStream.available();
                }
                Thread.sleep(0);
            }
        } catch (IOException e) {
            System.out.println("获取输出流失败");
            System.exit(0);
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // 读取串口返回信息，并定义其长度,主要用这个进行数据库和智能控制
    public void readComm() {
        byte[] readBuffer = new byte[1024];
        String data2 = null;
        try {
            // 从线路上读取数据流
            int len = 0;
            while ((len = inputStream.read(readBuffer)) != -1) {

                System.out.println("实时反馈：" + byte2HexStr(readBuffer, len));

                data2 = byte2HexStr(readBuffer, len);
                int x = data2.split(" ").length;
                String[] handler = data2.split(" ");
                if (x == 10) {
                    // s传感器短地址，t传感器获取数据
                    String s = handler[5] + " " + handler[6] + " " + handler[7];
                    String t = handler[8];
                    // System.out.println("网络地址：" + s + " 值：" + t);
                    dataAll.put(s, t);
                } else if (x == 11) {
                    String s = handler[5] + " " + handler[6] + " " + handler[7];
                    String t = handler[8] + " " + handler[9];
                    // System.err.println(s);
                    // System.err.println(t);
                    // System.out.println("网络地址：" + s + " 值：" + t);
                    dataAll.put(s, t);
                }

                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //向串口发送信息方法
    public void sendMsg(String com) {

        String info = "";
        String msg = "071800F100A001" + com;//要发送的命令
        info = "02" + msg + checkcode(msg);
        System.out.println("info=" + info + "  字符串：" + hexStr2Bytes(info));

        try {
            outputStream.write(hexStr2Bytes(info));
            outputStream.flush();
            System.out.println("输出成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("输出失败");
        }
    }


    public void sendO2Msg(String o2) {
        System.out.println("o2:" + o2);
        System.out.println("0" + binaryString2hexString(light_control + water_control + o2 + heating_control));
        sendMsg("0" + binaryString2hexString(light_control + water_control + o2 + heating_control));
        addo2_control = o2;
    }

    public void sendWaterMsg(String water) {
        System.out.println("water:" + water);
        System.out.println("0" + binaryString2hexString(light_control + water + addo2_control + heating_control));
        sendMsg("0" + binaryString2hexString(light_control + water + addo2_control + heating_control));
        water_control = water;
    }

    class MaplayoutThread extends Thread {
        @Override
        public void run() {

            // 2.从容器中获取mapper

            while (true) {
                try {
                    sleep(3000);
                    Set<String> keySet = dataAll.keySet();
                    Iterator<String> it1 = keySet.iterator();
                    while (it1.hasNext()) {
                        String ID = it1.next();
                        System.out.println(ID + " " + dataAll.get(ID));
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
    }

    public void ErrorControl() {

        String control = "";
        control = dataAll.get("00 A0 01");

        String x = hexString2binaryString(control);

        light_control = x.substring(4, 5);
        water_control = x.substring(5, 6);
        addo2_control = x.substring(6, 7);
        heating_control = x.substring(7, 8);
        if (Smart == 1) {
            if (dataAll.get("EE 61 01").equals("01") && water_control.equals("1")) {
                sendMsg("0" + binaryString2hexString(light_control + "0" + addo2_control + heating_control));
            }
            /*
             * else if (dataAll.get("EE 61 01").equals("00") && water_control.equals("0")) {
             * sendMsg("0" + binaryString2hexString(light_control + "1" + addo2_control +
             * heating_control)); }
             */

            String[] handler = dataAll.get("47 8C 01").split(" ");
            String t = handler[1] + handler[0];
            float wendu = (float) (Integer.parseInt(t, 16) / 100.00);

            if (wendu > Swendu && heating_control.equals("1")) {
                sendMsg("0" + binaryString2hexString(light_control + water_control + addo2_control + "0"));
            } else if (wendu < Swendu && heating_control.equals("0")) {
                sendMsg("0" + binaryString2hexString(light_control + water_control + addo2_control + "1"));
            }
        }
    }


    public int getSwendu() {
        return Swendu;
    }

    public void setSwendu(int swendu) {
        Swendu = swendu;
    }

    // 关闭串口
    public void closeSerialPort() {
        if (serialPort != null) {

            serialPort.notifyOnDataAvailable(false);
            serialPort.removeEventListener();

            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;

                } catch (IOException e) {
                    System.out.println("hhh");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                }
            }
            serialPort.close();
            serialPort = null;
        }
    }


    public static String Xor(String strHex_X, String strHex_Y) {

        //将x、y转成二进制形式

        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));

        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));

        String result = "";

        //判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {

                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }

        //异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            //如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i))
                result += "0";
            else {
                result += "1";
            }
        }
        return Integer.toHexString(Integer.parseInt(result, 2));
    }

    public static String checkcode(String para) {
        int length = para.length() / 2;
        String[] dateArr = new String[length];

        for (int i = 0; i < length; i++) {
            dateArr[i] = para.substring(i * 2, i * 2 + 2);
        }
        String code = "00";
        for (int i = 0; i < dateArr.length; i++) {
            code = Xor(code, dateArr[i]);
        }
        return code;
    }

    public double exchange(String string) {
        String a = string.substring(0, 2);
        String b = string.substring(2, 4);
        String temp = "";

        temp = a;
        a = b;
        b = temp;

        return Integer.parseInt(a + b, 16) / 100.00;
    }

    /*
     *bytes字符串转换为Byte值
     * @param String src Byte字符串，每个Byte之间没有分隔符
     */
    public static byte[] hexStr2Bytes(String src) {
        /* 对输入值进行规范化整理 */
        src = src.trim().replace(" ", "").toUpperCase();

        int m = 0, n = 0;
        int l = src.length() / 2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }

    //字节转换成十六进制字符串，无空格
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    //bytes转换成十六进制字符串 ,每个Byte值之间空格分隔
    public static String byte2HexStr(byte[] b) {
        String stmp = "";

        StringBuilder sb = new StringBuilder("");

        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);

            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);

            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    //同上，但可自定义长度
    public static String byte2HexStr(byte[] b, int len) {
        String stmp = "";
        StringBuilder sb = new StringBuilder();

        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    //16转4位2进制
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 4 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    public static void main(String[] args) {
        Rxtx_fish test1 = new Rxtx_fish();
        test1.init();

        MaplayoutThread map1 = test1.new MaplayoutThread();
        map1.start();

        //test1.readPort();
        //test1.sendMsg();
        //test1.closeSerialPort();
    }
}
