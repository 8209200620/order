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
 * 菜品表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Recipe extends Model<Recipe> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 图片
     */
    private String image;

    /**
     * 份量
     */
    private String weightf;

    /**
     * 单价
     */
    private Double price;
    
    /**
     * 推荐系数
     */
    private Integer recommendScore;
    

    /**
     * 口味
     */
    private String taste;

    /**
     * 食材
     */
    private String food;

    /**
     * 制作方式
     */
    private String makeWay;

    /**
     * 提示
     */
    private String prompt;

    /**
     * 是否删除，0:未删除，1:已删除
     */
    private Boolean isDelete;

    private Date createTime;

    private Date updateTime;
    
    @TableField(exist = false)
    private Category category;
    
    @TableField(exist = false)
    private Double commentsScoreSum;
    @TableField(exist = false)
    private Integer commentsCount;

}
