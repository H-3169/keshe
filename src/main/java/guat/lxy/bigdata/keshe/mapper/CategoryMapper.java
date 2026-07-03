package guat.lxy.bigdata.keshe.mapper;

import guat.lxy.bigdata.keshe.entity.Category;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CategoryMapper {
    @Select("SELECT * FROM category ORDER BY id")
    List<Category> findAll();

    @Select("SELECT * FROM category WHERE id = #{id}")
    Category findById(Integer id);

    @Insert("INSERT INTO category(name, descp) VALUES(#{name}, #{descp})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);

    @Update("UPDATE category SET name=#{name}, descp=#{descp} WHERE id=#{id}")
    int update(Category category);

    @Delete("DELETE FROM category WHERE id=#{id}")
    int deleteById(Integer id);
}