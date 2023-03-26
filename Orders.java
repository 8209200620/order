package edu.canteen.order.system.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Orders extends Model<Cart> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 总金额
     */
    private Double totalPrice;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 订单状态，0:用户下单，1:设置餐桌，2:完成上菜，3:支付完成
     */
    private Integer status;
    
    /**
     * 餐桌号
     */
    private String tableNo;
    /**
     * 设置餐桌号人
     */
    private Integer setUserId;

    /**
     * 是否删除，0:未删除，1:已删除
     */
    private Boolean isDelete;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    
    @TableField(exist = false)
    private User user;

    private Boolean isComment;

}
