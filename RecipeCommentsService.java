package edu.canteen.order.system.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import edu.canteen.order.system.pojo.CommentsVo;
import edu.canteen.order.system.pojo.RecipeComments;

/**
 * <p>
 * 菜品评论表 服务类
 * </p>
 *
 */
public interface RecipeCommentsService extends IService<RecipeComments> {

	void add(CommentsVo vo);

	Map<String, Object> list(Integer page, Integer rows, Integer recipeId);

	void delete(Integer id);

}
