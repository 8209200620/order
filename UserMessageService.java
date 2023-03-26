package edu.canteen.order.system.service;

import edu.canteen.order.system.pojo.UserMessage;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface UserMessageService extends IService<UserMessage> {

	Map<String, Object> list(Integer page, Integer rows, Integer userId);

}
