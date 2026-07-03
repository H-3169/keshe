package guat.lxy.bigdata.keshe.mapper;

import guat.lxy.bigdata.keshe.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM t_user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT r.role FROM t_user_role ur LEFT JOIN t_role r ON ur.role_id = r.id WHERE ur.user_id = #{userId}")
    List<String> findRolesByUserId(Integer userId);

    @Insert("INSERT INTO t_user(username, password, active) VALUES(#{username}, #{password}, #{active})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Insert("INSERT INTO t_user_role(user_id, role_id) VALUES(#{userId}, #{roleId})")
    int assignRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}