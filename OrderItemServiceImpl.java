package edu.canteen.order.system.service.impl;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.canteen.order.system.mapper.OrderItemMapper;
import edu.canteen.order.system.mapper.OrdersMapper;
import edu.canteen.order.system.mapper.RecipeMapper;
import edu.canteen.order.system.pojo.OrderItem;
import edu.canteen.order.system.pojo.Orders;
import edu.canteen.order.system.pojo.Recipe;
import edu.canteen.order.system.pojo.User;
import edu.canteen.order.system.service.OrderItemService;
import edu.canteen.order.system.util.MessageUtils;

/**
 * <p>
 * 订单项表 服务实现类
 * </p>
 *
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private OrdersMapper ordersMapper;
	
	@Autowired
	private RecipeMapper recipeMapper;
	
	@Override
	public void cook(Integer id) {
		OrderItem item = new OrderItem();
		User user = (User) session.getAttribute("admin");
		item.setId(id);
		item.setCookId(user.getId());
		item.setUpdateTime(new Date());
		item.setStatus(1);
		this.baseMapper.updateById(item);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void upRecipe(Integer id) {
		OrderItem item = new OrderItem();
		item.setId(id);
		item.setUpdateTime(new Date());
		item.setStatus(2);
		this.baseMapper.updateById(item);
		User user = (User) session.getAttribute("admin");
		OrderItem orderItem = this.baseMapper.selectById(id);
		Orders orders = ordersMapper.selectById(orderItem.getOrderId());
		Recipe recipe = recipeMapper.selectById(orderItem.getRecipeId());
		// 向服务员群发消息
		JSONObject serviceMessage = new JSONObject();
		serviceMessage.put("message", "厨师'"+user.getName()+"‘"+"已完成 "+orders.getOrderNo()+"订单中'"+recipe.getName()+"'菜品，请前往上菜");
		MessageUtils.sendMessage(JSONObject.toJSONString(serviceMessage), 3);
		
		// 向单个用户发送完成烹饪消息
		JSONObject userMessage = new JSONObject();
		userMessage.put("message", "您的'"+orders.getOrderNo()+"'订单中'"+recipe.getName()+"'菜品已完成烹饪，请等待服务员上菜");
		MessageUtils.sendMessageByUser(JSONObject.toJSONString(userMessage), orders.getUserId());;
		
		//入表
		MessageUtils.messageInDBByUserType(3, 2, serviceMessage.getString("message"));
		MessageUtils.messageInDBByUserId(orders.getUserId(), 2, userMessage.getString("message"));;
		
	}

	@Override
	public void finishUp(Integer id) {
		OrderItem item = new OrderItem();
		item.setId(id);
		item.setUpdateTime(new Date());
		item.setStatus(3);
		this.baseMapper.updateById(item);
		OrderItem orderItem = this.baseMapper.selectById(id);
		// 检查是否还存在未完成上菜的子订单，若不存在，直接修改主订单状态为2（上菜完成）
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<OrderItem>();
		queryWrapper.eq("order_id", orderItem.getOrderId()).ne("status", 3);
		Integer count = this.baseMapper.selectCount(queryWrapper);
		if(count == null || count == 0) {
			Orders orders = new Orders();
			orders.setId(orderItem.getOrderId());
			orders.setStatus(2);
			orders.setUpdateTime(new Date());
			this.ordersMapper.updateById(orders);
		}
	}

}
