package sensor;

import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class Rxtx_fish implements SerialPortEventListener {
	

    private static final String DEMONAME = "串口测试";
    /**
     * 检测系统中可用的端口
     */
    private CommPortIdentifier portId=null;
   
    private static InputStream inputStream;
    
    private static OutputStream outputStream;
 
    //RS-232的串行口
    private SerialPort serialPort;

    /**
     * 初始化
     * @param baudRate 波特率
     */
    public void init() {
    	
    	try {
    		portId=CommPortIdentifier.getPortIdentifier("COM6");
        	System.out.println("打开端口："+portId.getName());
            serialPort = (SerialPort) portId.open(DEMONAME,2000);
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
            outputStream=serialPort.getOutputStream();
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
            
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
        	break;
       //获取到有效信息
        case SerialPortEvent.DATA_AVAILABLE :            	
        	readPort();                
        	break;  
        	
        default:               
        	break;       
        }
    }

    /**
     * 读取串口信息
     */
    public void readPort() {
    	byte[] readBuffer = new byte[30];
        int availableBytes  = 0;
        
    	String rain="";
    	String light="";
    	String temp="";
    	String humi="";
    	String control="";
        
        try {
			while (true) {
				availableBytes = inputStream.available();
				while (availableBytes > 0) {
					inputStream.read(readBuffer);
					
					String x=bytesToHexString(readBuffer).substring(0, 10);
					String y=bytesToHexString(readBuffer).substring(10,14);
					
					//if(x.equals("02141800f1")&&y.equals("f002")){
					//	netaddress=bytesToHexString(readBuffer).substring(10,14);
					//	wuliaddress=bytesToHexString(readBuffer).substring(18,34);
					//}物理地址
					
					if(x.equals("02071800f1")&y.equals("ee61")){
						rain=bytesToHexString(readBuffer).substring(16,20);
						System.out.println("rain:"+rain);
					}
					if(x.equals("02071800f1")&y.equals("00a0")){
						control=bytesToHexString(readBuffer).substring(16,18);
						System.out.println("control:"+control);
					}
					if(x.equals("02081800f1")&&y.equals("d715")){
						light=bytesToHexString(readBuffer).substring(16,20);
						System.out.println("light:"+exchange(light));
					}
					
					if(x.equals("02081800f1")&&y.equals("478c")){
						if(bytesToHexString(readBuffer).substring(14, 16).equals("01")){
							temp=bytesToHexString(readBuffer).substring(16,20);
							System.out.println("temp:"+exchange(temp));
						}
						if(bytesToHexString(readBuffer).substring(14, 16).equals("02")){
							humi=bytesToHexString(readBuffer).substring(16,20);
							System.out.println("humi:"+exchange(humi));
						}
					}
					
					
					System.out.println(byte2HexStr(readBuffer));
					
					//更新循环条件
					availableBytes = inputStream.available();
				}
				Thread.sleep(0);
			} 
        }catch (IOException e) {
        	System.out.println("获取输出流失败");
        	System.exit(0);
            e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
  //向串口发送信息方法
  	public void sendMsg(){
  		
  		String info="";
  		String msg = "071800F100A00102";//要发送的命令
  		info="02"+msg+checkcode(msg);
  		System.out.println("info="+info+"  字符串："+hexStr2Bytes(info));

  		try {
  		    outputStream.write(hexStr2Bytes(info));
  		    outputStream.flush();
  		    System.out.println("输出成功");
  		} catch (IOException e) {
  			e.printStackTrace();
  			System.out.println("输出失败");
  		}
  	}

    /*
	 *bytes字符串转换为Byte值 
     * @param String src Byte字符串，每个Byte之间没有分隔符 
     * @return byte[] 
     */  
    public static byte[] hexStr2Bytes(String src)  
    {  
    	/* 对输入值进行规范化整理 */
		src = src.trim().replace(" ", "").toUpperCase();
		
        int m=0,n=0;  
        int l=src.length()/2;  
        System.out.println(l);  
        byte[] ret = new byte[l];  
        for (int i = 0; i < l; i++)  
        {  
            m=i*2+1;  
            n=m+1;  
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m)+ src.substring(m, n)) & 0xFF);
        }  
        return ret;  
    }  
    
  //字节转换成十六进制字符串
    public static String bytesToHexString(byte[] src){  
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
    public static String byte2HexStr(byte[] b)  

    {  
        String stmp="";  

        StringBuilder sb = new StringBuilder("");  

        for (int n=0;n<b.length;n++)  
        {  
            stmp = Integer.toHexString(b[n] & 0xFF);  

            sb.append((stmp.length()==1)? "0"+stmp : stmp);  

            sb.append(" ");  
        }  
        return sb.toString().toUpperCase().trim();  
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

 				}
 				catch (IOException e) {
 					System.out.println("hhh");
 				}
 			}
 			if (outputStream != null) {
 				try {
 					outputStream.close();
 					outputStream = null;
 				}
 				catch (IOException e) {}
 			}
 			serialPort.close();
 			serialPort = null;
 		}
 	}
 	

public static String Xor(String strHex_X,String strHex_Y){ 

			//将x、y转成二进制形式 

			String anotherBinary=Integer.toBinaryString(Integer.valueOf(strHex_X,16)); 

			String thisBinary=Integer.toBinaryString(Integer.valueOf(strHex_Y,16)); 

			String result = ""; 

			//判断是否为8位二进制，否则左补零 
			if(anotherBinary.length() != 8){ 
			for (int i = anotherBinary.length(); i <8; i++) { 

					anotherBinary = "0"+anotherBinary; 
				} 
			} 
			if(thisBinary.length() != 8){ 
			for (int i = thisBinary.length(); i <8; i++) { 
					thisBinary = "0"+thisBinary; 
				} 
			} 

			//异或运算 
			for(int i=0;i<anotherBinary.length();i++){ 
			//如果相同位置数相同，则补0，否则补1 
					if(thisBinary.charAt(i)==anotherBinary.charAt(i)) 
						result+="0"; 
					else{ 
						result+="1"; 
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

public double exchange(String string){
	String a=string.substring(0,2);
	String b=string.substring(2,4);
	String temp="";
	
	temp=a;
	a=b;
	b=temp;
	
	return Integer.parseInt(a+b,16)/100.00;
}


     public static void main(String[] args) {
    	Rxtx_fish test1=new Rxtx_fish();
    	test1.init();
    	//test1.readPort();
    	//test1.sendMsg();
        //test1.closeSerialPort();
    }
}
