package com.song.service;

import com.alibaba.fastjson.JSON;
import com.song.mapper.UserMapper;
import com.song.entity.User;
import com.song.pgmapper.PgPromotionMapper;
import com.song.pgrepositoty.PgPromotionRepositoty;
import com.song.repositoty.UserRepositoty;
import com.song.utils.CookieUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by feng on 2019/5/26.
 */
@Service
public class UserService {
    @Autowired
    private UserRepositoty userRepositoty;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private PgPromotionMapper pgPromotionMapper;

    @Autowired(required = false)
    private PgPromotionRepositoty pgPromotionRepositoty;

    /**
     * log
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);


    public User findUserByName(String name){
        User user = null;
        try{
            user = userRepositoty.findByUserName(name);
        }catch (Exception e){}
        return user;
    }

    public List<User> findAllUsers(){
        List<User> userList = null;
        try{
            userList = userMapper.findAllUsers();
        }catch (Exception e){}
        return userList;
    }

    /**
     * 测试hibernate事务
     *
     *  传播行为	            含义
     PROPAGATION_REQUIRED	表示当前方法必须运行在事务中。如果当前事务存在，方法将会在该事务中运行。否则，会启动一个新的事务
     PROPAGATION_SUPPORTS	表示当前方法不需要事务上下文，但是如果存在当前事务的话，那么该方法会在这个事务中运行
     PROPAGATION_MANDATORY	表示该方法必须在事务中运行，如果当前事务不存在，则会抛出一个异常
     PROPAGATION_REQUIRED_NEW	表示当前方法必须运行在它自己的事务中。一个新的事务将被启动。如果存在当前事务，在该方法执行期间，当前事务会被挂起。
                                如果使用JTATransactionManager的话，则需要访问TransactionManager
     PROPAGATION_NOT_SUPPORTED	表示该方法不应该运行在事务中。如果存在当前事务，在该方法运行期间，当前事务将被挂起。如果使用JTATransactionManager的话，
                                则需要访问TransactionManager
     PROPAGATION_NEVER	表示当前方法不应该运行在事务上下文中。如果当前正有一个事务在运行，则会抛出异常
     PROPAGATION_NESTED	表示如果当前已经存在一个事务，那么该方法将会在嵌套事务中运行。嵌套的事务可以独立于当前事务进行单独地提交或回滚。
                        如果当前事务不存在，那么其行为与PROPAGATION_REQUIRED一样。
                        注意各厂商对这种传播行为的支持是有所差异的。可以参考资源管理器的文档来确认它们是否支持嵌套事务
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int transactionA(){
        int code = 0;
        code = userRepositoty.addUser("test1","test222");
        int i = 1/0;
        return code;
    }

    /**
     * 测试mybatis事务
     *   DEFAULT 这是一个PlatfromTransactionManager默认的隔离级别，使用数据库默认的事务隔离级别.
         未提交读（read uncommited） :脏读，不可重复读，虚读都有可能发生
         已提交读 （read commited）:避免脏读。但是不可重复读和虚读有可能发生
         可重复读 （repeatable read） :避免脏读和不可重复读.但是虚读有可能发生.
         串行化的 （serializable） :避免以上所有读问题.
         Mysql 默认:可重复读
         Oracle 默认:读已提交
     * @return
     */
    @Transactional(isolation=Isolation.DEFAULT)
    public long transactionB(){
        long code = 0;
        code = userMapper.addUser("test1","testxxx","1");
        int i = 1/0;
        return code;
    }

    /**
     * 测试mybatis事务
     * @return
     */
    @Transactional(value="transactionManagerSecondary")
    public int transactionC(){
        int code = 0;
        code = pgPromotionMapper.addPgPromotion("test1","test2");
        int i = 1/0;
        return code;
    }

    /**
     * 测试mybatis事务
     * @return
     */
    @Transactional(value="transactionManagerSecondary")
    public int transactionD(){
        int code = 0;
        code = pgPromotionRepositoty.addPgPromotion("pgtest1","pgtest2");
        int i = 1/0;
        return code;
    }

    /**
     * 测试mybatis一级缓存事务bug
     * @return
     */
    @Transactional
    public void mybatisCacheOne(){
        logger.info("mybatisCacheOne第一次查询" + userMapper.findUserByName("song"));
         userMapper.updateUser("1","song","feng1",null);
        logger.info("mybatisCacheOne第二次查询" + userMapper.findUserByName("song"));
    }

    public int storeCookies(HttpServletRequest request, String key){
        CookieUtil.storeCookies(request,key);
        return 1;
    }

    /**
     * 测试mybatis一级缓存事务bug
     * @return
     */
    @Async
    @Transactional(isolation= Isolation.REPEATABLE_READ)
    //@Transactional
    public void mySQLRR1(){
        User user = userMapper.findUserByName("song");
        logger.info("mySQLRR1" + user);
        for(int i = 0; i < 200000; i++){

        }
        logger.info("mySQLRR1" + userMapper.findUserByName("song"));
        logger.info("mySQLRR1 resul={}",userMapper.updateUser("1","song","fengfin",user.getVersion() + ""));
    }

    /**
     * 测试mybatis一级缓存事务bug
     * @return
     */
    @Async
    @Transactional(isolation= Isolation.REPEATABLE_READ)
    //@Transactional
    public void mySQLRR2(){
        for(int i = 0; i < 10000; i++){

        }
        User user = userMapper.findUserByName("song");
        logger.info("mySQLRR2 resul={}",userMapper.updateUser("1","song","fengTemp",user.getVersion() + ""));
    }


    /**
     * 测试mybatis一级缓存事务bug
     * @return
     */
    @Async
    @Transactional(isolation= Isolation.REPEATABLE_READ)
    //@Transactional
    public void mySQLRR3(){
        User user = userMapper.findUserByName("song");
        logger.info("mySQLRR3" + user);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("mySQLRR3" + userMapper.findUserByName("song"));
        if(user == null){
            logger.info("mySQLRR3 resul={}",userMapper.addUser("song","feng","2"));
        }
    }

    /**
     * 测试mybatis一级缓存事务bug
     * @return
     */
    @Async
    @Transactional(isolation= Isolation.REPEATABLE_READ)
    //@Transactional
    public void mySQLRR4(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = userMapper.findUserByName("song");
        logger.info("mySQLRR4" + user);
        if(user == null) {
            logger.info("mySQLRR4 resul={}",userMapper.addUser("song","fengfin","1"));
        }
    }

}
