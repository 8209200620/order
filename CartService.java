package edu.canteen.order.system.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import edu.canteen.order.system.pojo.Cart;
import edu.canteen.order.system.pojo.CartVo;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 */
public interface CartService extends IService<Cart> {

	void add(Cart cart);

	void delete(Integer id);

	Map<String, Object> list(Integer page, Integer rows, Integer userId);

	void sub(Cart cart);

	List<CartVo> getCartsByIds(Integer[] ids);

}
