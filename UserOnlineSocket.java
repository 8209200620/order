package edu.canteen.order.system.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;
@Component
@ServerEndpoint("/webSocket/user/online")
public class UserOnlineSocket {

	private static Map<Integer,Map<String,Object>> online = new HashMap<Integer, Map<String,Object>>();
	
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
	}

	@OnClose
	public void onClose(Session session) throws IOException {
	}

	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
	}
	
	@OnMessage
	public  void onMessage(String msg,Session session) throws IOException {
		if(msg == null || "".equals(msg)) {
			return ;
		}
		Integer userId = Integer.parseInt(msg);
		Map<String, Object> map = online.get(userId);
		if(map == null) {
			map = new HashMap<String, Object>();
			online.put(userId, map);
		}
		System.err.println(session);
		System.err.println(map);
		map.put("session", session);
		System.err.println(online);
	}

	public static Map<Integer,Map<String,Object>> getOnline(){
		return online;
	}
}
