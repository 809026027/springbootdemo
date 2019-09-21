package com.song.mapper;

import com.github.pagehelper.Page;
import com.song.entity.Promotion;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Mybatis数据库操作类
 * Created by 17060342 on 2019/6/4.
 */
@Mapper
public interface PromotionMapper {
    @Select("select * from t_promotion order by createtime desc")
    List<Promotion> findAllPromotions();

    @Select("select * from t_promotion order by createtime desc")
    Page<Promotion> findPagePromotions();

    @Insert("insert into t_promotion(title,content) values(#{title},#{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long addPromotion(Promotion promotion);

    @Update("update t_promotion set title = #{title},content = #{content},version=version+1 where id=#{id}")
    int updatePromotion(@Param("id") String id, @Param("title") String title, @Param("content") String content);

    @Delete("delete from t_promotion where id=#{id}")
    int deletePromotion(@Param("id") String id);
}
