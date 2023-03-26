package edu.canteen.order.system.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.canteen.order.system.pojo.Result;
import edu.canteen.order.system.pojo.UserMessage;
import edu.canteen.order.system.service.UserMessageService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/edu/user/message")
public class UserMessageController {

	@Autowired
	private UserMessageService userMessageService;
	
	@GetMapping("list")
	public Result<Map<String,Object>> list(Integer page,Integer rows,Integer userId){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Map<String, Object> data = userMessageService.list(page, rows, userId);
		result.setCode(200);
		result.setData(data);
		return result;
	}
	
	@GetMapping("count")
	public Result<Integer> count(Integer userId){
		Result<Integer> result = new Result<Integer>();
		QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<UserMessage>();
		queryWrapper.eq("to_user_id", userId).eq("is_read", 0);
		int count = userMessageService.count(queryWrapper);
		result.setCode(200);
		result.setData(count);
		return result;
	}
}
