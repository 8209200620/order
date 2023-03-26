package edu.canteen.order.system.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import edu.canteen.order.system.pojo.Recipe;

/**
 * <p>
 * 菜品表 服务类
 * </p>
 */
public interface RecipeService extends IService<Recipe> {

	Map<String, Object> list(Integer page, Integer rows);

	List<Recipe> getRecipes(Integer returnSiz);

	List<Recipe> getRecipesByCategoryId(Integer id);

}
