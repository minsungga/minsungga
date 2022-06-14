package test;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class MyServer {

	public static void main(String[] args) throws Exception {
		new MyServer().start();
	}

	public void start() throws Exception {
		Server server = new Server();
		
		// Proxy-1
		ServerConnector http1 = new ServerConnector(server);
		http1.setHost("127.0.0.1");
		http1.setPort(5001);
		
		// Proxy-2
		ServerConnector http2 = new ServerConnector(server);
		http2.setHost("127.0.0.1");
		http2.setPort(5002);
		
		Connector[] connectors = {http1, http2};
		
		server.setConnectors(connectors);
		
//		server.addConnector(http);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyServlet.class, "/*");
		
		server.setHandler(servletHandler);

		server.start();
		server.join();
	}
}
