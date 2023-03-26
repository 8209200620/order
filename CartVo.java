package edu.canteen.order.system.pojo;

import lombok.Data;

@Data
public class CartVo {

    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 菜品id
     */
    private Integer recipeId;

    /**
     * 数量
     */
    private Integer number;

    private Recipe recipe;
    
    private Double subTotal;

	
}
