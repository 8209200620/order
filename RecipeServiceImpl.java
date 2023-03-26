package edu.canteen.order.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.canteen.order.system.mapper.CategoryMapper;
import edu.canteen.order.system.mapper.RecipeMapper;
import edu.canteen.order.system.pojo.Category;
import edu.canteen.order.system.pojo.Recipe;
import edu.canteen.order.system.service.RecipeService;

/**
 * <p>
 * 菜品表 服务实现类
 * </p>
 *
 */
@Service
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public Map<String, Object> list(Integer page, Integer rows) {
		IPage<Recipe> ipage = new Page<Recipe>(page,rows);
		QueryWrapper<Recipe> queryWrapper = new QueryWrapper<Recipe>();
		queryWrapper.eq("is_delete", 0);
		IPage<Recipe> pageInfo = this.baseMapper.selectPage(ipage, queryWrapper);
		List<Recipe> data = pageInfo.getRecords();
		for (Recipe recipe : data) {
			Category category = categoryMapper.selectById(recipe.getCategoryId());
			recipe.setCategory(category);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", pageInfo.getRecords());
		result.put("total", pageInfo.getTotal());
		return result;
	}

	@Override
	public List<Recipe> getRecipes(Integer returnSiz) {
		QueryWrapper<Recipe> queryWrapper = new QueryWrapper<Recipe>();
		queryWrapper.eq("is_delete", 0);
		queryWrapper.orderByDesc("create_time");
		List<Recipe> recipes = null;
		if(returnSiz != null) {
			
			IPage<Recipe> iPage = new Page<Recipe>(1,returnSiz);
			IPage<Recipe> pageInfo = this.baseMapper.selectPage(iPage, queryWrapper);
			recipes = pageInfo.getRecords();
		}else {
			recipes = this.baseMapper.selectList(queryWrapper);
		}
		if(recipes != null) {
			
			for (Recipe recipe : recipes) {
				Category category = categoryMapper.selectById(recipe.getCategoryId());
				recipe.setCategory(category);
			}
		}
		
		return recipes;
	}

	
	@Override
	public List<Recipe> getRecipesByCategoryId(Integer id) {
		QueryWrapper<Recipe> queryWrapper = new QueryWrapper<Recipe>();
		queryWrapper.eq("is_delete", 0);
		if(id != null) {
			queryWrapper.eq("category_id", id);
		}
		queryWrapper.orderByDesc("create_time");
		List<Recipe> recipes = this.baseMapper.selectList(queryWrapper);
		for (Recipe recipe : recipes) {
			Category category = categoryMapper.selectById(recipe.getCategoryId());
			recipe.setCategory(category);
			
		}
		
		return recipes;
	}
}
