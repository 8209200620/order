package edu.canteen.order.system.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.canteen.order.system.interceptor.AdminInterceptor;
import edu.canteen.order.system.interceptor.UserInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${file.upload.path}")
	private String path;

	@Autowired
	private UserInterceptor userInterceptor;

	@Autowired
	private AdminInterceptor adminInterceptor;

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("file:" + path + "/")
				.addResourceLocations("classpath:/static/");
	}

	public void addInterceptors(InterceptorRegistry registry) {

		// admin interceptor
		List<String> exclude = new ArrayList<String>();
		exclude.add("/code");
		exclude.add("/admin/login");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**").excludePathPatterns(exclude);

		// user list
		List<String> paths = new ArrayList<String>();
		paths.add("/cart");
		paths.add("/orders");
		paths.add("/edu/cart/add");
		registry.addInterceptor(userInterceptor).addPathPatterns(paths);

	}

}
