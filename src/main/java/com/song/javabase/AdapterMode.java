package com.song.javabase;

/**
 * 适配器模式
 * Created by 17060342 on 2019/8/30.
 */
public class AdapterMode {
    public static void main(String[] args) {
        Computer computer = new ThinkpadComputer();
        SDCard sdCard = new SDCardImpl();
        System.out.println(computer.readSD(sdCard));
        System.out.println("====================================");
        TFCard tfCard = new TFCardImpl();
        SDCard tfCardAdapterSD = new SDAdapterTF(tfCard);
        System.out.println(computer.readSD(tfCardAdapterSD));
    }
}

/**
 * 1、先创建一个SD卡的接口：
 */
interface SDCard {
    //读取SD卡方法
    String readSD();
    //写入SD卡功能
    int writeSD(String msg);
}

/**
 * 2、创建SD卡接口的实现类，模拟SD卡的功能：
 */
class SDCardImpl implements SDCard {
    @Override
    public String readSD() {
        String msg = "sdcard read a msg :hello word SD";
        return msg;
    }
    @Override
    public int writeSD(String msg) {
        System.out.println("sd card write msg : " + msg);
        return 1;
    }
}

/**
 * 3、创建计算机接口，计算机提供读取SD卡方法：
 */
interface Computer {
    String readSD(SDCard sdCard);
}

/**
 * 4、创建一个计算机实例，实现计算机接口，并实现其读取SD卡方法：
 */
class ThinkpadComputer implements Computer {
    @Override
    public String readSD(SDCard sdCard) {
        if(sdCard == null)throw new NullPointerException("sd card null");
        return sdCard.readSD();
    }
}

/**
 * 5、这时候就可以模拟计算机读取SD卡功能：
 */
class ComputerReadDemo {
    public static void main(String[] args) {
        Computer computer = new ThinkpadComputer();
        SDCard sdCard = new SDCardImpl();
        System.out.println(computer.readSD(sdCard));
    }
}


/**
 * 接下来在不改变计算机读取SD卡接口的情况下，通过适配器模式读取TF卡：
 * 1、创建TF卡接口：
 */
interface TFCard {
    String readTF();
    int writeTF(String msg);
}

/**
 * 2、创建TF卡实例：
 */
class TFCardImpl implements TFCard {
    @Override
    public String readTF() {
        String msg ="tf card reade msg : hello word tf card";
        return msg;
    }
    @Override
    public int writeTF(String msg) {
        System.out.println("tf card write a msg : " + msg);
        return 1;
    }
}

/**
 * 3、创建SD适配TF （也可以说是SD兼容TF，相当于读卡器）：
      实现SDCard接口，并将要适配的对象作为适配器的属性引入。
 */
class SDAdapterTF implements SDCard {
    private TFCard tfCard;
    public SDAdapterTF(TFCard tfCard) {
        this.tfCard = tfCard;
    }
    @Override
    public String readSD() {
        System.out.println("adapter read tf card ");
        return tfCard.readTF();
    }
    @Override
    public int writeSD(String msg) {
        System.out.println("adapter write tf card");
        return tfCard.writeTF(msg);
    }
}
