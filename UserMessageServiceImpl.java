package edu.canteen.order.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.canteen.order.system.mapper.UserMapper;
import edu.canteen.order.system.mapper.UserMessageMapper;
import edu.canteen.order.system.pojo.User;
import edu.canteen.order.system.pojo.UserMessage;
import edu.canteen.order.system.service.UserMessageService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public Map<String, Object> list(Integer page, Integer rows, Integer userId) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		IPage<UserMessage> iPage = new Page<UserMessage>(page,rows);
		
		QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<UserMessage>();
		if(userId!= null) {
			queryWrapper.eq("to_user_id", userId);
		}
		queryWrapper.orderByDesc("is_read,create_time");
		IPage<UserMessage> pageInfo = this.baseMapper.selectPage(iPage, queryWrapper);
		List<UserMessage> data = pageInfo.getRecords();
		for (UserMessage userMessage : data) {
			User user = userMapper.selectById(userMessage.getToUserId());
			userMessage.setUser(user);
			userMessage.setDateStr(userMessage.getCreateTime().getTime()+"");
			if(!userMessage.getIsRead()) {
				userMessage.setIsRead(true);
				this.baseMapper.updateById(userMessage);
			}
		}
		result.put("data", pageInfo.getRecords());
		result.put("total", pageInfo.getTotal());
		result.put("pages", pageInfo.getPages());
		
		return result;
	}

}
