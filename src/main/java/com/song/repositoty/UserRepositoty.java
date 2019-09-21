package com.song.repositoty;

import com.song.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hibernate数据库操作类
 * Created by feng on 2019/5/26.
 */
@Repository
public interface UserRepositoty extends JpaRepository<User,Long> {

    @Query("select t from User t where t.name = :name")
    User findByUserName(@Param("name") String name);

    @Query(value = "insert into tbl_user(name,password) values(?,?)", nativeQuery = true)
    @Transactional
    @Modifying
    int addUser(@Param("name") String name,@Param("password") String password);

    @Query("update User u set u.name = :name,u.password = :password where u.id=:id")
    @Transactional
    @Modifying
    int updateUser(@Param("id") Long id,@Param("name") String name,@Param("password") String password);

    @Query("delete from User u where u.id=:id")
    @Transactional
    @Modifying
    int deleteUser(@Param("id") Long id);
}