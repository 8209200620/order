package edu.canteen.order.system.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import edu.canteen.order.system.pojo.Category;
import edu.canteen.order.system.pojo.Result;
import edu.canteen.order.system.service.CategoryService;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/edu/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("list")
	public Result<Map<String,Object>> list(Integer page,Integer rows,String name,Integer type){
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		IPage<Category> ipage = new Page<Category>(page,rows);
		QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
		queryWrapper.eq("is_delete", 0);
		IPage<Category> pageInfo = categoryService.page(ipage,queryWrapper);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", pageInfo.getRecords());
		data.put("total", pageInfo.getTotal());
		result.setCode(200);
		result.setData(data);
		return result;
	}
	
	@PostMapping("add")
	public Result<Integer> add(@RequestBody Category category){
		Result<Integer> result = new Result<Integer>();
		category.setCreateTime(new Date());
		categoryService.save(category);
		result.setCode(200);
		result.setMessage("新增成功");
		return result;
	}
	
	@PostMapping("update")
	public Result<Integer> update(@RequestBody Category category){
		Result<Integer> result = new Result<Integer>();
		category.setUpdateTime(new Date());
		categoryService.saveOrUpdate(category);
		result.setCode(200);
		result.setMessage("修改成功");
		return result;
	}
	
	@GetMapping("{id}")
	public Result<Category> getById(@PathVariable("id") Integer id){
		Result<Category> result = new Result<Category>();
		Category category = categoryService.getById(id);
		result.setCode(200);
		result.setData(category);
		return result;
	}
	
	@GetMapping("delete/{id}")
	public Result<Integer> delete(@PathVariable("id") Integer id){
		Result<Integer> result = new Result<Integer>();
		Category category = new Category();
		category.setId(id);
		category.setIsDelete(true);
		category.setCreateTime(new Date());
		categoryService.saveOrUpdate(category);
		result.setCode(200);
		result.setMessage("删除成功");
		return result;
	}
	
	@GetMapping("all")
	public Result<List<Category>> all(){
		Result<List<Category>> result = new Result<List<Category>>();
		QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
		queryWrapper.eq("is_delete", 0);
		List<Category> data = categoryService.list(queryWrapper);
		result.setCode(200);
		result.setData(data);
		return result;
	}
}
