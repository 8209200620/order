package edu.canteen.order.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.canteen.order.system.pojo.CartVo;
import edu.canteen.order.system.pojo.Category;
import edu.canteen.order.system.pojo.Recipe;
import edu.canteen.order.system.pojo.User;
import edu.canteen.order.system.service.CartService;
import edu.canteen.order.system.service.CategoryService;
import edu.canteen.order.system.service.RecipeService;

@Controller
public class ViewController {
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CartService cartService;
	
	@GetMapping("admin/login")
	public String loginAdmin() {
		return "admin/login";
	}
	
	@GetMapping(value = {"admin","admin/index"})
	public String index(HttpSession session) {
		User user = (User) session.getAttribute("admin");
		if(user.getType() == 3) {
			return "admin/orders-service";
		}
		if(user.getType() == 2) {
			return "admin/orders-cook";
		}
		
		return "admin/user";
	}
	@GetMapping(value = {"admin/user"})
	public String adminUser() {
		return "admin/user";
	}
	@GetMapping(value = {"admin/carousel"})
	public String carousel() {
		return "admin/carousel";
	}
	@GetMapping(value = {"admin/category"})
	public String category() {
		return "admin/category";
	}
	@GetMapping(value = {"admin/recipe"})
	public String recipe() {
		return "admin/recipe";
	}
	@GetMapping(value = {"admin/orders"})
	public String adOrders() {
		return "admin/orders";
	}
	@GetMapping(value = {"admin/comments"})
	public String adComments() {
		return "admin/comments";
	}
	@GetMapping(value = {"admin/orders-service"})
	public String ordersService() {
		return "admin/orders-service";
	}
	@GetMapping(value = {"admin/orders-cook"})
	public String ordersCook() {
		return "admin/orders-cook";
	}
	@GetMapping(value = {"admin/orders-wait-up"})
	public String ordersWaitUp() {
		return "admin/orders-wait-up";
	}
	
	@GetMapping(value = {"/","index"})
	public String index(HttpServletRequest request) {
		List<Category> menu = getMenu();
		request.setAttribute("menus", menu);
		List<Recipe> recipes = recipeService.getRecipes(12);
		request.setAttribute("recipes", recipes);
		return "index/index";
	}
	
	@GetMapping("recipe")
	public String recipe(HttpServletRequest request) {
		List<Category> menu = getMenu();
		request.setAttribute("menus", menu);
		List<Recipe> recipes = recipeService.getRecipes(null);
		request.setAttribute("recipes", recipes);
		return "index/recipe";
	}
	
	
	@GetMapping("login")
	public String login() {
		return "index/login";
	}
	@GetMapping("register")
	public String register() {
		return "index/register";
	}
	@GetMapping("recipe-detail")
	public String recipeDetail(HttpServletRequest request, Integer id) {
		List<Category> menu = getMenu();
		request.setAttribute("menus", menu);
		Recipe recipe = recipeService.getById(id);
		Category category = categoryService.getById(recipe.getCategoryId());
		
		recipe.setCategory(category);
		request.setAttribute("recipe", recipe);
		return "index/recipe-detail";
	}
	@GetMapping("cart")
	public String cat(HttpServletRequest request, Integer id) {
		List<Category> menu = getMenu();
		request.setAttribute("menus", menu);
		return "index/cart";
	}
	
	@GetMapping("pay-order")
	public String payOrder(HttpServletRequest request, Integer[] ids) {
		List<Category> menu = getMenu();
		request.setAttribute("menus", menu);
		List<CartVo> carts = cartService.getCartsByIds(ids);
		request.setAttribute("carts", carts);
		Double total = 0D;
		for (CartVo cartVo : carts) {
			total+=cartVo.getSubTotal();
		}
		request.setAttribute("total", total);
		return "index/pay-order";
	}
	
	
	@GetMapping("orders")
	public String orders(HttpServletRequest request ) {
		List<Category> menu = getMenu();
		request.setAttribute("menus", menu);
		return "index/orders";
	}
	
	private List<Category> getMenu(){
		QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
		queryWrapper.eq("is_delete", 0);
		List<Category> categorys = categoryService.list(queryWrapper);
		return categorys;
	}
	
	@GetMapping("category")
	public String indexCategory(HttpServletRequest request,Integer id) {
		List<Category> menu = getMenu();
		request.setAttribute("menus", menu);
		List<Recipe> recips = recipeService.getRecipesByCategoryId(id);
		request.setAttribute("recipes", recips);
		request.setAttribute("id", id);
		
		return "index/category";
	}
}
