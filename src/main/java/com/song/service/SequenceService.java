package com.song.service;

import com.song.entity.Sequence;
import com.song.entity.User;
import com.song.mapper.SequenceMapper;
import com.song.mapper.UserMapper;
import com.song.pgmapper.PgPromotionMapper;
import com.song.pgrepositoty.PgPromotionRepositoty;
import com.song.repositoty.UserRepositoty;
import com.song.utils.CookieUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by feng on 2019/5/26.
 */
@Service
public class SequenceService {

    @Autowired(required = false)
    private SequenceMapper sequenceMapper;
    /**
     * log
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SequenceService.class);

    @Transactional
    public long getCount(long id){
        try{
            Sequence sequence = sequenceMapper.findSequenceById(id);
            if(sequence == null){
                System.out.println("尝试新增count");
                sequenceMapper.addSequence(id);
                System.out.println("尝试新增count成功，返回1");
                return 1;
            }else{
                System.out.println("尝试更新count");
                int result = sequenceMapper.updateSequence(id,sequence.getCount());
                if(result == 0){
                    System.out.println("尝试更新count失败，递归");
                    return getCount(id);
                }else {
                    System.out.println("尝试更新count成功，当前count + 1");
                    return sequence.getCount() + 1;
                }
            }
        }catch (Exception e){
            if(e instanceof DuplicateKeyException){
                System.out.println("尝试新增count失败");
                return getCount(id);
            }else {
                System.out.println("获取count异常，返回-1");
                return -1;
            }
        }
    }

    /**
     * 采用ignore效率稍高于直接插入索引冲突
     * @param id
     * @return
     */
    @Transactional
    public long getIgnoreCount(long id){
        try{
            Sequence sequence = sequenceMapper.findSequenceById(id);
            if(sequence == null){
                System.out.println("尝试新增count");
                int result = sequenceMapper.addIgnoreSequence(id);
                if(result == 1){
                    System.out.println("尝试新增count成功，返回1");
                    return 1;
                }else{
                    System.out.println("尝试新增count失败，ignore");
                    return getCount(id);
                }
            }else{
                System.out.println("尝试更新count");
                int result = sequenceMapper.updateSequence(id,sequence.getCount());
                if(result == 0){
                    System.out.println("尝试更新count失败，递归");
                    return getCount(id);
                }else {
                    System.out.println("尝试更新count成功，当前count + 1");
                    return sequence.getCount() + 1;
                }
            }
        }catch (Exception e){
            if(e instanceof DuplicateKeyException){
                System.out.println("尝试新增count失败，唯一索引冲突");
                return getCount(id);
            }else {
                System.out.println("获取count异常，返回-1");
                return -1;
            }
        }
    }

    /**
     * 采用synchronized效率稍高于直接插入索引冲突
     * @param id
     * @return
     */
    @Transactional
    public synchronized long getSyncCount(long id){
        try{
            Sequence sequence = sequenceMapper.findSequenceById(id);
            if(sequence == null){
                System.out.println("尝试新增count");
                int result = sequenceMapper.addIgnoreSequence(id);
                if(result == 1){
                    System.out.println("尝试新增count成功，返回1");
                    return 1;
                }else{
                    System.out.println("尝试新增count失败，ignore");
                    return -1;
                }
            }else{
                sequenceMapper.updateSequence(id,sequence.getCount());
                System.out.println("尝试更新count成功，当前count + 1");
                return sequence.getCount() + 1;
            }
        }catch (Exception e){
            System.out.println("获取count异常，返回-1");
            return -1;
        }
    }
}
