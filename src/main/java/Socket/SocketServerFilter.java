package Socket;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import javax.servlet.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class SocketServerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("1111111111");
        response.setContentType("text/html;charset=" + "UTF-8");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
