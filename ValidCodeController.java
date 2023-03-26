package edu.canteen.order.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.canteen.order.system.util.CodeUtils;

@RestController
public class ValidCodeController {

	/**
	 * 验证码
	 * @param request
	 * @param response
	 */
	@GetMapping(value = "code")
	public void randomCode(HttpServletRequest request,HttpServletResponse response) {
		try {
			CodeUtils.createImageCode(request, response);
		} catch (Exception e) {
			System.err.println("验证码生成错误" + e);
		}
	}
}
