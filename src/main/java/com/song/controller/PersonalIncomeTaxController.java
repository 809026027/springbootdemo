package com.song.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.song.service.PersonalIncomeTaxService;
import com.song.utils.PersonalIncomeTaxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2019/5/26.
 */
@Controller
@RequestMapping(value = "/tax")
public class PersonalIncomeTaxController {

    @Autowired
    private PersonalIncomeTaxService personalIncomeTaxService;

    @RequestMapping(value = "/index")
    public String index(){
        return "tax/index";
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public String show(HttpServletRequest request){
        //税前金额
        String amt = request.getParameter("amt");
        //公积金
        String gjjAmt = request.getParameter("gjjAmt");
        //公积金比例
        String gjjRate = request.getParameter("gjjRate");
        //社保基数
        String sbAmt = request.getParameter("sbAmt");
        //附件免税金额
        String fjAmt = request.getParameter("fjAmt");
        //查询类型：1-平均，2-按月
        String type = request.getParameter("type");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("amt",amt);
        params.put("gjjAmt",gjjAmt);
        params.put("gjjRate",gjjRate);
        params.put("sbAmt",sbAmt);
        params.put("fjAmt",fjAmt);
        JSONObject result = new JSONObject();
        Double nutAmt = PersonalIncomeTaxUtil.getNutAmt(params);
        result.put("nutAmt",nutAmt);
        if("1".equals(type)){
            BigDecimal avgTaxAmt = personalIncomeTaxService.queryTaxByAvg(nutAmt);
            result.put("taxAmt",avgTaxAmt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            result.put("sjAmt",new BigDecimal(nutAmt).add(new BigDecimal(fjAmt)).subtract(avgTaxAmt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }else {
            List<BigDecimal> taxAmtList = personalIncomeTaxService.queryTaxByMonth(nutAmt);
            List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
            for(BigDecimal taxAmt : taxAmtList){
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("taxAmt",taxAmt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                map.put("sjAmt",new BigDecimal(nutAmt).add(new BigDecimal(fjAmt)).subtract(taxAmt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                list.add(map);
            }
            result.put("list",list);
        }
        return result.toJSONString();
    }
}