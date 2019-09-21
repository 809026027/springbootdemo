package com.song.mapper;

import com.song.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Mybatis数据库操作类
 * Created by 17060342 on 2019/6/4.
 */
@Mapper
public interface UserMapper {
    @Select("select * from tbl_user")
    List<User> findAllUsers();

    @Select("select * from tbl_user where name = #{name}")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    User findUserByName(@Param("name") String name);

    @Insert("insert into tbl_user(name,password,version) values(#{name},#{password},#{version})")
    int addUser(@Param("name") String name, @Param("password") String password, @Param("version") String version);

    @Update("<script>update tbl_user set name = #{name},password = #{password},version=version+1 where id=#{id}" +
            "<when test='version!=null'>" +
            " and version = #{version} " +
            "</when>" +
            "</script>")
    int updateUser(@Param("id") String id, @Param("name") String name, @Param("password") String password,@Param("version") String version);

    @Delete("delete from tbl_user where id=#{id}")
    int deleteUser(@Param("id") String id);
}
