package com.song.pgmapper;

import com.github.pagehelper.Page;
import com.song.pgentity.PgPromotion;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Mybatis数据库操作类
 * Created by 17060342 on 2019/8/27.
 */
@Mapper
public interface PgPromotionMapper {
    @Select("select id,title,content,type,amt,trandate,to_char(createtime, 'YYYY-MM-DD HH24:MI:SS') as createtime from test order by createtime desc")
    List<PgPromotion> findAllPgPromotions();

    @Select("select id,title,content,type,amt,trandate,to_char(createtime, 'YYYY-MM-DD HH24:MI:SS') as createtime from test order by createtime desc")
    Page<PgPromotion> findPagePgPromotions();

    @Insert("insert into test(title,content) values(#{title},#{content})")
    int addPgPromotion(@Param("title") String title, @Param("content") String content);

    @Update("update test set title = #{title},content = #{content} where id=#{id}")
    int updatePgPromotion(@Param("id") String id, @Param("title") String title, @Param("content") String content);

    @Delete("delete from test where id=#{id}")
    int deletePgPromotion(@Param("id") String id);
}
