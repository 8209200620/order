package edu.canteen.order.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import edu.canteen.order.system.pojo.Orders;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 */
public interface OrdersMapper extends BaseMapper<Orders> {

	IPage<Orders> selectWaitUp(IPage<Orders> iPage);

}
