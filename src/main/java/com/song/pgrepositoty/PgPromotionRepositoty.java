package com.song.pgrepositoty;

import com.song.entity.User;
import com.song.pgentity.PgPromotion;
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
public interface PgPromotionRepositoty extends JpaRepository<PgPromotion,Long> {

    @Query("select t from PgPromotion t where t.title = :title")
    PgPromotion findByUserName(@Param("title") String title);

    @Query(value = "insert into test(title,content) values(?,?)", nativeQuery = true)
    @Transactional
    @Modifying
    int addPgPromotion(@Param("title") String title,@Param("content") String content);
}