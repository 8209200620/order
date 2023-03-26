package edu.canteen.order.system.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.canteen.order.system.pojo.Result;

@RestControllerAdvice
public class GlobalExceptionController {

	@ExceptionHandler(Exception.class)
	public Result<Object> exception(Exception e){
		Result<Object> result = new Result<Object>();
		e.printStackTrace();
		result.setCode(500);
		result.setMessage("服务器出错，请联系系统管理员");
		
		return result;
	}
	@ExceptionHandler(RuntimeException.class)
	public Result<Object> runtimeException(Exception e){
		Result<Object> result = new Result<Object>();
		e.printStackTrace();
		result.setCode(500);
		result.setMessage(e.getMessage());
		return result;
	}
}
