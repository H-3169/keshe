package guat.lxy.bigdata.keshe.mapper;

import guat.lxy.bigdata.keshe.entity.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT p.*, c.name as categoryName FROM product p LEFT JOIN category c ON p.cat_id = c.id WHERE p.id = #{id}")
    Product findById(Integer id);

    @Select("<script>" +
            "SELECT p.*, c.name as categoryName FROM product p LEFT JOIN category c ON p.cat_id = c.id " +
            "<where> " +
            "   <if test='keyword != null and keyword != \"\"'> " +
            "       AND p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "   </if> " +
            "   <if test='catId != null'> " +
            "       AND p.cat_id = #{catId} " +
            "   </if> " +
            "   <if test='minPrice != null'> " +
            "       AND p.price &gt;= #{minPrice} " +
            "   </if> " +
            "   <if test='maxPrice != null'> " +
            "       AND p.price &lt;= #{maxPrice} " +
            "   </if> " +
            "</where> " +
            "ORDER BY p.id ASC " +
            "</script>")
    List<Product> searchProducts(@Param("keyword") String keyword,
                                 @Param("catId") Integer catId,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice);

    @Insert("INSERT INTO product(name, photo_url, price, descp, release_date, cat_id) " +
            "VALUES(#{name}, #{photoUrl}, #{price}, #{descp}, #{releaseDate}, #{catId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Update("UPDATE product SET name=#{name}, photo_url=#{photoUrl}, price=#{price}, " +
            "descp=#{descp}, release_date=#{releaseDate}, cat_id=#{catId} WHERE id=#{id}")
    int update(Product product);

    @Delete("DELETE FROM product WHERE id=#{id}")
    int deleteById(Integer id);
}