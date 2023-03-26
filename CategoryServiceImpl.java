package edu.canteen.order.system.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.canteen.order.system.mapper.CategoryMapper;
import edu.canteen.order.system.pojo.Category;
import edu.canteen.order.system.service.CategoryService;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
