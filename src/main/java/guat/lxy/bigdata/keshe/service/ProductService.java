package guat.lxy.bigdata.keshe.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import guat.lxy.bigdata.keshe.entity.Product;
import guat.lxy.bigdata.keshe.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    // ---------- 单体查询（使用 @Cacheable） ----------
    @Cacheable(value = "product", key = "#id", unless = "#result == null")
    public Product getProductById(Integer id) {
        return productMapper.findById(id);
    }

    // ---------- 更新商品：使用 @CachePut 直接更新缓存 ----------
    // ★★★ 关键：这个方法必须由外部（Controller）调用，才会触发 @CachePut ★★★
    @CachePut(value = "product", key = "#product.id")
    public Product updateProductWithCache(Product product) {
        // 更新数据库
        productMapper.update(product);
        // 清除列表缓存
        clearProductListCache();
        // 返回更新后的对象（从数据库查询确保数据一致）
        return productMapper.findById(product.getId());
    }

    // ---------- 删除商品：清除缓存 ----------
    @CacheEvict(value = "product", key = "#id")
    public void deleteProduct(Integer id) {
        productMapper.deleteById(id);
    }

    // ---------- 带缓存的删除 ----------
    @Transactional
    public int deleteProductWithCache(Integer id) {
        int result = productMapper.deleteById(id);
        deleteProduct(id);        // 清除单体缓存
        clearProductListCache();  // 清除列表缓存
        return result;
    }

    // ---------- 添加商品 ----------
    @Transactional
    public int saveProduct(Product product) {
        int result = productMapper.insert(product);
        clearProductListCache();
        return result;
    }

    // ---------- 分页列表（Redis 手动缓存） ----------
    private static final String PRODUCT_LIST_KEY = "product:list:";

    public PageInfo<Product> searchProducts(Integer pageNum, Integer pageSize,
                                            String keyword, Integer catId,
                                            Double minPrice, Double maxPrice) {
        String cacheKey = PRODUCT_LIST_KEY + pageNum + ":" + pageSize + ":"
                + keyword + ":" + catId + ":" + minPrice + ":" + maxPrice;

        if (redisTemplate != null) {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return (PageInfo<Product>) cached;
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.searchProducts(keyword, catId, minPrice, maxPrice);
        PageInfo<Product> pageInfo = new PageInfo<>(list);

        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(cacheKey, pageInfo, 5, TimeUnit.MINUTES);
        }
        return pageInfo;
    }

    // ---------- 清除列表缓存 ----------
    private void clearProductListCache() {
        if (redisTemplate != null) {
            Set<String> keys = redisTemplate.keys(PRODUCT_LIST_KEY + "*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }
}