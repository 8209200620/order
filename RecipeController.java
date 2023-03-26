package edu.canteen.order.system.controller;


import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.canteen.order.system.pojo.Recipe;
import edu.canteen.order.system.pojo.Result;
import edu.canteen.order.system.service.RecipeService;

/**
 * <p>
 * 菜品表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/edu/recipe")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;
	
	@GetMapping("list")
	public Result<Map<String,Object>> list(Integer page,Integer rows,String name,Integer type){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Map<String, Object> data = recipeService.list(page,rows);
		result.setCode(200);
		result.setData(data);
		return result;
	}
	
	@PostMapping("add")
	public Result<Integer> add(@RequestBody Recipe recipe){
		Result<Integer> result = new Result<Integer>();
		recipeService.save(recipe);
		result.setCode(200);
		result.setMessage("新增成功");
		return result;
	}
	
	@PostMapping("update")
	public Result<Integer> update(@RequestBody Recipe recipe){
		Result<Integer> result = new Result<Integer>();
		recipe.setUpdateTime(new Date());
		recipeService.saveOrUpdate(recipe);
		result.setCode(200);
		result.setMessage("修改成功");
		return result;
	}
	
	@GetMapping("{id}")
	public Result<Recipe> getById(@PathVariable("id") Integer id){
		Result<Recipe> result = new Result<Recipe>();
		Recipe recipe = recipeService.getById(id);
		result.setCode(200);
		result.setData(recipe);
		return result;
	}
	
	@GetMapping("delete/{id}")
	public Result<Integer> delete(@PathVariable("id") Integer id){
		Result<Integer> result = new Result<Integer>();
		Recipe recipe = new Recipe();
		recipe.setId(id);
		recipe.setIsDelete(true);
		recipe.setCreateTime(new Date());
		recipeService.saveOrUpdate(recipe);
		result.setCode(200);
		result.setMessage("删除成功");
		return result;
	}
	
}
