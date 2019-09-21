package com.song.javabase;

import org.apache.commons.collections.map.HashedMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 17060342 on 2019/9/3.
 */
public class saleDemo {
    public static void main(String[] args){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total","300");
        map.put("sub","30");
        map.put("type","1");
        list.add(map);
        map = new HashMap<String,Object>();
        map.put("rate","0.8");
        map.put("type","2");
        list.add(map);
        map = new HashMap<String,Object>();
        map.put("sub","20");
        map.put("type","3");
        list.add(map);

        BigDecimal amt = new BigDecimal("1000");
        System.out.println("优惠前：" + amt.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue());
        for(Map<String,Object> m : list){
            SaleDo saleDo = new SaleDo(SaleModeFactory.getSaleMode(m));
            amt = saleDo.calculate(amt);
        }
        System.out.println("实付款：" + amt.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue());
    }

}

interface Sale{
    BigDecimal calculate(BigDecimal amt);
}

class SaleDo {
    private Sale sale;

    public SaleDo(Sale sale){
        this.sale = sale;
    }

    public BigDecimal calculate(BigDecimal amt){
        return sale.calculate(amt);
    }
}

class SaleMode implements Sale{
    private BigDecimal total;
    private BigDecimal sub;

    public SaleMode(BigDecimal total,BigDecimal sub){
        this.total = total;
        this.sub = sub;
    }
    @Override
    public BigDecimal calculate(BigDecimal amt) {
        if(amt.compareTo(total) >= 0){
            amt = amt.subtract(sub);
            System.out.println("满" + total + "减" + sub + "，优惠后" + amt.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue());
        }else {
            System.out.println(amt.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue() + "不满足满" + total + "减" + sub + "优惠");
        }

        return amt;
    }
}

class SaleMode1 implements Sale{

    private BigDecimal rate;

    public SaleMode1(BigDecimal rate){
        this.rate = rate;
    }
    @Override
    public BigDecimal calculate(BigDecimal amt) {
        amt = amt.multiply(rate);
        System.out.println("全场" + rate.multiply(new BigDecimal("10")) + "折，优惠后" + amt.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue());
        return amt;
    }
}
class SaleMode2 implements Sale{

    private BigDecimal sub;

    public SaleMode2(BigDecimal sub){
        this.sub = sub;
    }
    @Override
    public BigDecimal calculate(BigDecimal amt) {
        if(amt.compareTo(sub) >= 0){
            amt = amt.subtract(sub);
            System.out.println("无门槛叠加券，立减" + sub + "，优惠后" + amt.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue());
        }else {
            System.out.println(amt.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue() + "不满足该优惠");
        }

        return amt;
    }
}

class SaleModeFactory{
    public static Sale getSaleMode(Map<String,Object> params){
        int type = Integer.valueOf(params.get("type").toString());
        Sale sale = null;
        switch (type){
            case 1:sale = new SaleMode(new BigDecimal(params.get("total").toString()),new BigDecimal(params.get("sub").toString()));break;
            case 2:sale = new SaleMode1(new BigDecimal(params.get("rate").toString()));break;
            case 3:sale = new SaleMode2(new BigDecimal(params.get("sub").toString()));break;
        }
        return sale;
    }
}
