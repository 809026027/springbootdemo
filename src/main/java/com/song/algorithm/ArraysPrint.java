package com.song.algorithm;

/**
 * Created by 17060342 on 2019/9/14.
 */
public class ArraysPrint {
    public static void printArrays(int n){
        String[] strs = new String[n + 1];
        for(int i = 0; i <= n; i++){
            strs[i] = i + "";
        }
        for(int j = 0; j<= n; j++){
            if(n >= 1)
            {
                System.out.println(j);
            }
            dns(j,strs,n,j + 1);
        }
    }

    private static void dns(int j, String[] strs,int n,int offset) {
        if(offset == n + 1) {
            return;
        }
        StringBuffer sb = new StringBuffer(j + "");
        for(int index = Integer.valueOf(offset); index <= n; index++){
            sb.append(strs[index]);
            if(sb.length() <= n)
            {
                System.out.println(sb);
            }
        }
        dns(j,strs,n,++offset);
    }


    public static void main(String[] args) {
        printArrays(3);
    }
}
