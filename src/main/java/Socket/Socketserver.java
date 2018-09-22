package Socket;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Socketserver{
	
	
	 public static void main(String[] args) {
		 NioSocketAcceptor acceptor = null;
		 try {
	            acceptor = new NioSocketAcceptor();
	            acceptor.setHandler(new SocketHandler());
	            acceptor.getFilterChain().addLast("Filterserver", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName( "UTF-8" ))));
	            acceptor.setReuseAddress(true);
	            acceptor.bind(new InetSocketAddress(8989));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

}
