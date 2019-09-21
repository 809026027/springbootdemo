package com.song.javabase;

/**
 * 责任链模式
 * Created by 17060342 on 2019/8/30.
 */
public class ChainMode {
    public static void main(String[] args) {
        Handler h1 = new ConcreteHandler("h1");
        Handler h2 = new ConcreteHandler("h2");
        Handler h3 = new ConcreteHandler("h3");
        h1.setNextHandler(h2);   //h1的下一个处理器是h2
        h2.setNextHandler(h3);   //h2的下一个处理器是h3
        h1.handleRequest();
    }
}

/**
 * 抽象处理器
 */
abstract class Handler {
    //下一个处理器
    private Handler nextHandler;

    //处理方法
    public abstract void handleRequest();

    public Handler getNextHandler() {
        return nextHandler;
    }
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }
}

/**
 * 具体处理器.
 */
class ConcreteHandler extends Handler {
    private String id;
    public ConcreteHandler(String id){
        this.id = id;
    }
    @Override
    public void handleRequest() {
        System.out.println("ConcreteHandler"+id+"处理器处理");
        if (getNextHandler()!=null){   //判断是否存在下一个处理器
            getNextHandler().handleRequest();   //存在则调用下一个处理器
        }
    }

}
