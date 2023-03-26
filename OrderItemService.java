package edu.canteen.order.system.service;

import com.baomidou.mybatisplus.extension.service.IService;

import edu.canteen.order.system.pojo.OrderItem;

/**
 * <p>
 * 订单项表 服务类
 * </p>
 *
 */
public interface OrderItemService extends IService<OrderItem> {

	void cook(Integer id);

	void upRecipe(Integer id);

	void finishUp(Integer id);

}
