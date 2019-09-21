package com.song.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 新个税计算
 * Created by 17060342 on 2019/6/26.
 */
public final class PersonalIncomeTaxUtil {
    /**
     * 旧个税起征点
     */
    private static int OLD_TAX_THRESHOLD = 3500;

    /**
     * 新个税起征点
     */
    private static int NEW_TAX_THRESHOLD = 5000;

    /**
     * 私有构造方法
     */
    private PersonalIncomeTaxUtil()
    {

    }

    /**
     * 旧个税计算方案: <br>
     * 〈旧个税计算方案〉
     *
     * @param amt
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static Double oldTaxCalculate(Double amt)
    {
        System.out.print(amt + "==");
        BigDecimal taxAmt = new BigDecimal(amt).subtract(BigDecimal.valueOf(OLD_TAX_THRESHOLD));
        if(taxAmt.compareTo(new BigDecimal("0")) <= 0)
        {
            return new BigDecimal("0.00").doubleValue();
        }else if(taxAmt.compareTo(new BigDecimal("1500")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.03))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else if(taxAmt.compareTo(new BigDecimal("4500")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.1)).subtract(BigDecimal.valueOf(105))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else if(taxAmt.compareTo(new BigDecimal("9000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.2)).subtract(BigDecimal.valueOf(555))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else if(taxAmt.compareTo(new BigDecimal("35000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.25)).subtract(BigDecimal.valueOf(1005))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else if(taxAmt.compareTo(new BigDecimal("55000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.30)).subtract(BigDecimal.valueOf(2755))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else if(taxAmt.compareTo(new BigDecimal("80000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.35)).subtract(BigDecimal.valueOf(5505))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else{
            return taxAmt.multiply(BigDecimal.valueOf(0.45)).subtract(BigDecimal.valueOf(13505))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    /**
     * 新个税计算方案: <br>
     * 〈新个税计算方案〉
     *
     * @param amt
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static BigDecimal newTaxCalculate(Double amt)
    {
        BigDecimal taxAmt = new BigDecimal(amt).subtract(BigDecimal.valueOf(NEW_TAX_THRESHOLD));
        if(taxAmt.compareTo(new BigDecimal("0")) <= 0)
        {
            return new BigDecimal("0.00");
        }else if(taxAmt.compareTo(new BigDecimal("3000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.03))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }else if(taxAmt.compareTo(new BigDecimal("12000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.1))
                    .subtract(BigDecimal.valueOf(3000*(0.1-0.03)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }else if(taxAmt.compareTo(new BigDecimal("25000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.2))
                    .subtract(BigDecimal.valueOf(3000*(0.2-0.03) + (12000-3000)*(0.2-0.1)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }else if(taxAmt.compareTo(new BigDecimal("35000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.25))
                    .subtract(BigDecimal.valueOf(3000*(0.25-0.03) + (12000-3000)*(0.25-0.1) + (25000-12000)*(0.25-0.2)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }else if(taxAmt.compareTo(new BigDecimal("55000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.30))
                    .subtract(BigDecimal.valueOf(3000*(0.30-0.03) + (12000-3000)*(0.30-0.1) + (25000-12000)*(0.30-0.2) + (35000-25000)*(0.30-0.25)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }else if(taxAmt.compareTo(new BigDecimal("80000")) <= 0){
            return taxAmt.multiply(BigDecimal.valueOf(0.35))
                    .subtract(BigDecimal.valueOf(3000*(0.35-0.03) + (12000-3000)*(0.35-0.1) + (25000-12000)*(0.35-0.2) + (35000-25000)*(0.35-0.25) + (55000-35000)*(0.35-0.3)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }else{
            return taxAmt.multiply(BigDecimal.valueOf(0.45))
                    .subtract(BigDecimal.valueOf(3000*(0.45-0.03) + (12000-3000)*(0.45-0.1) + (25000-12000)*(0.45-0.2) + (35000-25000)*(0.45-0.25) + (55000-35000)*(0.45-0.3) + (80000-35000)*(0.45-0.35)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 新个税计算方案按月: <br>
     * 〈新个税计算方案按月〉
     *
     * @param amt
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static List<BigDecimal> newTaxCalculateByMonth(Double amt)
    {
        BigDecimal taxAmt = BigDecimal.valueOf(NEW_TAX_THRESHOLD);
        BigDecimal totalAmt = new BigDecimal(amt).subtract(taxAmt);
        BigDecimal totalTaxAmt = BigDecimal.ZERO;
        List<BigDecimal> taxAmtList = new ArrayList<BigDecimal>();
        for(int i = 1; i <= 12; i++) {
            if(totalAmt.compareTo(BigDecimal.ZERO) <= 0){
                taxAmtList.add(BigDecimal.ZERO);
            }else if(totalAmt.compareTo(new BigDecimal("36000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.03)).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("144000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.1)).subtract(new BigDecimal("2520")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("300000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.2)).subtract(new BigDecimal("16920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("420000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.25)).subtract(new BigDecimal("31920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("660000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.3)).subtract(new BigDecimal("52920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("960000")) <= 0) {
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.35)).subtract(new BigDecimal("85920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else{
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.45)).subtract(new BigDecimal("181920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            totalAmt = totalAmt.add(new BigDecimal(amt)).subtract(taxAmt);
            totalTaxAmt = totalTaxAmt.add(taxAmtList.get(i - 1));
        }
        System.out.print(amt + "==avgTaxAmt-->" + totalTaxAmt.divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP));
        return taxAmtList;
    }

    /**
     * 新个税计算方案按月: <br>
     * 〈新个税计算方案按月〉
     *
     * @param list
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static List<BigDecimal> newTaxCalculateByMonth(List<String> list)
    {
        BigDecimal taxAmt = BigDecimal.valueOf(NEW_TAX_THRESHOLD);
        BigDecimal totalAmt = new BigDecimal(list.get(0)).subtract(taxAmt);
        BigDecimal totalTaxAmt = BigDecimal.ZERO;
        List<BigDecimal> taxAmtList = new ArrayList<BigDecimal>();
        for(int i = 1; i <= 12; i++) {
            if(totalAmt.compareTo(BigDecimal.ZERO) <= 0){
                taxAmtList.add(BigDecimal.ZERO);
            }else if(totalAmt.compareTo(new BigDecimal("36000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.03)).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("144000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.1)).subtract(new BigDecimal("2520")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("300000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.2)).subtract(new BigDecimal("16920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("420000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.25)).subtract(new BigDecimal("31920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("660000")) <= 0){
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.3)).subtract(new BigDecimal("52920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(totalAmt.compareTo(new BigDecimal("960000")) <= 0) {
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.35)).subtract(new BigDecimal("85920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }else{
                taxAmtList.add(totalAmt.multiply(BigDecimal.valueOf(0.45)).subtract(new BigDecimal("181920")).subtract(totalTaxAmt)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            totalAmt = i < 12 ? totalAmt.add(new BigDecimal(list.get(i))).subtract(taxAmt) : totalAmt;
            totalTaxAmt = totalTaxAmt.add(taxAmtList.get(i - 1));
        }
        System.out.print(list + "==avgTaxAmt-->" + totalTaxAmt.divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP));
        return taxAmtList;
    }

    /**
     * 获取计税金额，扣除五险一金和附加免税金额
     * @param params
     * @return
     */
    public static Double getNutAmt(Map<String,Object> params){
        BigDecimal amt = new BigDecimal(params.get("amt").toString());
        BigDecimal gjjAmt = new BigDecimal(params.get("gjjAmt").toString());
        BigDecimal gjjRate = new BigDecimal(params.get("gjjRate").toString());
        BigDecimal sbAmt = new BigDecimal(params.get("sbAmt").toString());
        BigDecimal fjAmt = new BigDecimal(params.get("fjAmt").toString());

        BigDecimal totalgjjAmt = gjjAmt.multiply(gjjRate).multiply(new BigDecimal("2"));
        BigDecimal totalSbAmt = sbAmt.multiply(gjjRate).multiply(new BigDecimal("0.105")).add(new BigDecimal("10"));

        BigDecimal nutAmt = amt.subtract(totalgjjAmt).subtract(totalSbAmt).subtract(fjAmt).setScale(2, BigDecimal.ROUND_HALF_UP);
        return nutAmt.doubleValue();
    }

    public static void main(String[] args) {
        System.out.println(oldTaxCalculate(5000d) + "-->" +newTaxCalculate(5000d));
        System.out.println(oldTaxCalculate(6000d) + "-->" +newTaxCalculate(6000d));
        System.out.println(oldTaxCalculate(8000d) + "-->" +newTaxCalculate(8000d));
        System.out.println(oldTaxCalculate(10000d) + "-->" +newTaxCalculate(10000d));
        System.out.println(oldTaxCalculate(13200d) + "-->" +newTaxCalculate(13200d));
        System.out.println(oldTaxCalculate(14000d) + "-->" +newTaxCalculate(14000d));
        System.out.println(oldTaxCalculate(23200d) + "-->" +newTaxCalculate(23200d));
        System.out.println(oldTaxCalculate(50000d) + "-->" +newTaxCalculate(50000d));
        System.out.println(oldTaxCalculate(100000d) + "-->" +newTaxCalculate(100000d));

        System.out.println(oldTaxCalculate(11280d) + "-->" +newTaxCalculate(11280d));
        System.out.println(newTaxCalculateByMonth(13500d));
        System.out.println(newTaxCalculateByMonth(14000d));
        System.out.println(newTaxCalculateByMonth(14500d));
        System.out.println(newTaxCalculateByMonth(15000d));
        System.out.println(newTaxCalculateByMonth(15500d));
        System.out.println(newTaxCalculateByMonth(16000d));
        System.out.println(newTaxCalculateByMonth(Arrays.asList("14000", "14000", "14000","14000","14000","14000","14000","14000","14000","14000","14000","14000")));
    }
}
