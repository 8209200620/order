package edu.canteen.order.system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.canteen.order.system.pojo.Result;
import edu.canteen.order.system.service.OrderItemService;

/**
 * <p>
 * 订单项表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/edu/order/item")
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;
	
	@PostMapping("cook")
	public Result<Integer> cook(Integer id){
		Result<Integer> result = new Result<Integer>();
		orderItemService.cook(id);
		result.setCode(200);
		result.setMessage("设置菜品烹饪中成功");
		return result;
	}
	
	@PostMapping("upRecipe")
	public Result<Integer> upRecipe(Integer id){
		Result<Integer> result = new Result<Integer>();
		orderItemService.upRecipe(id);
		result.setCode(200);
		result.setMessage("设置上菜成功");
		return result;
	}
	
	@PostMapping("finishUp")
	public Result<Integer> finishUp(Integer id){
		Result<Integer> result = new Result<Integer>();
		orderItemService.finishUp(id);
		result.setCode(200);
		result.setMessage("完成上菜成功");
		return result;
	}
}
