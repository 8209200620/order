package edu.canteen.order.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.canteen.order.system.mapper.CartMapper;
import edu.canteen.order.system.mapper.CategoryMapper;
import edu.canteen.order.system.mapper.OrderItemMapper;
import edu.canteen.order.system.mapper.OrdersMapper;
import edu.canteen.order.system.mapper.RecipeMapper;
import edu.canteen.order.system.mapper.UserMapper;
import edu.canteen.order.system.pojo.Cart;
import edu.canteen.order.system.pojo.Category;
import edu.canteen.order.system.pojo.OrderItem;
import edu.canteen.order.system.pojo.Orders;
import edu.canteen.order.system.pojo.Recipe;
import edu.canteen.order.system.pojo.User;
import edu.canteen.order.system.service.OrdersService;
import edu.canteen.order.system.util.MessageUtils;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private RecipeMapper recipeMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String add(List<Integer> ids) {
		Orders orders = new Orders();
		User user = (User) session.getAttribute("user");
		orders.setOrderNo(new Date().getTime()+"");
		orders.setUserId(user.getId());
		orders.setStatus(0);
		orders.setCreateTime(new Date());
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.in("id", ids);
		List<Cart> carts = cartMapper.selectList(queryWrapper);
		
		// 总金额
		Double taotalPrice = 0D;
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (Cart cart : carts) {
			Recipe recipe = recipeMapper.selectById(cart.getRecipeId());
			taotalPrice += (cart.getNumber()*recipe.getPrice());
			OrderItem item = new OrderItem();
			item.setOrderId(orders.getId());
			item.setRecipeId(recipe.getId());
			item.setNumber(cart.getNumber());
			item.setPrice(recipe.getPrice());
			item.setSubTotal(cart.getNumber()*recipe.getPrice());
			item.setCreateTime(new Date());
			orderItems.add(item);
		}
		
		orders.setTotalPrice(taotalPrice);
		
		this.baseMapper.insert(orders);
		
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orders.getId());
			orderItemMapper.insert(orderItem);
		}
		cartMapper.delete(queryWrapper);
		// 发送消息
		JSONObject messageJson = MessageUtils.sendService(user.getName(), orders.getOrderNo());
		// 入表
		MessageUtils.messageInDBByUserType(3, 1, messageJson.getString("message"));
		return orders.getOrderNo();
	}


	@Override
	public String confirmPay(String no) {
		QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
		queryWrapper.eq("order_no", no);
		Orders orders = new Orders();
		orders.setOrderNo(no);
		orders.setStatus(1);
		orders.setUpdateTime(new Date());
		this.baseMapper.update(orders, queryWrapper);
		return no;
	}


	@Override
	public Map<String, Object> list(Integer page, Integer rows, Integer userId,Integer status) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		IPage<Orders> iPage = new Page<Orders>(page,rows);
		
		QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
		if(userId!= null) {
			
			queryWrapper.eq("user_id", userId);
		}
		if(status!= null) {
			queryWrapper.eq("status", status);
		}
		queryWrapper.eq("is_delete", 0);
		IPage<Orders> pageInfo = this.baseMapper.selectPage(iPage, queryWrapper);
		List<Orders> data = pageInfo.getRecords();
		for (Orders order : data) {
			User user = userMapper.selectById(order.getUserId());
			order.setUser(user);
		}
		result.put("data", pageInfo.getRecords());
		result.put("total", pageInfo.getTotal());
		
		return result;
	}


	@Override
	public void cancel(Integer id) {
		Orders orders = new Orders();
		orders.setId(id);
		orders.setStatus(3);
		orders.setUpdateTime(new Date());
		this.baseMapper.updateById(orders);
	}


	@Override
	public void delete(Integer id) {
		Orders orders = new Orders();
		orders.setId(id);
		orders.setIsDelete(true);
		orders.setUpdateTime(new Date());
		this.baseMapper.updateById(orders);
		
	}


	@Override
	public void collection(Integer id) {
		Orders orders = new Orders();
		orders.setId(id);
		orders.setStatus(2);
		orders.setUpdateTime(new Date());
		this.baseMapper.updateById(orders);
		
	}


	@Override
	public List<OrderItem> detail(Integer id) {
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<OrderItem>();
		queryWrapper.eq("order_id", id);
		List<OrderItem> items = orderItemMapper.selectList(queryWrapper);
		for (OrderItem orderItem : items) {
			Recipe recipe = recipeMapper.selectById(orderItem.getRecipeId());
			orderItem.setRecipe(recipe);
			Category category = categoryMapper.selectById(recipe.getCategoryId());
			recipe.setCategory(category);
		}
		return items;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void setTableNo(Integer id, String tableNo) {
		User user = (User) session.getAttribute("admin");
		Orders orders = new Orders();
		orders.setId(id);
		orders.setTableNo(tableNo);
		orders.setStatus(1);
		orders.setUpdateTime(new Date());
		orders.setSetUserId(user.getId());
		this.baseMapper.updateById(orders);
		// 发送消息
		JSONObject messageJson = MessageUtils.sendCook(user.getName());
		// 入表
		MessageUtils.messageInDBByUserType(2, 3, messageJson.getString("message"));
	}


	@Override
	public Map<String, Object> waitUp(Integer page, Integer rows) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		IPage<Orders> iPage = new Page<Orders>(page,rows);
		
		IPage<Orders> pageInfo = this.baseMapper.selectWaitUp(iPage);
		List<Orders> data = pageInfo.getRecords();
		for (Orders order : data) {
			User user = userMapper.selectById(order.getUserId());
			order.setUser(user);
		}
		result.put("data", pageInfo.getRecords());
		result.put("total", pageInfo.getTotal());
		
		return result;
	}
}
