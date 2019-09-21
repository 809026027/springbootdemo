package com.song.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by feng on 2019/9/7.
 */
public class ThreadPoolUtil {


    public static ThreadPoolExecutor threadPool;

    /**
     * 无返回值直接执行, 管他娘的
     * @param runnable
     */
    public static void execute(Runnable runnable){
        getThreadPool().execute(runnable);
    }

    /**
     * 返回值直接执行, 管他娘的
     * @param callable
     */
    public  static <T> Future<T> submit(Callable<T> callable){
        return   getThreadPool().submit(callable);
    }


    /**
     * dcs获取线程池
     * corePoolSize： 线程池维护线程的最少数量
         maximumPoolSize：线程池维护线程的最大数量
         keepAliveTime： 线程池中超过corePoolSize数目的空闲线程最大存活时间
         unit： 线程池维护线程所允许的空闲时间的单位
         workQueue： 线程池所使用的缓冲队列
         handler： 线程池对拒绝任务的处理策略
                    默认AbortPolicy 该策略直接抛出异常，阻止系统工作
                    CallerRunsPolicy 只要线程池未关闭，该策略直接在调用者线程中运行当前被丢弃的任务。显然这样不会真的丢弃任务，但是，调用者线程性能可能急剧下降。
                    DiscardOldestPolicy 丢弃最老的一个请求任务，也就是丢弃一个即将被执行的任务，并尝试再次提交当前任务。
                    DiscardPolicy 默默的丢弃无法处理的任务，不予任何处理。
                    自定义拒绝策略
     * @return 线程池对象
     */
    public static ThreadPoolExecutor getThreadPool() {
        if (threadPool != null) {
            return threadPool;
        } else {
            synchronized (ThreadPoolUtil.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPoolExecutor(8, 16, 60, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(32), new ThreadPoolExecutor.CallerRunsPolicy());
                }
                return threadPool;
            }
        }
    }

    public static ExecutorService newCachedThreadPool(){
        return new ThreadPoolExecutor(0,Integer.MAX_VALUE,60L,TimeUnit.MILLISECONDS,new SynchronousQueue<Runnable>());
    }

    public static ExecutorService newFixedThreadPool(int nThreads){
        return new ThreadPoolExecutor(nThreads,nThreads,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 定时任务线程池
     * @param nThreads
     * @return
     * 1、线程对象，多久后执行，时间单位
     * <V> ScheduledFuture<V> scheduleTask = scheduler.scheduleAtFixedRate(command, 1, TimeUnit.SECONDS);
     * 2、线程对象，多久后执行，每隔多长时间执行一次(上次开始时间，若上次未结束，则等待上次结束后执行)，时间单位
     * ScheduledFuture<?> scheduleTask = scheduler.scheduleAtFixedRate(command, 5, 1, TimeUnit.SECONDS);
     * 3、线程对象，多久后执行，每隔多长时间执行一次(上次结束时间)，时间单位
     * ScheduledFuture<?> scheduleTask = scheduler.scheduledWithFixedDelay(command, 5, 1, TimeUnit.SECONDS);
     */
    public static ExecutorService newScheduledThreadPool(int nThreads){
        return Executors.newScheduledThreadPool(nThreads);
    }

    public static ExecutorService newSingleThreadExecutor() {
        return new ThreadPoolExecutor(1,1,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        List<Future> futureList = new ArrayList();
        // 发送10次消息
        for (int i = 0; i < 10; i++) {
            try {
                String message = String.format("这是第{%s}条消息", i);
                Future<String> messageFuture = ThreadPoolUtil.submit(new Callable<String>(){
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(300);
                        System.out.println(String.format("打印消息%s", message));
                        return "OK";
                    }
                });
                futureList.add(messageFuture);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Future<String> message : futureList) {
            String messageData = message.get();
        }

        System.out.println(String.format("共计耗时{%s}毫秒", System.currentTimeMillis() - start));

    }
}
