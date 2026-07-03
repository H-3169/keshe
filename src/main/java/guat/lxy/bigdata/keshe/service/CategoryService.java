package guat.lxy.bigdata.keshe.service;

import guat.lxy.bigdata.keshe.entity.Category;
import guat.lxy.bigdata.keshe.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CATEGORY_LIST_KEY = "category:list";

    public List<Category> getAllCategories() {
        if (redisTemplate != null) {
            Object cached = redisTemplate.opsForValue().get(CATEGORY_LIST_KEY);
            if (cached != null) {
                return (List<Category>) cached;
            }
        }
        List<Category> list = categoryMapper.findAll();
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(CATEGORY_LIST_KEY, list, 10, TimeUnit.MINUTES);
        }
        return list;
    }

    public Category getCategoryById(Integer id) {
        return categoryMapper.findById(id);
    }

    @Transactional
    public int addCategory(Category category) {
        int result = categoryMapper.insert(category);
        if (redisTemplate != null) {
            redisTemplate.delete(CATEGORY_LIST_KEY);
        }
        return result;
    }

    @Transactional
    public int updateCategory(Category category) {
        int result = categoryMapper.update(category);
        if (redisTemplate != null) {
            redisTemplate.delete(CATEGORY_LIST_KEY);
        }
        return result;
    }

    @Transactional
    public int deleteCategory(Integer id) {
        int result = categoryMapper.deleteById(id);
        if (redisTemplate != null) {
            redisTemplate.delete(CATEGORY_LIST_KEY);
        }
        return result;
    }
}