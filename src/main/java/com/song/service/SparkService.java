package com.song.service;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.receiver.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark_project.guava.base.Joiner;
import org.spark_project.guava.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.apache.spark.sql.functions.avg;

/**
 * Created by feng on 2019/8/31.
 */
@Service
public class SparkService implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(SparkService.class);

    private static final Pattern SPACE = Pattern.compile(" ");

    @Autowired
    private JavaSparkContext sc;

    @Autowired
    private SparkSession sparkSession;

    public Map<String, Object> calculateTopTen() {

        Map<String, Object> result = new HashMap<String, Object>();
        JavaRDD<String> lines = sc.textFile("src/test/java/test.txt").cache();

        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println(lines.count());

        JavaRDD<String> words = lines.flatMap(str -> Arrays.asList(SPACE.split(str)).iterator());

        JavaPairRDD<String, Integer> ones = words.mapToPair(str -> new Tuple2<String, Integer>(str, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey((Integer i1, Integer i2) -> (i1 + i2));

        JavaPairRDD<Integer, String> temp = counts.mapToPair(tuple -> new Tuple2<Integer, String>(tuple._2, tuple._1));

        JavaPairRDD<String, Integer> sorted = temp.sortByKey(false).mapToPair(tuple -> new Tuple2<String, Integer>(tuple._2, tuple._1));

        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println(sorted.count());

        //List<Tuple2<String, Integer>> output = sorted.collect();

        //List<Tuple2<String, Integer>> output = sorted.take(10);

        List<Tuple2<String, Integer>> output = sorted.top(10, new CustomComaprator());

        for (Tuple2<String, Integer> tuple : output) {
            result.put(tuple._1(), tuple._2());
        }

        return result;
    }

    /**
     * 练习demo，熟悉其中API
     */
    public void sparkExerciseDemo() {
        List<Integer> data = Lists.newArrayList(1,2,3,4,5,6);
        JavaRDD<Integer> rdd01 = sc.parallelize(data);
        rdd01 = rdd01.map(num ->{
            return num * num;
        });
        //data map :1,4,9,16,25,36
        logger.info("data map :{}", Joiner.on(",").skipNulls().join(rdd01.collect()).toString());

        rdd01 = rdd01.filter(x -> x < 6);

        //data filter :1,4
        logger.info("data filter :{}",Joiner.on(",").skipNulls().join(rdd01.collect()).toString());

        rdd01 = rdd01.flatMap( x ->{
            Integer[] test = {x,x+1,x+2};
            return Arrays.asList(test).iterator();
        });

        //flatMap :1,2,3,4,5,6
        logger.info("flatMap :{}",Joiner.on(",").skipNulls().join(rdd01.collect()).toString());

        JavaRDD<Integer> unionRdd = sc.parallelize(data);

        rdd01 = rdd01.union(unionRdd);

        //union :1,2,3,4,5,6,1,2,3,4,5,6
        logger.info("union :{}",Joiner.on(",").skipNulls().join(rdd01.collect()).toString());

        List<Integer> result = Lists.newArrayList();
        result.add(rdd01.reduce((Integer v1,Integer v2) -> {
            return v1+v2;
        }));

        //reduce :42
        logger.info("reduce :{}",Joiner.on(",").skipNulls().join(result).toString());
        result.forEach(System.out::print);

        JavaPairRDD<Integer,Iterable<Integer>> groupRdd  = rdd01.groupBy(x -> {
            logger.info("======grouby========：{}",x);
            if (x > 10) return 0;
            else  return 1;
        });

        List<Tuple2<Integer,Iterable<Integer>>> resul = groupRdd.collect();

        //group by  key:1 value:1,2,3,4,5,6,1,2,3,4,5,6
        resul.forEach(x -> {
            logger.info("group by  key:{} value:{}",x._1,Joiner.on(",").skipNulls().join(x._2).toString());
        });

    }

    public void sparkMySql(){
        SQLContext sqlContext = new SQLContext(sc);
        //jdbc.url=jdbc:mysql://localhost:3306/database
        String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false";
        //查找的表名
        String table = "t_promotion";
        //增加数据库的用户名(user)密码(password),指定test数据库的驱动(driver)
        Properties connectionProperties = new Properties();
        connectionProperties.put("user","root");
        connectionProperties.put("password","root");
        connectionProperties.put("driver","com.mysql.jdbc.Driver");

        // 读取表中所有数据
        Dataset<Row> ds = sqlContext.read().jdbc(url,table,connectionProperties);

        Encoder<String> stringEncoder = Encoders.STRING();
        Dataset<String> stringDataset = ds.map(
                (MapFunction<Row, String>) row -> "Name: " + row.getInt(0),
                stringEncoder);
        stringDataset.show();

        //ds.groupBy(col("createtime").substr(0,10)).count().show();
        //ds.groupBy(col("title").like("%苏宁%")).count().show();
        ds.groupBy(col("title").like("%苏宁%").alias("like %苏宁%")).agg(count(col("title")),max(col("id")),min(col("id")),avg(col("id"))).show();
        //ds = ds.filter(col("createtime").gt("2019-09-01 10:00:00"));
        ds = ds.sort(col("createtime").desc());
        //显示数据
        ds.show();

        JavaRDD<Row> rowJavaRDD = ds.javaRDD();
        List<Integer> list = rowJavaRDD.map(row -> {
            return row.getInt(0);
        }).collect();
        System.out.println("111aaa"+list);
    }

    public void sparkSessionMySql(){
        //jdbc.url=jdbc:mysql://localhost:3306/database
        String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false";
        //查找的表名
        String table = "t_promotion";
        //增加数据库的用户名(user)密码(password),指定test数据库的驱动(driver)
        Properties connectionProperties = new Properties();
        connectionProperties.put("user","root");
        connectionProperties.put("password","root");
        connectionProperties.put("driver","com.mysql.jdbc.Driver");

        // 读取表中所有数据
        Dataset<Row> ds = sparkSession.read().jdbc(url,table,connectionProperties);
        //ds.groupBy(col("createtime").substr(0,10)).count().show();
        //ds.groupBy(col("title").like("%苏宁%")).count().show();
        ds.groupBy(col("title").like("%苏宁%").alias("like %苏宁%")).agg(count(col("title")),max(col("id")),min(col("id")),avg(col("id"))).show();
        //ds = ds.filter(col("createtime").gt("2019-09-01 10:00:00"));
        ds = ds.sort(col("createtime").desc());
        //显示数据
        ds.show();
    }

    /**
     * spark streaming 练习
     */
    public void sparkStreaming() throws InterruptedException {
        JavaStreamingContext jsc = new JavaStreamingContext(sc, Durations.seconds(10));//批间隔时间
        JavaReceiverInputDStream<String> lines = jsc.receiverStream(new CustomReceiver(StorageLevel.MEMORY_AND_DISK_2()));
        JavaDStream<Long> count =  lines.count();
        count = count.map(x -> {
            logger.info("这次批一共多少条数据：{}",x);
            return x;
        });
        count.print();
        jsc.start();
        jsc.awaitTermination();
        jsc.stop();
    }
}

/**
 * 自定义接收streaming类
 */
class CustomReceiver extends Receiver<String> {

    private static Logger logger = LoggerFactory.getLogger(CustomReceiver.class);


    /**
     *
     * @author	hz15041240
     * @date	2018年1月18日 下午4:37:22
     * @version
     */
    private static final long serialVersionUID = 5817531198342629801L;

    public CustomReceiver(StorageLevel storageLevel) {
        super(storageLevel);
    }

    @Override
    public void onStart() {
        new Thread(this::doStart).start();
        logger.info("开始启动Receiver...");
        //doStart();
    }

    public void doStart() {
        while(!isStopped()) {
            int value = RandomUtils.nextInt(100);
            if(value <20) {
                try {
                    Thread.sleep(1000);
                }catch (Exception e) {
                    logger.error("sleep exception",e);
                    restart("sleep exception", e);
                }
            }
            store(String.valueOf(value));
        }
    }


    @Override
    public void onStop() {
        logger.info("即将停止Receiver...");
    }

}

class CustomComaprator implements Serializable, Comparator {
    public int compare(Object o11, Object o12) {
        Tuple2<String, Integer> o1 = (Tuple2<String, Integer>) o11;
        Tuple2<String, Integer> o2 = (Tuple2<String, Integer>) o12;
        if (o1._2 > o2._2) {
            return o1._2;
        }else{
            return o2._2;
        }
    }
}
