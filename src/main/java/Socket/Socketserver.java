package Socket;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import javax.servlet.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class Socketserver implements Filter {


    public static void main(String[] args) {
        NioSocketAcceptor acceptor = null;
        try {
            acceptor = new NioSocketAcceptor();
            acceptor.setHandler(new SocketHandler());
            acceptor.getFilterChain().addLast("Filterserver", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
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
            acceptor.getFilterChain().addLast("Filterserver", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
            acceptor.setReuseAddress(true);
            acceptor.bind(new InetSocketAddress(8989));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
