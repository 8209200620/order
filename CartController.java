package edu.canteen.order.system.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.canteen.order.system.pojo.Cart;
import edu.canteen.order.system.pojo.Result;
import edu.canteen.order.system.service.CartService;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/edu/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@PostMapping("add")
	public Result<Integer> add(@RequestBody Cart cart){
		Result<Integer> result = new Result<Integer>();
		
		cartService.add(cart);
		
		result.setCode(200);
		result.setMessage("添加成功");
		return result;
	}
	
	@PostMapping("sub")
	public Result<Integer> sub(@RequestBody Cart cart){
		Result<Integer> result = new Result<Integer>();
		
		cartService.sub(cart);
		
		result.setCode(200);
		return result;
	}
	
	@GetMapping("list")
	public Result<Map<String,Object>> list(Integer page,Integer rows,Integer userId){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Map<String, Object> data = cartService.list(page, rows, userId);
		result.setCode(200);
		result.setData(data);
		return result;
	}
	
	@GetMapping("delete/{id}")
	public Result<Integer> delete(@PathVariable("id") Integer id){
		Result<Integer> result = new Result<Integer>();
		cartService.delete(id);
		result.setCode(200);
		result.setMessage("删除成功");
		return result;
	}
	
}
