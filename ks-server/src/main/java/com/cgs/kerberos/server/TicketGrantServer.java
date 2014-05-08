package com.cgs.kerberos.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketGrantServer extends BaseServer {

	private static Logger logger = LoggerFactory.getLogger(TicketGrantServer.class);

	public TicketGrantServer(int port) {
		super(port);
	}

	public void run() {
		try {
			logger.info("Ticket granting server listening on port " + port);
			serverSocket = new ServerSocket(port);
			serverSocketSucessfullyOpened = true;
			while (!closed) {
				logger.info("Waiting to accept a new client.");
				Socket socket = serverSocket.accept();
				InetAddress inetAddress = socket.getInetAddress();
				logger.info("Connected to client at " + inetAddress);

				logger.info("Starting new socket node.");
				
				new Thread(new TGSHandler(socket)).start();
			}
		} catch (SocketException e) {
			if ("socket closed".equals(e.getMessage())) {
				logger.info("Audit server has been closed");
			} else {
				logger.info("Caught an SocketException", e);
			}
		} catch (IOException e) {
			logger.info("Caught an IOException", e);
		} catch (Exception e) {
			logger.error("Caught an unexpectged exception.", e);
		}

	}

}