package edu.canteen.order.system.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import edu.canteen.order.system.pojo.RegisterUser;
import edu.canteen.order.system.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface UserService extends IService<User> {

	User login(String account, String password, String validCode);

	String logout(String type);
	
	void add(User  user);

	User getUserById(Integer id);

	void update(User user);

	void updateCurrUserInfo(User user);

	Map<String, Object> getUsers(Integer page, Integer rows);

	void updatePassword(Integer id,String oldPassword, String password);

	void register(RegisterUser user);

	void delete(Integer id);

}
