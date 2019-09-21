package com.song.algorithm.sorts;

import com.alibaba.fastjson.JSON;

/**
 * 快速排序
 * Created by feng on 2019/9/19.
 */
public class QuickSort {
    public static int[] arrays = new int[]{65, 58, 95, 10, 57, 62, 13, 106, 78, 23, 85};

    public static void sort(int low,int high){
        if(low >= high){
            return;
        }
        //int middle = getMiddleDns(arrays[low],low,high);
        int middle = getMiddleLoop(arrays[low],low,high);
        System.out.println("middle="+middle);
        sort(low,middle - 1);
        sort(middle + 1,high);
    }

    private static int getMiddleDns(int tip,int low, int high) {
        while(low < high && arrays[high] > tip){
            high--;
        }
        while(low < high && arrays[low] < tip){
            low++;
        }
        if(low < high){
            int temp = arrays[low];
            arrays[low] = arrays[high];
            arrays[high] = temp;
            System.out.println(JSON.toJSONString(arrays));
            return getMiddleDns(tip,low,high);
        }
        return low;
    }

    private static int getMiddleLoop(int tip,int low, int high) {
        while(low < high){
            while(low < high && arrays[high] > tip){
                high--;
            }
            while(low < high && arrays[low] < tip){
                low++;
            }
            if(low < high){
                int temp = arrays[low];
                arrays[low] = arrays[high];
                arrays[high] = temp;
                System.out.println(JSON.toJSONString(arrays));
            }
        }
        return low;
    }

    public static void main(String[] args){
        sort(0,arrays.length - 1);
        System.out.println(JSON.toJSONString(arrays));
    }
}
