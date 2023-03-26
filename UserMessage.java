package edu.canteen.order.system.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserMessage extends Model<UserMessage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型：1：下单消息，2：上菜消息
     */
    private Integer type;

    private Integer toUserId;

    private String content;

   
    private Date createTime;

    private Boolean isRead;
    
    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private String dateStr;

}
