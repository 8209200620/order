package edu.canteen.order.system.pojo;

import lombok.Data;

@Data
public class CommentsVo {

	private Integer orderId;
	
	private Integer score;
	
	private String content;
	
	
}
