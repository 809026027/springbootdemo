package com.song.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * 敏感词过滤
 * Created by 17060342 on 2019/7/2.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KeywordFilterUtil
{
    /** 直接禁止的 */
    private HashMap keysMap   = new HashMap();
    private int     matchType = 1;            // 1:最小长度匹配 2：最大长度匹配

    public void addKeywords(List<String> keywords)
    {
        for (int i = 0; i < keywords.size(); i++)
        {
            String key = keywords.get(i).trim();
            HashMap nowhash = null;
            nowhash = keysMap;
            for (int j = 0; j < key.length(); j++)
            {
                char word = key.charAt(j);
                Object wordMap = nowhash.get(word);
                if (wordMap != null)
                {
                    nowhash = (HashMap) wordMap;
                } else
                {
                    HashMap<String, String> newWordHash = new HashMap<String, String>();
                    newWordHash.put("isEnd", "0");
                    nowhash.put(word, newWordHash);
                    nowhash = newWordHash;
                }
                if (j == key.length() - 1)
                {
                    nowhash.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 重置关键词
     */
    public void clearKeywords()
    {
        keysMap.clear();
    }

    /**
     * 检查一个字符串从begin位置起开始是否有keyword符合， 如果有符合的keyword值，返回值为匹配keyword的长度，否则返回零
     * flag 1:最小长度匹配 2：最大长度匹配
     */
    private int checkKeyWords(String txt, int begin, int flag)
    {
        HashMap nowhash = null;
        nowhash = keysMap;
        int maxMatchRes = 0;
        int res = 0;
        int l = txt.length();
        char word = 0;
        for (int i = begin; i < l; i++)
        {
            word = txt.charAt(i);
            Object wordMap = nowhash.get(word);
            if (wordMap != null)
            {
                res++;
                nowhash = (HashMap) wordMap;
                if (((String) nowhash.get("isEnd")).equals("1"))
                {
                    if (flag == 1)
                    {
                        wordMap = null;
                        nowhash = null;
                        txt = null;
                        return res;
                    } else
                    {
                        maxMatchRes = res;
                    }
                }
            } else
            {
                txt = null;
                nowhash = null;
                return maxMatchRes;
            }
        }
        txt = null;
        nowhash = null;
        return maxMatchRes;
    }

    /**
     * 返回txt中关键字的列表
     */
    public List<String> getTxtKeyWords(String txt)
    {
        List<String> list = new ArrayList<String>();
        int l = txt.length();
        for (int i = 0; i < l;)
        {
            int len = checkKeyWords(txt, i, matchType);
            if (len > 0)
            {
                String tt = "<font color='#ff0000'>" + txt.substring(i, i + len) + "</font>";
                list.add(tt);
                i += len;
            } else
            {
                i++;
            }
        }
        txt = null;
        return list;
    }

    /**
     * 仅判断txt中是否有关键字
     */
    public boolean isContentKeyWords(String txt)
    {
        for (int i = 0; i < txt.length(); i++)
        {
            int len = checkKeyWords(txt, i, 1);
            if (len > 0)
            {
                return true;
            }
        }
        txt = null;
        return false;
    }
    /**
     * 初始化敏感词列表
     * */
    public void initfiltercode()
    {
        List<String> keywords = new ArrayList<String>();

        InputStream in = KeywordFilterUtil.class.getClassLoader().getResourceAsStream("words.properties");
        Properties pro = new Properties();
        try
        {
            pro.load(in);
            in.close();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
        Enumeration<String> enu = (Enumeration<String>) pro.propertyNames();
        while (enu.hasMoreElements())
        {
            try
            {
                String dd = (String) enu.nextElement();
                dd = new String(dd.getBytes("ISO8859-1"), "UTF-8");
                keywords.add(dd);
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        addKeywords(keywords);
    }
    public int getMatchType()
    {
        return matchType;
    }

    public void setMatchType(int matchType)
    {
        this.matchType = matchType;
    }

    public static void main(String[] args) throws IOException
    {
        Date date = new Date();
        KeywordFilterUtil filter = new KeywordFilterUtil();
        filter.initfiltercode();
        File file = ResourceUtils.getFile("classpath:test.txt");
        String txt = FileUtils.readFileToString(file,"UTF-8");
        boolean boo = filter.isContentKeyWords(txt);
        System.out.println(boo);
        List<String> set = filter.getTxtKeyWords(txt);
        System.out.println("包含的敏感词如下："+set);
        Date date2 = new Date();
        float ss = date2.getTime() - date.getTime();
        System.out.println(ss + "毫秒");
    }
}