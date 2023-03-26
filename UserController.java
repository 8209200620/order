package edu.canteen.order.system.controller;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.canteen.order.system.pojo.LoginUser;
import edu.canteen.order.system.pojo.RegisterUser;
import edu.canteen.order.system.pojo.Result;
import edu.canteen.order.system.pojo.User;
import edu.canteen.order.system.pojo.UserPassword;
import edu.canteen.order.system.service.UserService;
import edu.canteen.order.system.util.FileUploadUtils;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("edu/user")
public class UserController {

	@Value("${file.upload.path}")
	private String path;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 登陆
	 * @param account
	 * @param password
	 * @param validCode
	 * @param type
	 * @return
	 */
	@PostMapping("login")
	public Result<String> login(@RequestBody LoginUser loginUser){
		Result<String> result = new Result<String>();
		
		if(loginUser.getAccount() == null || "".equals(loginUser.getAccount())) {
			result.setCode(500);
			result.setMessage("帐号不能为空");
		}
		
		if(loginUser.getPassword() == null || "".equals(loginUser.getPassword())) {
			result.setCode(500);
			result.setMessage("密码不能为空");
		}
		if(loginUser.getSmsCode() == null || "".equals(loginUser.getSmsCode())) {
			result.setCode(500);
			result.setMessage("验证码不能为空");
		}
		
		User user = userService.login(loginUser.getAccount(),loginUser.getPassword(),loginUser.getSmsCode());
		result.setCode(200);
		result.setMessage("登陆成功");
		
		if(user.getType()== 1) {
			result.setData("/");
		}else {
			result.setData("/admin");
		}
		return result;
	}
	
	/**
	 * 退出
	 * @param type
	 * @return
	 */
	@GetMapping("logout")
	public Result<String> logout(String type){
		Result<String> result = new Result<String>();
		String path = userService.logout(type);
		result.setCode(200);
		result.setMessage("退出成功");
		result.setData(path);
		return result;
	}
	
	/**
	 * 普通用户注册
	 * @param user
	 * @return
	 */
	@PostMapping("register")
	public Result<String> register(@RequestBody RegisterUser user){
		Result<String> result = new Result<String>();
		
		userService.register(user);
		
		result.setCode(200);
		result.setMessage("注册成功");
		result.setData("/");
		return result;
	}
	
	/**
	 * 当前登陆用户修改个人信息
	 * @param id
	 * @return
	 */
	@GetMapping("{id}")
	public Result<User> getUserById(@PathVariable("id") Integer id){
		Result<User> result = new Result<User>();
		User user = userService.getUserById(id);
		result.setCode(200);
		result.setData(user);
		return result;
	}
	
	/**
	 * 修改个人信息
	 * @param user
	 * @return
	 */
	@PostMapping("updateCurrUser")
	public Result<Integer> updateCurrUser(@RequestBody User user){
		
		Result<Integer> result = new Result<Integer>();
		
		userService.updateCurrUserInfo(user);
		result.setCode(200);
		result.setMessage("修改成功");
		return result;
		
	}
	
	
	/**
	 * 修改密码
	 * @param id
	 * @param oldPassword
	 * @param password
	 * @return
	 */
	@PostMapping("updatePassword")
	public Result<String> updatePassword(@RequestBody UserPassword up){
		Result<String> result = new Result<String>();
		userService.updatePassword(up.getId(),up.getOldPassword(),up.getPassword());
		result.setCode(200);
		result.setMessage("修改密码成功，请重新登陆");
		result.setData("/");
		return result;
	}
	
	@GetMapping("list")
	public Result<Map<String,Object>> getUsers(Integer page,Integer rows){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Map<String, Object> data = userService.getUsers(page, rows);
		result.setCode(200);
		result.setData(data);
		return result;
	}
	
	
	@PostMapping("updateHeaderImage")
	public Result<String> updateHeaderImage(MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException{
		Result<String> result = new Result<String>();
		String tempPath;
		if(path.lastIndexOf("/") != -1) {
			tempPath = path + "";
		}else {
			tempPath = path +"/";
		}
		File saveFile = FileUploadUtils.saveFile(tempPath, file);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		user.setHeaderImage(saveFile.getName());
		userService.update(user);
		result.setCode(200);
		result.setData(saveFile.getName());
		result.setMessage("修改头像成功");
		return result;
	}
	
	
	@PostMapping("add")
	public Result<String> add(@RequestBody User user){
		Result<String> result = new Result<String>();
		userService.add(user);
		result.setCode(200);
		result.setMessage("新增成功");
		return result;
	}
	
	@PostMapping("update")
	public Result<String> update(@RequestBody User user){
		Result<String> result = new Result<String>();
		userService.update(user);
		result.setCode(200);
		result.setMessage("更新成功");
		return result;
	}
	
	@GetMapping("delete/{id}")
	public Result<String> delete(@PathVariable("id") Integer id){
		Result<String> result = new Result<String>();
		userService.delete(id);
		result.setCode(200);
		result.setMessage("删除成功");
		return result;
	}
	
}
