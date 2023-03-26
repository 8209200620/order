package edu.canteen.order.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.canteen.order.system.mapper.CartMapper;
import edu.canteen.order.system.mapper.CategoryMapper;
import edu.canteen.order.system.mapper.RecipeMapper;
import edu.canteen.order.system.pojo.Cart;
import edu.canteen.order.system.pojo.CartVo;
import edu.canteen.order.system.pojo.Category;
import edu.canteen.order.system.pojo.Recipe;
import edu.canteen.order.system.pojo.User;
import edu.canteen.order.system.service.CartService;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private RecipeMapper recipeMapper;;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public void add(Cart cart) {
		// TODO Auto-generated method stub
		Integer recipeId = cart.getRecipeId();
		QueryWrapper<Cart> cartWrapper = new QueryWrapper<Cart>();
		User user  = (User) session.getAttribute("user");
		cartWrapper.eq("recipe_id", recipeId).eq("is_delete", 0).eq("user_id", user.getId());
		Cart dbCart = this.baseMapper.selectOne(cartWrapper);
		
		if(dbCart != null) {
			dbCart.setNumber(dbCart.getNumber()+1);
			dbCart.setUpdateTime(new Date());
			this.updateById(dbCart);
		}else {
			cart.setCreateTime(new Date());
			cart.setNumber(1);
			cart.setUserId(user.getId());
			this.baseMapper.insert(cart);
		}
	}
	@Override
	public void sub(Cart cart) {
		Integer recipeId = cart.getRecipeId();
		QueryWrapper<Cart> cartWrapper = new QueryWrapper<Cart>();
		User user  = (User) session.getAttribute("user");
		cartWrapper.eq("recipe_id", recipeId).eq("is_delete", 0).eq("user_id", user.getId());
		Cart dbCart = this.baseMapper.selectOne(cartWrapper);
		
		dbCart.setNumber(dbCart.getNumber()-1);
		dbCart.setUpdateTime(new Date());
		this.updateById(dbCart);
	}

	@Override
	public void delete(Integer id) {
		Cart cart = new Cart();
		cart.setId(id);
		cart.setIsDelete(true);
		cart.setUpdateTime(new Date());
		baseMapper.updateById(cart);
	}

	@Override
	public Map<String, Object> list(Integer page, Integer rows, Integer userId) {
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		IPage<Cart> iPage = new Page<Cart>(page,rows);
		
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId).eq("is_delete", 0);
		IPage<Cart> pageInfo = this.baseMapper.selectPage(iPage, queryWrapper);
		List<Cart> data = pageInfo.getRecords();
		for (Cart cart : data) {
			Recipe recipe = recipeMapper.selectById(cart.getRecipeId());
			Category category = categoryMapper.selectById(recipe.getCategoryId());
			recipe.setCategory(category);
			cart.setRecipe(recipe);
		}
		result.put("data", pageInfo.getRecords());
		result.put("total", pageInfo.getTotal());
		result.put("pages", pageInfo.getPages());
		
		return result;
	}
	@Override
	public List<CartVo> getCartsByIds(Integer[] ids) {
		
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.in("id", Arrays.asList(ids));
		List<Cart> carts = this.baseMapper.selectList(queryWrapper);
		List<CartVo> data = new ArrayList<CartVo>();
		for (Cart cart : carts) {
			CartVo vo = new CartVo();
			vo.setId(cart.getId());
			vo.setNumber(cart.getNumber());
			vo.setRecipeId(cart.getRecipeId());
			vo.setUserId(cart.getUserId());
			Recipe recipe = recipeMapper.selectById(cart.getRecipeId());
			Category category = categoryMapper.selectById(recipe.getCategoryId());
			recipe.setCategory(category);
			vo.setSubTotal(cart.getNumber()*recipe.getPrice());
			vo.setRecipe(recipe);
			data.add(vo);
		}
		
		return data;
	}
}
