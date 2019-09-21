package com.song.algorithm.sorts;

import com.alibaba.fastjson.JSON;

/**
 * Created by 17060342 on 2019/9/19.
 */
public class SelectSort {
    public static int[] arrays = new int[]{3,6,2,1,9,4,7,8};

    public static void sort(){
        int len = arrays.length;
        for(int i= 0; i < len; i++){
            int min = Integer.MAX_VALUE;
            int minIndex = 0;
            for(int j = i; j < len; j++){
                if(min > arrays[j]){
                    min = arrays[j];
                    minIndex = j;
                }
            }
            if(min < arrays[i]){
                int temp = arrays[i];
                arrays[i] = min;
                arrays[minIndex] = temp;
            }
            System.out.println("第"+ (i + 1) +"轮排序结束:"+ JSON.toJSONString(arrays));
        }

    }

    public static void main(String[] args){
        sort();
    }
}
