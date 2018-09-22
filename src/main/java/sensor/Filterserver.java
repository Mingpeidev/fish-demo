package sensor;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.logging.SocketHandler;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Filterserver implements Filter{
	
	
	 public static void main(String[] args) {
		 NioSocketAcceptor acceptor = null;
		 try {
	            acceptor = new NioSocketAcceptor();
	            acceptor.setHandler((IoHandler) new SocketHandler());
	            acceptor.getFilterChain().addLast("Filterserver", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName( "UTF-8" ))));
	            acceptor.setReuseAddress(true);
	            acceptor.bind(new InetSocketAddress(8989));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	
	//private Rxtx_fish rf=new Rxtx_fish();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("---------------哈哈哈-------------------");
		//rf.init();
		//chain.doFilter(request, response);
		NioSocketAcceptor acceptor = null;
		 
	        try {
	            acceptor = new NioSocketAcceptor();
	            acceptor.setHandler((IoHandler) new SocketHandler());
	            acceptor.getFilterChain().addLast("Filterserver", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName( "UTF-8" ))));
	            acceptor.setReuseAddress(true);
	            acceptor.bind(new InetSocketAddress(8989));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		

	}

}
