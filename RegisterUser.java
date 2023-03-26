package edu.canteen.order.system.pojo;

import lombok.Data;

@Data
public class RegisterUser {

	private String account;
	private String password;
	private String name;
	private String mobile;
	private String smsCode;
	private String rePassword;
	
	
}
