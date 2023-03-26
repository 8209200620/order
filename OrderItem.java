package edu.canteen.order.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单项表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderItem extends Model<Cart> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 菜品id
     */
    private Integer recipeId;

    /**
     * 单价
     */
    private Double price;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 单项总和
     */
    private Double subTotal;

    /**
     * 是否删除，0:未删除，1:已删除
     */
    private Boolean isDelete;

    private Date createTime;

    private Date updateTime;

    /**
     * 状态：0:待上菜，1:上菜完成
     */
    private Integer status;
    
    @TableField(exist = false)
    private Recipe recipe;

    /**
     * 厨师id
     */
    private Integer cookId;
    
    /**
     * 上菜服务员id
     */
    private Integer upRecipeUserId;
}
