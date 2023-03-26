package edu.canteen.order.system.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import edu.canteen.order.system.pojo.OrderItem;
import edu.canteen.order.system.pojo.Orders;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 */
public interface OrdersService extends IService<Orders> {

	String add(List<Integer> ids);

	String confirmPay(String no);

	Map<String, Object> list(Integer page, Integer rows, Integer userId,Integer status);

	void cancel(Integer id);

	void delete(Integer id);

	void collection(Integer id);

	List<OrderItem> detail(Integer id);

	void setTableNo(Integer id, String tableNo);

	Map<String, Object> waitUp(Integer page, Integer rows);


}
