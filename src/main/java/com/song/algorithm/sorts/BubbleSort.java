package com.song.algorithm.sorts;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 17060342 on 2019/9/19.
 */
public class BubbleSort {

    public static int[] arrays = new int[]{3,6,2,1,9,4,7,8};

    public static void sort(){
        int len = arrays.length;
        for(int i= 0; i < len; i++){
            boolean order = true;
            for(int j = 0; j < len - 1; j++){
                if(arrays[j] > arrays[j + 1]){
                    int temp = arrays[j];
                    arrays[j] = arrays[j + 1];
                    arrays[j + 1] = temp;
                    order = false;
                }
            }
            System.out.println("第"+ (i + 1) +"轮排序结束:"+JSON.toJSONString(arrays));
            if(order){
                break;
            }
        }

    }

    public static void main(String[] args){
        sort();
    }
}
