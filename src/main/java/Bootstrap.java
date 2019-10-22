import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import system.proxy.ProxyServer;

public class Bootstrap {
    public static void main(String... args) {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        ConnectHandler proxy = new ConnectHandler();
        server.setHandler(proxy);

        ServletContextHandler contextHandler = new ServletContextHandler(proxy, "/", ServletContextHandler.NO_SESSIONS);
        contextHandler.addServlet(getProxyForRedirectPath("http://localhost:8080/"), "/api/unsecure/*");
        contextHandler.addServlet(getProxyForRedirectPath("https://localhost:8443/"), "/api/layer1/*");
        contextHandler.addServlet(getProxyForRedirectPath("https://localhost:9443/"), "/api/layer2/*");
        server.setHandler(contextHandler);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ServletHolder getProxyForRedirectPath(String path) {
        final ProxyServer servlet = new ProxyServer();
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setInitParameter("proxyTo", path);
        servletHolder.setInitParameter("prefix", "/");
        servletHolder.setInitParameter("responseBufferSize", Integer.toString(10 * 1024));
        servletHolder.setInitParameter("requestBufferSize", Integer.toString(10 * 1024));
        servletHolder.setInitParameter("maxConnections", Integer.toString(1024));
        servletHolder.setInitParameter("timeout", Integer.toString(60000));
        return servletHolder;
    }
}
