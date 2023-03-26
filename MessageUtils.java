package edu.canteen.order.system.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.canteen.order.system.mapper.UserMapper;
import edu.canteen.order.system.mapper.UserMessageMapper;
import edu.canteen.order.system.pojo.User;
import edu.canteen.order.system.pojo.UserMessage;
import edu.canteen.order.system.service.UserService;
import edu.canteen.order.system.socket.UserOnlineSocket;

/**
 * 
 *消息发送工具类
 */
public class MessageUtils {

	/**
	 * 向所有在线服务员发送订单消息
	 * @param username
	 * @param orderNo
	 */
	public static JSONObject sendService(String username,String orderNo) {
		Map<Integer, Map<String, Object>> online = UserOnlineSocket.getOnline();
		System.err.println("online = "+online);
		UserService userService = ApplicationContextUtil.getApplicationContext().getBean(UserService.class);
		JSONObject result = new JSONObject();
		result.put("message", "用户'"+username+"'"+"已提交菜品，请前往设置就餐桌号，单号:"+orderNo);
		for (Map.Entry<Integer, Map<String, Object>> entry : online.entrySet()) {
			Session session = (Session) entry.getValue().get("session");
			System.err.println(session);
			User user = (User) entry.getValue().get("user");
			if(user == null) {
				user = userService.getUserById(entry.getKey());
			}
			if(session != null &&session.isOpen() && user.getType() == 3) {
				try {
					session.getAsyncRemote().sendText(JSONObject.toJSONString(result));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
		
	}
	
	/**
	 * 向所有在线厨师发送菜品消息
	 * @param username
	 */
	public static JSONObject sendCook(String username) {
		UserService userService = ApplicationContextUtil.getApplicationContext().getBean(UserService.class);
		Map<Integer, Map<String, Object>> online = UserOnlineSocket.getOnline();
		JSONObject result = new JSONObject();
		result.put("message", "服务员'"+username+"'"+"已设置餐桌，请前往领取烹饪菜品");
		for (Map.Entry<Integer, Map<String, Object>> entry : online.entrySet()) {
			Session session = (Session) entry.getValue().get("session");
			User user = (User) entry.getValue().get("user");
			if(user == null) {
				user = userService.getUserById(entry.getKey());
			}
			if(session != null && session.isOpen() && user.getType() == 2) {
				session.getAsyncRemote().sendText(JSONObject.toJSONString(result));
			}
		}
		return result;
	}
	
	/**
	 * 通用发送群消息
	 * @param message 消息内容
	 * @param type 用户类型
	 */
	public static void sendMessage(String message,Integer type) {
		UserService userService = ApplicationContextUtil.getApplicationContext().getBean(UserService.class);
		Map<Integer, Map<String, Object>> online = UserOnlineSocket.getOnline();
		for (Map.Entry<Integer, Map<String, Object>> entry : online.entrySet()) {
			Session session = (Session) entry.getValue().get("session");
			User user = (User) entry.getValue().get("user");
			if(user == null) {
				user = userService.getUserById(entry.getKey());
			}
			if(session != null && session.isOpen() && user.getType() == type) {
				session.getAsyncRemote().sendText(message);
			}
		}
	}
	
	/**
	 * 通用发送单个消息
	 * @param message 消息内容
	 * @param type 用户类型
	 */
	public static void sendMessageByUser(String message,Integer userId) {
		Map<Integer, Map<String, Object>> online = UserOnlineSocket.getOnline();
		Map<String, Object> map = online.get(userId);
		if(map != null) {
			Session session = (Session) map.get("session");
			if(session != null && session.isOpen()) {
				session.getAsyncRemote().sendText(message);
			}
		}
	}
	
	/**
	 * 消息入表(群体消息)
	 * @param userType 用户类型
	 * @param messageType 消息类型，1:下单消息，2:上菜消息,3:烹饪消息
	 * @param content
	 */
	public static void messageInDBByUserType(Integer userType,Integer messageType,String content) {
		UserMapper userMapper = ApplicationContextUtil.getApplicationContext().getBean(UserMapper.class);
		UserMessageMapper userMessageMapper = ApplicationContextUtil.getApplicationContext().getBean(UserMessageMapper.class);
		QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
		userQueryWrapper.eq("type", userType).eq("is_delete", 0);
		List<User> users = userMapper.selectList(userQueryWrapper);
		for (User user : users) {
			UserMessage message = new UserMessage();
			message.setType(messageType);
			message.setContent(content);
			message.setToUserId(user.getId());
			message.setCreateTime(new Date());
			userMessageMapper.insert(message);
		}
	}
	
	/**
	 * 消息入表(单个消息)
	 * @param userType 用户类型
	 * @param messageType 消息类型，1:下单消息，2:上菜消息,3:烹饪消息
	 * @param content
	 */
	public static void messageInDBByUserId(Integer userId,Integer messageType,String content) {
		UserMapper userMapper = ApplicationContextUtil.getApplicationContext().getBean(UserMapper.class);
		UserMessageMapper userMessageMapper = ApplicationContextUtil.getApplicationContext().getBean(UserMessageMapper.class);
		QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
		userQueryWrapper.eq("id", userId).eq("is_delete", 0);
		User user = userMapper.selectOne(userQueryWrapper);
		UserMessage message = new UserMessage();
		message.setType(messageType);
		message.setContent(content);
		message.setToUserId(user.getId());
		message.setCreateTime(new Date());
		userMessageMapper.insert(message);
	}
}
