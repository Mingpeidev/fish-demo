package Socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Socketserver implements Filter{
	
	
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

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		 NioSocketAcceptor acceptor = null;
		 try {
	            acceptor = new NioSocketAcceptor();
	            acceptor.setHandler(new SocketHandler());
	            acceptor.getFilterChain().addLast("Filterserver", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName( "UTF-8" ))));
	            acceptor.setReuseAddress(true);
	            acceptor.bind(new InetSocketAddress(8989));
	        } catch (Exception e) {
	            e.printStackTrace();
		
	}}

}
