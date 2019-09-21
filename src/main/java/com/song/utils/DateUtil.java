package com.song.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 * 
 * Created by 17060342 on 2019/6/3.
 */
public final class DateUtil{

	private static final  Logger logger = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * 年月日常量
	 */
	public static final String DATEFORMATE_YYYYMMDD = "yyyyMMdd";

	/**
	 * 日期格式
	 */
	public static final String DATEFORMATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/**
	 * 日期格式
	 */
	public static final String DATEFORMATE_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式
	 */
	public static final String DATEFORMATE_YYYY_MM_DD = "yyyy-MM-dd";

	/**
	 * 日期格式
	 */
	public static final String DATEFORMATE_CN_TIME = "yyyy年MM月dd日 HH:mm:ss";

	/**
	 * 日期格式
	 */
	public static final String DATEFORMATE_CN_DATE = "yyyy年MM月dd日";

	/**
	 * 日期格式
	 */
	public static final String DATEFORMATE_YYYYMMDD_SLASH = "yyyy/MM/dd";

	/**
	 * 日期格式
	 */
	public static final String DATEFORMATE_FLAG_HHMMSS = "HH:mm:ss";

	/**
	 * 日期格式
	 */
	public static final String DATEFORMATE_HHMMSS = "hhMMss";

	/**
	 * 日期格式转换器
	 */
	static final DateFormat YYYYMMDDHHMMSS_FORMAT = new SimpleDateFormat(
			DATEFORMATE_YYYYMMDDHHMMSS);

	/**
	 * 日期格式转换器
	 */
	static final DateFormat YYYYMMDD_FORMAT = new SimpleDateFormat(
			DATEFORMATE_YYYYMMDD);

	/**
	 * 日期格式转换器
	 */
	static final DateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat(
			DATEFORMATE_YYYY_MM_DD_HHMMSS);

	/**
	 * 日期格式转换器
	 */
	static final DateFormat CN_TIME_FORMAT = new SimpleDateFormat(
			DATEFORMATE_CN_TIME);

	/**
	 * 日期格式转换器
	 */
	static final DateFormat YYYY_MM_DD = new SimpleDateFormat(
			DATEFORMATE_YYYY_MM_DD);

	/**
	 * 日期格式转换器
	 */
	static final DateFormat YYYYMMDD_SLASH_FORMAT = new SimpleDateFormat(
			DATEFORMATE_YYYYMMDD_SLASH);

	/**
	 * 日期格式转换器
	 */
	static final DateFormat HHMMSS_FORMAT = new SimpleDateFormat(
			DATEFORMATE_HHMMSS);

	/**
	 * 日期格式转换器
	 */
	static final DateFormat TIME_FORMATE = new SimpleDateFormat(
			"HHmmss");

	/**
	 * 格式化日期函数 内部使用
	 * 
	 * 根据指定格式对当前日期进行格式化
	 * 
	 * @param date
	 *            当前日期
	 * @param format
	 *            需要转化的格式
	 * @return String 转换后的字符串格式日期
	 */
	public static String parseDate(Date date, String format) {

		SimpleDateFormat dateformat = new SimpleDateFormat(format);

		return dateformat.format(date);
	}

	/**
	 * 取当前日期 功能描述： 输入参数：<按照参数定义顺序>
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static java.sql.Date getCurDate() {
		return getDate(getCurTime());
	}



	/**
	 * 取当前日期，格式yyyy-MM-dd HH:mm:ss 功能描述： 输入参数：<按照参数定义顺序>
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static String getFormatCurDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curdate = getCurDate();
		return formatter.format(curdate);
	}

	/**
	 * 根据指定格式获取当前日期 功能描述： 输入参数：<按照参数定义顺序>
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static String getFormatCurDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date curdate = getCurDate();
		return formatter.format(curdate);
	}

	/**
	 * 根据类型 type：year;month;day time:表示时间多长,负数标示往前推
	 * 返回当前时间的固定相比时间
	 * @param format
	 *            最终返还的格式
	 * @return
	 */
	public static String getFormatCurDate(String format, String type,
			int time) {
		Date curdate = getCurDate();//当前时间
		Calendar date = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		date.setTime(curdate);
		if ("year".equals(type)) {
			date.add(Calendar.YEAR, time);
			return formatter.format(date.getTime());
		} else if ("month".equals(type)) {
			date.add(Calendar.MONTH, time);
			return formatter.format(date.getTime());
		} else if ("day".equals(type)) {
			date.add(Calendar.DATE, time);
			return formatter.format(date.getTime());
		} else {
			return formatter.format(curdate);
		}
	}
	
	public static String getFormatCurDate(String format, String type,
			int time,Date curdate) {
		Calendar date = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		date.setTime(curdate);
		if ("year".equals(type)) {
			date.add(Calendar.YEAR, time);
			return formatter.format(date.getTime());
		} else if ("month".equals(type)) {
			date.add(Calendar.MONTH, time);
			return formatter.format(date.getTime());
		} else if ("day".equals(type)) {
			date.add(Calendar.DATE, time);
			return formatter.format(date.getTime());
		} else {
			return formatter.format(curdate);
		}
	}
	
	/**
	 * 把yyyy-MM-dd格式的字符串转换成Date
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDateOfStr(String dateStr) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date da = null;
		try {
			da = df.parse(dateStr);
		} catch (Exception e) {
			logger.error("DateUtil.getDateOfStr,{}",e);
		}
		return da;
	}

	/**
	 * 获取当前日期 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentDateString() {
		return formateDateStr(new Date());
	}

	/**
	 * 获取明天日期 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getTomorrowDateString() {
		return getDateStringDelay(1);
	}

	/**
	 * 获取距离今天 delay 天的日期 yyyy-MM-dd
	 * 
	 * @param delay
	 * @return
	 */
	public static String getDateStringDelay(int delay) {
		return formateDateStr(DateUtil.addDate(new Date(), delay));
	}
	
	/**
	 * 获取距离今天 delay 天的日期 yyyy-MM-dd
	 * 
	 * @param delay
	 * @return
	 */
	public static String getDateStringDelay(Date date,int delay) {
		return formateDateStr(DateUtil.addDate(date, delay));
	}

	/**
	 * 
	 * 功能描述：取当前时间戳 输入参数：<按照参数定义顺序>
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static Timestamp getCurTime() {
		return new Timestamp(Calendar.getInstance().getTime().getTime());
	}

	/**
	 * 
	 * 功能描述：取当前时间，以字符串形式返回 输入参数：<按照参数定义顺序>
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static String getTime() {
		Timestamp time = new Timestamp(Calendar.getInstance().getTime()
				.getTime());
		return time.toString();
	}

	/**
	 * 得到当前日期
	 * 
	 * @param type
	 *            :0日期中间横线;1：有横线
	 * @return
	 */
	public static String getDate(int type) {
		Calendar now = Calendar.getInstance();

		int year = now.get(Calendar.YEAR);

		int month = now.get(Calendar.MONTH) + 1;

		int day = now.get(Calendar.DAY_OF_MONTH);

		String sMonth = String.valueOf(month);

		if (sMonth.length() == 1)
			sMonth = "0" + sMonth;
		String sDay = String.valueOf(day);
		if (sDay.length() == 1)
			sDay = "0" + sDay;
		String tDate = String.valueOf(year) + "-" + sMonth + "-" + sDay;
		if (type == 0)
			tDate = String.valueOf(year) + sMonth + sDay;
		return tDate;
	}

	
	/**
	 * 把日期转换成 yyyyMMdd格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	 public static synchronized String getShortStrDate(Date date) {
		return YYYYMMDD_FORMAT.format(date);
	}

	/**
	 * 
	 * 把日期转换成 yyyyMMddHHmmss格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized String getShortStrDateTime(Date date) {
		return YYYYMMDDHHMMSS_FORMAT.format(date);
	}

	/**
	 * 根据Timestamp获得日期
	 * 
	 * @param time
	 * @return java.sql.Date
	 */
	public static java.sql.Date getDate(Timestamp time) {
		return new java.sql.Date(time.getTime());
	}

	/**
	 * 获得对应时间time的相应field的值。 如获得当前时间的分钟，则调用方式如下 getTimeFieldStr(getCurTime(),
	 * Calendar.MINUTE)
	 * 
	 * @param time
	 * @param field
	 * @return String
	 */
	public static String getTimeFieldStr(Timestamp time, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time.getTime()));
		int fieldValue = calendar.get(field);
		if (field == Calendar.MONTH)
			fieldValue++;
		return String.valueOf(fieldValue);
	}

	/**
	 * 获得时间time对应的中文日期的字符串
	 *
	 * @return String -- 如 2003年5月12日12:12:12
	 */
	public static String getDateTimeCn() {
		DateFormat f3 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return f3.format(new Date());
	}

	/**
	 * 将 Timestamp 对应的日期以 "yyyy-MM-dd" 格式返回一个字符串
	 * 
	 * @param stamp
	 * @return
	 */
	public static String formatDate(Timestamp stamp) {
		if (stamp == null)
			return "";
		return new java.sql.Date(stamp.getTime()).toString();
	}

	/**
	 * 返回前天，昨天，明天，后天等
	 * 
	 * @param diffdate
	 *            于今天相差的天数
	 * @return
	 */
	public static String getCustomDate(int diffdate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curdate = getCurDate();
		long myTime = curdate.getTime() + 1000L * 3600 * 24 * diffdate;
		curdate.setTime(myTime);
		String customDate = formatter.format(curdate);
		return customDate;
	}


	/**
	 * 
	 * 功能描述：根据传入日期格式化，yyyy-MM-dd 输入参数：日期
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static String getFormatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}


	/**
	 * 根据传入日期格式化，yyyy-MM-dd，传入null返回null 功能描述： 日期 输入参数：<按照参数定义顺序>
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static String formateDate(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null) {
			return df.format(date);
		} else {
			return null;
		}
	}

	/**
	 * 格式化日期格式返回字符串
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String formateDate(Date date, DateFormat dateFormat) {
		return dateFormat.format(date);
	}


	/**
	 * 日期格式 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized String formateDateStr(Date date) {
		if (date == null) {
			return "";
		}
		return formateDate(date, YYYY_MM_DD);

	}



	/**
	 * 得到某年某月的第一天
	 * 
	 * @return
	 */
	public static String getMonthFirstDay(int yearAmount,int monthAmount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, yearAmount);
		calendar.add(Calendar.MONTH, monthAmount);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return getFormatDate(calendar.getTime());
	}

	/**
	 * 得到某个月的最后一天
	 * 
	 * @return
	 */
	public static String getMonthLastDay(int yearAmount,int monthAmount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, yearAmount);
		calendar.add(Calendar.MONTH, monthAmount);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getFormatDate(calendar.getTime());
	}
	
	/**
	 * 得到某个月的最后一天
	 * @param dateStr yyyy-MM
	 * @return
	 */
	public static int getDayNumOfMonth(String dateStr) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");  
        calendar = Calendar.getInstance();  
        try {  
        	Date date = format.parse(dateStr);  
            calendar.setTime(date); 
        } catch (Exception e) {  
        	logger.error("DateUtil.getMonthLastDay,{}", e);
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 得到某个月的最后一天
	 * 
	 * @return
	 */
	public static int getDayNumOfMonth(int yearAmount,int monthAmount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, yearAmount);
		calendar.add(Calendar.MONTH, monthAmount);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * 功能描述：根据传入日期获取当月第几天 输入参数：日期date
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static int getDayofMonth(Date date1) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date1);
		return cld.get(5);
	}


	/**
	 * 日期相加
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 日期相加
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相加后的日期
	 */
	public static Date minusDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 日期相加
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, double day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis((long) (getMillis(date) + day * 24 * 3600 * 1000));
		return c.getTime();
	}

	/**
	 * 分钟相加
	 * 
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date addMinute(Date date, double minute) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis((long) (getMillis(date) + minute * 60 * 1000));
		return c.getTime();
	}

	/**
	 * 日期相减
	 * 
	 * @param date
	 *            日期
	 * @param date1
	 *            日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 
	 * 功能描述：小时相减 输入参数：date
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static int diffDateToHour(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (1000 * 60 * 60));
	}

	/**
	 * 功能描述: <br>
	 * 两个日期间隔多少秒
	 *
	 * @param firstDate
	 * @param secondDate
	 * @return
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static int diffSeconds(Date firstDate,
			Date secondDate) {
		return (int) ((getMillis(firstDate) - getMillis(secondDate)) / 1000);
	}

	/**
	 * 返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 判断是否同年同月
	 * 
	 * @param t1
	 *            日期1
	 * @param t2
	 *            日期2
	 * @return
	 */
	public static boolean isSameMonth(Timestamp t1, Timestamp t2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(t1);
		int year1 = cal.get(Calendar.YEAR);
		int month1 = cal.get(Calendar.MONTH);
		logger.info("year1:" + year1 + "month1:" + month1);
		cal.setTime(t2);
		int year2 = cal.get(Calendar.YEAR);
		int month2 = cal.get(Calendar.MONTH);
		logger.info("year2:" + year2 + "month2:" + month2);
		if (year1 == year2 && month1 == month2)
			return true;
		return false;
	}

	/**
	 * 根据传入的数据获取星期
	 * 
	 * @param i
	 * @return
	 */
	public static String getWeek(int i) {
		String result = "";
		switch (i) {
		case 1:
			result = "星期日";
			break;
		case 2:
			result = "星期一";
			break;
		case 3:
			result = "星期二";
			break;
		case 4:
			result = "星期三";
			break;
		case 5:
			result = "星期四";
			break;
		case 6:
			result = "星期五";
			break;
		case 7:
			result = "星期六";
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 
	 * 功能描述：根据传入时间获取时间戳 输入参数：date
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static Timestamp dateToTimestamp(Date date) {
		if (date != null) {
			return new Timestamp(date.getTime());
		}
		return null;
	}

	/**
	 * 计算两个日期相减
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回天数
	 */
	public static int getDay(Date date1, Date date2) {
		Long quot = date2.getTime() - date1.getTime();
		quot = quot / (1000 * 60 * 60 * 24) + 1;
		return quot.intValue();
	}

	// 判断时间s1是否在时间s2之前
	public static boolean compDate(String s1, String s2) {
		int day = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date time1;
		Date time2;
		try {
			time1 = sf.parse(s1);
			time2 = sf.parse(s2);

			day = (int) ((time2.getTime() - time1.getTime()) / 3600 / 24 / 1000);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		if (day > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 功能描述：判断时间s1是否在时间s2之前 输入参数：s1,s2
	 *
	 * @return 返回值
	 * @throw 异常描述
	 */
	public static String compareTo(String s1, String s2) {
		int day = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date time1;
		Date time2;
		try {
			time1 = sf.parse(s1);
			time2 = sf.parse(s2);

			day = (int) ((time2.getTime() - time1.getTime()) / 3600 / 24 / 1000);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		if (day > 0) {
			return s2;
		} else {
			return s1;
		}
	}

	/**
	 * 把日期格式化成中文格式
	 */
	synchronized public static String parseCnDate(Date date) {
		return CN_TIME_FORMAT.format(date);
	}

	public static Time getTime(Timestamp now) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return Time.valueOf(sdf.format(now));
	}

	/**
	 * 把整型转换成时间（如 12345 转换成 01:23:45）
	 * 
	 * @param intValue
	 * @return
	 */
	public static String parseIntValueToTime(Integer intValue) {
		String str = "00:00:00";
		if (intValue != null) {

			String _str = String.valueOf(intValue);
			switch (_str.length()) {
			case 1:
				_str = "00000" + _str;
				break;
			case 2:
				_str = "0000" + _str;
				break;
			case 3:
				_str = "000" + _str;
				break;
			case 4:
				_str = "00" + _str;
				break;
			case 5:
				_str = "0" + _str;
				break;
			case 6:
				break;
			default:
				_str = "000000";
				break;
			}
			str = _str.substring(0, 2) + ":" + _str.substring(2, 4) + ":"
					+ _str.substring(4);
		}
		return str;
	}

	/**
	 * 返回去年的年份
	 * @return
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static int getLastYear() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR) - 1;
		return year;
	}

	public static Date convertToDate(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	
	public static Date convertToDate(String dateStr,String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 返回当前时间的自定义格式String
	 * 
	 * @param dateFormat
	 * @return 自定义格式的日期
	 *         自定义支持的格式有： yyyy-MM-dd HH:mm:ss yyyy年MM月dd日 HH时mm分ss秒 yyyy年MM月dd日
	 *         HH时mm分 yyyy-MM-dd HH:mm yyyyMMddHH:mm:ss yyyy-MM-dd yyyyMMdd
	 *         HHmmss yyyy年MM月dd日 HH:mm:ss" HH时mm分ss秒 ......(很多)
	 */
	public static String getDefinableTime(String dateFormat) {
		if (null == dateFormat || "".equals(dateFormat.trim())) {
			return "";
		}
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 
	 * 日期转换成指定格式的字符串
	 *
	 * @param date
	 * @param formatStr
	 * @return
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static String getDateStrByFormat(Date date, String formatStr) {
		if (null == date || null == formatStr || "".equals(formatStr.trim())) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}
	
	/**
	 * 某段时间内的所有日期: <br>
	 * 〈某段时间内的所有日期〉
	 *
	 * @param startDate 2018-05-26
	 * @param endDate 2018-06-26
	 * @return List<String>
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static List<String> getRangeDates(String startDate, String endDate) {
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		List<String> list = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date daystart;
		Date dayend;
		try {
			daystart = df.parse(startDate);
			dayend = df.parse(endDate);
			beginCalendar.setTime(daystart); 
			endCalendar.setTime(dayend);
		} catch (ParseException e) {
			logger.error("DateUtil.getRangeDates,{}", e);
		}
		while(beginCalendar.compareTo(endCalendar) <= 0) {
			list.add(df.format(beginCalendar.getTime()));
			beginCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return list;
	}
}
