package edu.canteen.order.system.pojo;

import lombok.Data;

@Data
public class LoginUser {

	private String account;
	private String password;
	private String smsCode;
}
