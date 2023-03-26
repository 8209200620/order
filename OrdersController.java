package edu.canteen.order.system.controller;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.canteen.order.system.pojo.OrderItem;
import edu.canteen.order.system.pojo.Result;
import edu.canteen.order.system.service.OrdersService;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/edu/orders")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	@PostMapping("add")
	public Result<String> add(@RequestBody List<Integer> ids){
		Result<String> result = new Result<String>();
		String data = ordersService.add(ids);
		result.setCode(200);
		result.setData(data);
		result.setMessage("生成订单成功，请等待服务员上菜");
		return result;
	}
	 
	@PostMapping("confirmPay")
	public Result<String> confirmPay(@Param("no")String no){
		Result<String> result = new Result<String>();
		String data = ordersService.confirmPay(no);
		result.setCode(200);
		result.setData(data);
		result.setMessage("确认支付成功");
		return result;
	}
	
	@GetMapping("list")
	public Result<Map<String,Object>> list(Integer page,Integer rows,Integer userId,Integer status){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Map<String, Object> data = ordersService.list(page, rows, userId,status);
		result.setCode(200);
		result.setData(data);
		return result;
	}
	
	@GetMapping("cancel/{id}")
	public Result<Map<String,Object>> cancel( @PathVariable("id") Integer id){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		ordersService.cancel(id);
		result.setCode(200);
		result.setMessage("取消订单成功");
		return result;
	}
	
	@GetMapping("delete/{id}")
	public Result<Map<String,Object>> delete( @PathVariable("id") Integer id){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		ordersService.delete(id);
		result.setCode(200);
		result.setMessage("删除成功");
		return result;
	}
	
	@GetMapping("collection/{id}")
	public Result<Map<String,Object>> collection( @PathVariable("id") Integer id){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		ordersService.collection(id);
		result.setCode(200);
		result.setMessage("确认收款成功");
		return result;
	}
	
	@GetMapping("detail")
	public Result<List<OrderItem>> detail(Integer id){
		Result<List<OrderItem>> result = new Result<List<OrderItem>>();
		List<OrderItem> data = ordersService.detail(id);
		result.setCode(200);
		result.setData(data);
		return result;
	}
	
	
	@PostMapping("setTableNo")
	public Result<String> setTableNo(Integer id,String tableNo){
		Result<String> result = new Result<String>();
		ordersService.setTableNo(id,tableNo);
		result.setCode(200);
		result.setMessage("设置餐桌号成功");
		return result;
	}
	
	
	@GetMapping("wait/up")
	public Result<Map<String,Object>> waitUp(Integer page,Integer rows){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Map<String, Object> data = ordersService.waitUp(page, rows);
		result.setCode(200);
		result.setData(data);
		return result;
	}
	
}
