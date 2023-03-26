package edu.canteen.order.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.canteen.order.system.mapper.UserMapper;
import edu.canteen.order.system.pojo.RegisterUser;
import edu.canteen.order.system.pojo.User;
import edu.canteen.order.system.service.UserService;
import edu.canteen.order.system.socket.UserOnlineSocket;
import edu.canteen.order.system.util.MD5Util;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	
	
	@Autowired
	private HttpSession session;

	/**
	 * 用户登陆
	 */
	public User login(String account, String password, String validCode) {
		// 获取session中的验证码
		String sessionCode = (String) session.getAttribute("smsCode");

		// 比对验证码是否正确
		if (!sessionCode.equalsIgnoreCase(validCode)) {
			throw new RuntimeException("验证码错误");
		}
		
		QueryWrapper<User> wrapper = new QueryWrapper<User>();
		wrapper.eq("account", account);
		wrapper.eq("is_delete", 0);
		User user = this.baseMapper.selectOne(wrapper);

		// 判断数据该用户是否存在
		if (user == null) {
			// 不存在，抛出异常
			throw new RuntimeException("帐号错误");
		}
		// 判断密码是否正确
		if (!user.getPassword().equals(MD5Util.stringToMD5(password))) {
			// 错误，抛出异常
			throw new RuntimeException("密码错误");
		}
		if(user.getType() == 1) {
			session.removeAttribute("admin");
			session.setAttribute("user", user);
		}else {
			session.removeAttribute("user");
			session.setAttribute("admin", user);
		}
		
		Map<Integer, Map<String, Object>> online = UserOnlineSocket.getOnline();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("user", user);
		online.put(user.getId(), map);
		
		return user;
	}

	/**
	 * 退出
	 */
	public String logout(String type) {
		User user = (User)session.getAttribute("user");
		if(user == null) {
			session.removeAttribute("admin");
			return "/admin";
		}else {
			session.removeAttribute("user");
			return "/";
		}
	}

	

	/**
	 * 新增用户
	 */
	public void add(User user) {
		if(user.getHeaderImage() == null || "".equals(user.getHeaderImage())) {
			user.setHeaderImage("face.jpg");
		}
		user.setPassword(MD5Util.stringToMD5(user.getPassword()));
		user.setCreateTime(new Date());
		this.baseMapper.insert(user);
	}

	/**
	 * 更新用户信息
	 */
	public void update(User user) {
		user.setUpdateTime(new Date());
		User dbUser = this.baseMapper.selectById(user);
		if(user.getPassword()!= null && !user.getPassword().equals(dbUser.getPassword())) {
			user.setPassword(MD5Util.stringToMD5(user.getPassword()));
		}
		this.baseMapper.updateById(user);
	}

	/**
	 * 修改个人信息
	 */
	public void updateCurrUserInfo(User user) {
		// 更新数据库对应的信息
		update(user);

		User bdUser = getUserById(user.getId());
		// 将新的用户信息放入sesion中
		
		if(bdUser.getType() == 0) {
			
			session.setAttribute("admin", bdUser);
		}else{
			
			session.setAttribute("user", bdUser);
		}
		

	}

	/**
	 * 用户列表
	 */
	public Map<String, Object> getUsers(Integer page, Integer rows) {
		IPage<User> pageInfo = new Page<User>(page,rows);
		QueryWrapper<User> wrapper = new QueryWrapper<User>();
		wrapper.eq("is_delete", 0).ne("type", 0);
		IPage<User> pageData = this.baseMapper.selectPage(pageInfo, wrapper);
		
		List<User> datas = pageData.getRecords();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", datas);
		resultMap.put("total", pageData);
		return resultMap;
	}

	public User getUserById(Integer id) {
		return this.baseMapper.selectById(id);
	}

	/**
	 * 修改密码
	 */
	public void updatePassword(Integer id, String oldPassword, String password) {
		User user = this.baseMapper.selectById(id);
		// 判断旧密码是否正确
		if (!user.getPassword().equals(MD5Util.stringToMD5(oldPassword))) {
			throw new RuntimeException("旧密码不正确");
		}
		
		user.setPassword(MD5Util.stringToMD5(password));
		this.baseMapper.updateById(user);
		
		User dbUser = this.baseMapper.selectById(id);
		if((dbUser.getType() == 0)) {
			session.removeAttribute("admin");
		}else {
			
			session.removeAttribute("user");
		}
	}

	/**
	 * 注册、只注册普通用户,
	 */
	public void register(RegisterUser registerUser) {
		// 获取session中的验证码
		String sessionCode = (String) session.getAttribute("smsCode");

		// 比对验证码是否正确
		if (!sessionCode.equalsIgnoreCase(registerUser.getSmsCode())) {
			// 验证码错误，抛出异常
			throw new RuntimeException("验证码错误");
		}

		// 判断两次密码是否一致
		if (!registerUser.getPassword().equals(registerUser.getRePassword())) {
			// 两次密码不一致
			throw new RuntimeException("两次密码不一致");
		}
		User user = new User();
		
		user.setAccount(registerUser.getAccount());
		user.setName(registerUser.getName());
		user.setMobile(registerUser.getMobile());
		user.setAccount(registerUser.getAccount());
		user.setPassword(registerUser.getPassword());
		QueryWrapper<User> wrapper = new QueryWrapper<User>();
		wrapper.eq("account", registerUser.getAccount()).eq("is_delete", 0);
		User dbUser = this.baseMapper.selectOne(wrapper);
		if(dbUser != null ) {
			throw new RuntimeException("该用户已存在");
		}
		// 将该用户信息保存到数据库中
		add(user);

	}

	@Override
	public void delete(Integer id) {
		User user = new User();
		user.setId(id);
		user.setIsDelete(true);
		update(user);
	}
	
}
