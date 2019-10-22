package system.proxy;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class ProxyServer extends ProxyServlet.Transparent {
    @Override
    protected HttpClient newHttpClient() {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        sslContextFactory.setKeyStorePath("/Users/rahul.chandna/Desktop/jssecacerts");
        sslContextFactory.setKeyStorePassword("changeit");
        sslContextFactory.setKeyStoreType("JKS");
        return new HttpClient(sslContextFactory);
    }
}