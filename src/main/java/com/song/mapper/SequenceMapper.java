package com.song.mapper;

import com.song.entity.Sequence;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Mybatis数据库操作类
 * Created by 17060342 on 2019/6/4.
 */
@Mapper
public interface SequenceMapper {
    @Select("select * from t_sequence")
    List<Sequence> findAllSequences();

    @Select("select * from t_sequence where id = #{id}")
    Sequence findSequenceById(@Param("id") long id);

    @Insert("insert into t_sequence(id) values(#{id})")
    int addSequence(@Param("id") long id);

    @Insert("insert ignore into t_sequence(id) values(#{id})")
    int addIgnoreSequence(@Param("id") long id);

    @Update("update t_sequence set count = count + 1 where id = #{id} and count = #{count}")
    int updateSequence(@Param("id") long id, @Param("count") long count);

    @Delete("delete from t_sequence where id=#{id}")
    int deleteSequence(@Param("id") long id);
}
