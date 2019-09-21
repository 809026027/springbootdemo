package com.song.service;

import com.song.entity.User;
import com.song.mapper.UserMapper;
import com.song.repositoty.UserRepositoty;
import com.song.utils.CookieUtil;
import com.song.utils.PersonalIncomeTaxUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2019/5/26.
 */
@Service
public class PersonalIncomeTaxService {
    /**
     * log
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PersonalIncomeTaxService.class);

    /**
     * 查询平均个人所得说
     * @param amt
     * @return
     */
    public BigDecimal queryTaxByAvg(Double amt){
        return PersonalIncomeTaxUtil.newTaxCalculate(amt);
    }

    /**
     * 查询月度个人所得说
     * @param amt
     * @return
     */
    public List<BigDecimal> queryTaxByMonth(Double amt){
        return PersonalIncomeTaxUtil.newTaxCalculateByMonth(amt);
    }
}
