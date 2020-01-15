package com.elex.common.net.service.jetty;

import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.elex.common.net.INetServer;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 
 * @author mausmars
 *
 */
public class JettyServer implements INetServer {
	protected static final ILogger logger = XLogUtil.logger();

	private JettyNetConfig config;
	// --------------------------------------------
	private Server server;

	public void startup() {
		QueuedThreadPool threadPool = new QueuedThreadPool(config.getThreadPoolCount());
		server = new Server(threadPool);
		ServerConnector serverConnector = new ServerConnector(server);
		serverConnector.setPort(config.getPort());
		Connector[] connectors = { serverConnector };
		server.setConnectors(connectors);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

		// 设置servlet
		if (config.getServletMap() != null) {
			for (Entry<String, HttpServlet> entry : config.getServletMap().entrySet()) {
				context.addServlet(new ServletHolder(entry.getValue()), entry.getKey());
			}
		}

		// WebAppContext context = new WebAppContext();
		// context.setResourceBase("./WebContent");
		// context.setDescriptor("./WebContent/WEB-INF/web.xml");
		// context.setWar("WebContent");
		// context.setParentLoaderPriority(true);
		context.setContextPath(config.getContextPath());

		SessionHandler sessionHandler = context.getSessionHandler();
		sessionHandler.setMaxInactiveInterval(config.getMaxInactiveInterval());
		server.setHandler(context);
		try {
			server.start();
			// server.join();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void shutdown() {
		try {
			server.stop();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public NetServiceType getNetServiceType() {
		return NetServiceType.jetty;
	}

	@Override
	public String getIp() {
		return config.getHost();
	}

	@Override
	public int getPort() {
		return config.getPort();
	}

	@Override
	public void setConfig(Object config) {
		this.config = (JettyNetConfig) config;
	}
}
