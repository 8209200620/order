package edu.canteen.order.system.pojo;

import lombok.Data;

@Data
public class UserPassword {

	private Integer id;
	private String oldPassword;
	private String password;
}
