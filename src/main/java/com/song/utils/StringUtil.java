package com.song.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.text.DecimalFormat;

/**
 * 字符串工具类
 * Created by 17060342 on 2019/7/24.
 */
public class StringUtil {

    /**
     * 日志
     */
    public final static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     *
     * 对字符串判空
     * 〈功能详细描述〉
     *
     * @param str
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static boolean isNotNull(String str){
        if (null != str && !"".equals(str.trim())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 将获取当前系统IP
     * @return String
     */
    public static String getLocalIP(){
        try{
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        }
        catch(Exception e){
            logger.error("getLocalIP Exception:{}",e);
            return "";
        }
    }

    /**
     *
     * 对字符串判空
     * 〈功能详细描述〉
     *
     * @param str
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static boolean isNull(String str){
        if (null == str || "".equals(str.trim())){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * 格式化金额 <br>
     * 将金额字段格式化为  "1,123,234,345.12"
     * 保留2位小数，没有小数以".00"结尾
     * 且第三位小数不四舍五入进位
     *
     * @param amount
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String formatAmount(String amount) {
        if (!isNotNull(amount)){
            return "";
        }
        BigDecimal b = new BigDecimal(amount);
        DecimalFormat d1 = new DecimalFormat("#,##0.00");
        d1.setRoundingMode(RoundingMode.FLOOR);
        return d1.format(b);
    }

    public static String formatStrIsNull(String str){
        return isNotNull(str) ? str : "";
    }

    /**
     * 判断字段是否为空,如果为空则给一个默认值 newValue
     * @param str
     * @param newValue
     * @return
     */
    public static String formatStrIsNull(String str,String newValue){
        return isNotNull(str) ? str : newValue;
    }

    /**
     * 处理字符串
     * @param arg0 要处理的对象
     * @return 若obj为空(null)则返回"",否则返回obj转换成字符串且除去该字符前后空格之后的值
     */
    public static String parseString(Object arg0){
        return arg0 == null ? "" : arg0.toString().trim();
    }

    /**
     * 处理字符串
     * @param arg0 要处理的对象
     * @return 若obj为空(null)或者""则返回true,否则返回false
     */
    public static boolean isObjNull(Object arg0){
        return "".equals(parseString(arg0));
    }

    public static String fillZero(String value, int totalBit) {
        int length = value.length();
        int fillLength = totalBit - length;
        if(fillLength < 0) {
            logger.error("fillZero: value[{}].length()>totalBit[{}]!",value,totalBit);
            return value;
        }
        String returnValue = "";
        for(int i=0; i<fillLength; i++) {returnValue += "0";}
        returnValue += value;
        return returnValue;
    }

    public static void main(String[] args) {
        String url = "https://mvs.suning.com/project/JoinGo/groupdetail.html?groupId=603283016864800768&productCode=000000011223886276&custNoTK=PMHxVqnsDX7Up0tbMdSdwG2eHHHsi3BDov3DzfOp212U9f2X6kiSiZrlQieZSSEsErktewk7XJ60CU%2F4lEGbut5FUAW6QrR%2FV7q0hGQ%2FQKpIc4w2PJKKMC8RSV6fXRVIxjzNrQXteF8T1SuWCMl4UghCS9V7E6MkPh4X1NM4%3D";
        try {
            Document doc = Jsoup.connect(url).timeout(3000).get();
            System.out.println(doc.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

