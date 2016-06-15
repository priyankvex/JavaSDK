package io.cloudboost.util;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * 
 * @author cloudboost
 *
 */
public class CloudSocket{
	
	private static Socket socket;
	
	/**
	 * 
	 * Socket Initialization
	 * 
	 * @param url
	 */
	public static void init(String url){
		try {
			setSocket(IO.socket(url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static Socket getSocket() {
		return socket;
	}

	public static void setSocket(Socket socket) {
		CloudSocket.socket = socket;
		
	}
}
