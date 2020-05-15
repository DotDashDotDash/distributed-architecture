package org.codwh.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("all")
public class DateUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final long MILLI_SECONDS_ONE_DAY = 1000 * 3600 * 24;
    private static final long SECONDS_ONE_DAY = 3600 * 24;
    private static final long MINUTES_ONE_DAY = 60 * 24;
    private static final long HOURS_ONE_DAY = 24;
    /**
     * 工作日
     */
    private static final String DAY_TYPE_WORKDAY = "workday";
    /**
     * 周末
     */
    private static final String DAY_TYPE_WEEKEND = "weekend";
    private static DateFormat dateFormatter;
    private static DateFormat dateTimeFormatter;
    private static DateFormat timeFormatter;

    static {
        dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        dateTimeFormatter = new SimpleDateFormat(DATETIME_FORMAT);
        timeFormatter = new SimpleDateFormat(TIME_FORMAT);
    }

    /**
     * 根据 yyyy-MM-dd 字符串解析成相应的日期
     *
     * @param strDate yyyy-MM-dd 格式的日期
     * @return 转换后的日期
     */
    public static Date parseDate(String strDate) {
        Date date = null;
        try {
            date = dateFormatter.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
        return date;
    }

    /**
     * 根据传入的日期格式 将字符串解析成 日期类型
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 日期类型
     */
    public static Date parseDate(String strDate, String pattern) {
        Date date = null;
        try {
            DateFormat format = getDateFormat(pattern);
            date = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
        return date;
    }

    /**
     * 根据传入的格式，获取日期格式化实例，如果传入格式为空，则为默认格式 yyyy-MM-dd
     *
     * @param pattern 日期格式
     * @return 格式化实例
     */
    public static DateFormat getDateFormat(String pattern) {
        if (null == pattern || "".equals(pattern.trim())) {
            return new SimpleDateFormat(DATE_FORMAT);
        } else {
            return new SimpleDateFormat(pattern);
        }
    }

    /**
     * 根据 HH:mm:ss 字符串解析成相应的时间格式
     *
     * @param strTime HH:mm:ss 格式的时间格式
     * @return 转换后的时间
     */
    public static Date parseTime(String strTime) {
        Date date = null;
        try {
            date = timeFormatter.parse(strTime);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
        return date;
    }

    /**
     * 获取系统当前时间
     *
     * @return 系统当前时间
     */
    public static Date getCurrentDate() {
        Calendar gc = GregorianCalendar.getInstance();
        return gc.getTime();
    }

    /**
     * 得到日期所在月份的开始日期（第一天的开始日期），例如2004-1-15 15:10，转换后为2004-1-1 00:00
     *
     * @param date 需要转换的日期
     * @return 日期所在月份的开始日期
     */
    public static Date getMonthBegin(Date date) {
        Calendar gc = GregorianCalendar.getInstance();
        gc.setTime(date);
        int year = gc.get(Calendar.YEAR);
        int mon = gc.get(Calendar.MONTH);
        Calendar gCal = new GregorianCalendar(year, mon, 1);
        return gCal.getTime();
    }

    /**
     * 根据日期所在的星期，得到这个星期的开始日期，注意，每周从星期日开始计算
     *
     * @param date 需要转换的日期
     * @return 传入日期所在周的第一天的零点整
     */
    public static Date getWeekBegin(Date date) {
        Calendar gCal = GregorianCalendar.getInstance();
        gCal.setTime(date);
        int startDay = gCal.getActualMinimum(Calendar.DAY_OF_WEEK);
        gCal.set(Calendar.DAY_OF_WEEK, startDay);
        return gCal.getTime();
    }

    /**
     * 根据日期所在的星期，得到下周开始第一天的零点整
     *
     * @param date 需要转换的日期
     * @return 传入日期的下周开始第一天的零点整
     */
    public static Date getWeekEnd(Date date) {
        Calendar gCal = GregorianCalendar.getInstance();
        gCal.setTime(date);
        int lastDay = gCal.getActualMaximum(Calendar.DAY_OF_WEEK);
        gCal.set(Calendar.DAY_OF_WEEK, lastDay);
        return getTodayEnd(gCal.getTime());
    }

    /**
     * 得到日期的结束日期，例如2004-1-1 15:12，转换后为2004-1-2 00:00，注意为第二天的0点整
     *
     * @param date 所要转换的日期
     * @return 为第二天的零点整
     */
    public static Date getTodayEnd(Date date) {
        Calendar gc = GregorianCalendar.getInstance();
        gc.setTime(dateDayAdd(date, 1));
        return getTodayStart(gc.getTime());
    }

    /**
     * 在指定的日期基础上，增加或是减少天数
     *
     * @param date 指定的日期
     * @param days 需要增加或是减少的天数，正数为增加，负数为减少
     * @return 增加或是减少后的日期
     */
    public static Date dateDayAdd(Date date, int days) {
        long now = date.getTime() + days * MILLI_SECONDS_ONE_DAY;
        return (new Date(now));
    }

    /**
     * 得到日期的起始日期，例如2004-1-1 15:12，转换后为 2004-1-1 00:00
     *
     * @param date 需要转换的日期
     * @return 该日期的零点
     */
    public static Date getTodayStart(Date date) {
        Calendar gc = GregorianCalendar.getInstance();
        gc.setTime(date);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        return gc.getTime();
    }

    /**
     * 根据年、月返回由年、月构成的日期的下一个月第一天零点整
     *
     * @param year  所在的年
     * @param month 所在的月份，从1月到12月
     * @return 下一个月第一天零点整
     */
    public static Date getMonthEnd(int year, int month) {
        Date start = getMonthBegin(year, month);
        return getMonthEnd(start);
    }

    /**
     * 根据年、月返回由年、月构成的日期的月份开始日期
     *
     * @param year  所在的年
     * @param month 所在的月份，从1月到12月
     * @return 由年、月构成的日期的月份开始日期
     */
    public static Date getMonthBegin(int year, int month) {
        if (month <= 0 || month > 12) {
            throw new IllegalArgumentException("month must from 1 to 12");
        }
        Calendar gc = new GregorianCalendar(year, month - 1, 1);
        return gc.getTime();
    }

    /**
     * 根据日期所在的月份，得到下个月的第一天零点整
     *
     * @param date 需要转换的日期
     * @return 对应日期的下个月的第一天零点整
     */
    public static Date getMonthEnd(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return getTodayEnd(cal.getTime());
    }

    /**
     * 根据日期所在的年份，得到当年的开始日期，为每年的1月1日零点整
     *
     * @param date 需要转换的日期
     * @return 当年的开始日期，为每年的1月1日零点整
     */
    public static Date getYearBegin(Date date) {
        Calendar gCal = GregorianCalendar.getInstance();
        gCal.setTime(date);
        gCal.set(Calendar.DAY_OF_YEAR, 1);
        return getTodayStart(gCal.getTime());
    }

    /**
     * 根据日期所在的年份，得到当年的结束日期，为来年的1月1日零点整
     *
     * @param date 需要转换的日期
     * @return 来年的1月1日零点整
     */
    public static Date getYearEnd(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        int lastday = cal.getActualMaximum(GregorianCalendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, lastday);
        return getTodayEnd(cal.getTime());
    }

    /**
     * 转换日期为 yyyy-MM-dd 格式的字符串
     *
     * @param date 需要转换的日期
     * @return 转换后的日期字符串
     */
    public static String formatDate(Date date) {
        return dateFormatter.format(date);
    }

    /**
     * 转换指定日期成时间格式 HH:mm:ss 格式的字符串
     *
     * @param date 指定的时间
     * @return 转换后的字符串
     */
    public static String formatTime(Date date) {
        return timeFormatter.format(date);
    }

    /**
     * 转换指定日期成 yyyy-MM-dd HH:mm:ss 格式的字符串
     *
     * @param date 需要转换的日期
     * @return 转换后的字符串
     */
    public static String formatDateTime(Date date) {
        return dateTimeFormatter.format(date);
    }

    /**
     * 根据指定的转化模式，转换日期成字符串
     *
     * @param date    需要转换的日期
     * @param pattern 日期格式 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 转换后的字符串
     */
    public static String formatDate(Date date, String pattern) {
        DateFormat formater = getDateFormat(pattern);
        return formater.format(date);
    }

    /**
     * 在指定日期的基础上，增加或是减少月份信息，如1月31日，增加一个月后，则为2月28日（非闰年）
     *
     * @param date   指定的日期
     * @param months 增加或是减少的月份数，正数为增加，负数为减少
     * @return 增加或是减少后的日期
     */
    public static Date dateMonthAdd(Date date, int months) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        int m = cal.get(Calendar.MONTH) + months;
        if (m < 0) {
            m += -12;
        }
        cal.roll(Calendar.YEAR, m / 12);
        cal.roll(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 在指定的日期基础上，增加或是减少年数
     *
     * @param date 指定的日期
     * @param year 需要增加或是减少的年数，正数为增加，负数为减少
     * @return 增加或是减少后的日期
     */
    public static Date dateYearAdd(Date date, int year) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.roll(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 得到日期的年数
     *
     * @param date 指定的日期
     * @return 返回指定日期的年数
     */
    public static int getDateYear(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 得到指定日期的月份数
     *
     * @param date 指定的日期
     * @return 返回指定日期的月份数
     */
    public static int getDateMonth(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONDAY);
    }

    /**
     * 得到制定日期在当前天数，例如2004-5-20日，返回141
     *
     * @param date 指定的日期
     * @return 返回的天数
     */
    public static int getDateYearDay(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 得到制定日期在当前月中的天数，例如2004-5-20日，返回20
     *
     * @param date 指定的日期
     * @return 返回天数
     */
    public static int getDateMonthDay(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到指定日期的年份
     *
     * @param date 指定的日期
     * @return 日期的年份
     */
    public static int getYear(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 得到指定日期在当在一年中的月份数，从1开始
     *
     * @param date 指定的日期
     * @return 指定日期在当在一年中的月份数
     */
    public static int getMonth(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 得到指定日期在当在一月中的号数，从1开始
     *
     * @param date 指定的日期
     * @return 日期在当在一月中的号数
     */
    public static int getDay(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到指定日期在当前星期中的天数，例如2004-5-20日，返回5，
     * <p>
     * 每周以周日为开始按1计算，所以星期四为5
     *
     * @param date 指定的日期
     * @return 返回天数
     */
    public static int getDateWeekDay(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 得到指定日期在当前周内是第几天 (周一开始)
     *
     * @param date 指定日期
     * @return 周内天书
     */
    public static int getWeek(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return ((cal.get(Calendar.DAY_OF_WEEK) - 1) + 7) % 7;
    }

    /**
     * 计算两个时间之间的时间差
     *
     * @param from 开始
     * @param to   结束
     * @return 时间差
     */
    public static long calculateTimeInMillis(Date from, Date to) {
        Calendar fromCal = getCalendar(from);
        Calendar toCal = getCalendar(to);
        if (fromCal.after(toCal)) {
            fromCal.setTime(to);
            toCal.setTime(from);
        }
        return toCal.getTimeInMillis() - fromCal.getTimeInMillis();
    }

    /**
     * 获取Calendar实例
     *
     * @param date 日期类型
     * @return
     */
    public static Calendar getCalendar(Date date) {
        Calendar gc = GregorianCalendar.getInstance();
        gc.setTime(date);
        return gc;
    }

    /**
     * 根据 yyyyMMdd HH:mm 日期格式，转换成数据库使用的时间戳格式
     *
     * @param strTimestamp 以 yyyyMMdd HH:mm 格式的时间字符串
     * @return 转换后的时间戳
     */
    public static java.sql.Timestamp parseTimestamp(String strTimestamp) {
        return new java.sql.Timestamp(parseDateTime(strTimestamp).getTime());
    }

    /**
     * 根据yyyy-MM-dd HH:mm字符串解析成相应的日期时间
     *
     * @param strDateTime 以"yyyy-MM-dd HH:mm"为格式的时间字符串
     * @return 转换后的日期
     */
    public static Date parseDateTime(String strDateTime) {
        Date date = null;
        try {
            date = dateTimeFormatter.parse(strDateTime);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
        return date;
    }

    /**
     * 根据传入的日期格式 获取系统的前一天时间
     *
     * @param pattern 日期格式 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 系统的前一天时间
     */
    public static String getPreviousDate(String pattern) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date date = cal.getTime();
        DateFormat dateFormat = getDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 根据传入的日期格式 获取当前系统时间
     *
     * @param pattern 日期格式 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 对应格式的当前系统时间
     */
    public static String getCurrentDate(String pattern) {
        Calendar cal = GregorianCalendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = getDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 根据传入的日期格式 获取系统的明天时间
     *
     * @param pattern 日期格式 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 系统的明天时间
     */
    public static String getNextDate(String pattern) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date date = cal.getTime();
        DateFormat dateFormat = getDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 返回指定日期和其后n天日期list
     *
     * @param date    日期
     * @param n       天数 负数就是往前推，正数即往后推
     * @param pattern 格式化字符串 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 日期集合
     */
    public static List<String> getDates(String date, int n, String pattern) {
        List<String> dayList = new ArrayList<String>();
        String toDate = moveDate(date, n, pattern);
        dayList = getDates(date, toDate, pattern);
        return dayList;
    }

    /**
     * 获取指定日期的前或后推N天（字符串类型），如果传入的参数为空，则返回空
     *
     * @param date    日期
     * @param n       跳转天数 负数就是往前推，正数即往后推
     * @param pattern 日期的格式 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 返回字符型日期
     */
    public static String moveDate(String date, int n, String pattern) {
        if (n == 0) {
            return date;
        }
        if (date == null || date.trim().equals("")) {
            throw new IllegalArgumentException("传入的日期不能为空！");
        }
        String str = "";
        DateFormat dateFormat = getDateFormat(pattern);
        try {
            Date ddate = dateFormat.parse(date);
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(ddate);
            cal.add(Calendar.DAY_OF_MONTH, n);
            Date tmp = cal.getTime();
            str = dateFormat.format(tmp);
        } catch (ParseException e) {
            e.printStackTrace();
            return str;
        }
        return str;
    }

    /**
     * 取得两个日期之间的所有日期集合，包含起始日期和结束日期， 日期不分前后顺序，支持跨年。
     *
     * @param dateO   起始日期
     * @param dateT   结束日期
     * @param pattern 格式化字符串 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 日期集合
     */
    public static List<String> getDates(String dateO, String dateT, String pattern) {
        if ((dateO == null) || (dateT == null) || dateO.trim().equals("")
                || dateT.trim().equals("")) {
            throw new IllegalArgumentException("传入的日期不能为空！");
        }
        List<String> list = new ArrayList<String>();
        try {
            DateFormat format = getDateFormat(pattern);
            Date dO = format.parse(dateO);
            Date dT = format.parse(dateT);
            Calendar calO = GregorianCalendar.getInstance();
            Calendar calT = GregorianCalendar.getInstance();
            if (dO.after(dT)) {
                calO.setTime(dT);
                calT.setTime(dO);
            } else {
                calO.setTime(dO);
                calT.setTime(dT);
            }
            while (!calO.after(calT)) {
                list.add(format.format(calO.getTime()));
                calO.add(GregorianCalendar.DATE, +1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }

    /**
     * 获取两个日期之间的天数， 日期不分前后顺序，支持跨年。
     *
     * @param dateO   日期1
     * @param dateT   日期2
     * @param pattern 格式化字符串 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 两个日期之间的天数
     */
    public static int getDayCountBetween2Days(String dateO, String dateT, String pattern) {
        if ((dateO == null) || (dateT == null) || dateO.trim().equals("")
                || dateT.trim().equals("")) {
            throw new IllegalArgumentException("传入的日期不能为空！");
        }
        int count = 0;
        try {
            DateFormat format = getDateFormat(pattern);
            Date dO = format.parse(dateO);
            Date dT = format.parse(dateT);
            Calendar calO = GregorianCalendar.getInstance();
            Calendar calT = GregorianCalendar.getInstance();
            if (dO.after(dT)) {
                calO.setTime(dT);
                calT.setTime(dO);
            } else {
                calO.setTime(dO);
                calT.setTime(dT);
            }
            while (!calO.after(calT)) {
                count++;
                calO.add(GregorianCalendar.DATE, +1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return count;
        }
        return count;
    }

    /***
     * 获取传入日期的同类型日， 正数为未来周中，负数为历史周中
     *
     * @param date

     *            传入日期

     * @param n

     *            正数为未来周中，负数为历史周中

     * @param pattern

     *            日期的格式 如果传入格式为空，则为默认格式 yyyy-MM-dd

     *

     * @return 同类型日

     */
    public static String getSimilarDate(String date, int n, String pattern) {
        if (n == 0) {
            return date;
        }
        if (date == null || date.equals("")) {
            throw new IllegalArgumentException("传入的日期不能为空！");
        }
        String str = "";
        DateFormat dateFormat = getDateFormat(pattern);
        try {
            Date ddate = dateFormat.parse(date);
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(ddate);
            int count = n * 7;
            cal.add(Calendar.DAY_OF_MONTH, count);
            Date tmp = cal.getTime();
            str = dateFormat.format(tmp);
        } catch (ParseException e) {
            e.printStackTrace();
            return str;
        }
        return str;
    }

    /**
     * 获取一段时间内，对应日期的所有同类型日 日期不分前后顺序，支持跨年。<br>
     *
     * @param dateO       日期1
     * @param dateT       日期2
     * @param day_of_week 目标日期
     * @param pattern     目标日期格式化字符串 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 目标日期的同类型日列表（pattern格式）
     */
    public static List<String> getSimilarDates(String dateO, String dateT, String day_of_week,
                                               String pattern) {
        if ((null == dateO) || (null == dateT) || (null == day_of_week) || dateO.trim().equals("")
                || dateT.trim().equals("") || day_of_week.trim().equals("")) {
            throw new IllegalArgumentException("传入的日期不能为空！");
        }
        List<String> list = new ArrayList<String>();
        try {
            DateFormat format = getDateFormat(pattern);
            Date targetDate = format.parse(day_of_week);
            Calendar targetCal = GregorianCalendar.getInstance();
            targetCal.setTime(targetDate);
            int _target = targetCal.get(Calendar.DAY_OF_WEEK);
            Date dO = format.parse(dateO);
            Date dT = format.parse(dateT);
            Calendar calO = GregorianCalendar.getInstance();
            Calendar calT = GregorianCalendar.getInstance();
            if (dO.after(dT)) {
                calO.setTime(dT);
                calT.setTime(dO);
            } else {
                calO.setTime(dO);
                calT.setTime(dT);
            }
            while (!calO.after(calT)) {
                if (calO.get(Calendar.DAY_OF_WEEK) == _target) {
                    list.add(format.format(calO.getTime()));
                }
                calO.add(GregorianCalendar.DATE, +1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }

    /**
     * 获取一段时间内，对应日期的所有同类型日 日期不分前后顺序，支持跨年。
     *
     * @param dateO       日期1
     * @param dateT       日期2
     * @param day_of_week 目标日期 Calendar.DAY_OF_WEEK;
     * @param pattern     格式化字符串 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 目标日期的同类型日列表（pattern格式）
     */
    public static List<String> getSimilarDates(String dateO, String dateT, int day_of_week,
                                               String pattern) {
        if ((null == dateO) || (null == dateT) || dateO.trim().equals("")
                || dateT.trim().equals("")) {
            throw new IllegalArgumentException("传入的日期不能为空！");
        }
        if (0 == day_of_week) {
            throw new IllegalArgumentException("目标日起录入错误，目标日期为 1~7 的整数");
        }

        List<String> list = new ArrayList<String>();
        try {
            DateFormat format = getDateFormat(pattern);
            Date dO = format.parse(dateO);
            Date dT = format.parse(dateT);
            Calendar calO = GregorianCalendar.getInstance();
            Calendar calT = GregorianCalendar.getInstance();
            if (dO.after(dT)) {
                calO.setTime(dT);
                calT.setTime(dO);
            } else {
                calO.setTime(dO);
                calT.setTime(dT);
            }
            while (!calO.after(calT)) {
                if (calO.get(Calendar.DAY_OF_WEEK) == day_of_week) {
                    list.add(format.format(calO.getTime()));
                }
                calO.add(GregorianCalendar.DATE, +1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }

    /**
     * 获取一段时间内，所有双休日 日期不分前后顺序，支持跨年。
     *
     * @param dateO   日期1
     * @param dateT   日期2
     * @param pattern 格式化字符串 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 所有双休日列表
     */
    public static List<String> getWeekends(String dateO, String dateT, String pattern) {
        return getDates(dateO, dateT, pattern, DAY_TYPE_WEEKEND);
    }

    /**
     * 获取一段时间内，所有双休日 日期不分前后顺序，支持跨年。
     *
     * @param dateO   日期1
     * @param dateT   日期2
     * @param pattern 格式化字符串 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @param type    日期类型 -- DAY_TYPE_WORKDAY（工作日）、 DAY_TYPE_WEEKEND（周末）
     * @return pattern格式的日期列表
     */
    private static List<String> getDates(String dateO, String dateT, String pattern, String type) {
        if ((null == dateO) || (null == dateT) || dateO.trim().equals("")
                || dateT.trim().equals("")) {
            throw new IllegalArgumentException("传入的日期不能为空！");
        }
        List<String> list = new ArrayList<String>();
        try {
            DateFormat format = getDateFormat(pattern);
            Date dO = format.parse(dateO);
            Date dT = format.parse(dateT);
            Calendar calO = GregorianCalendar.getInstance();
            Calendar calT = GregorianCalendar.getInstance();
            if (dO.after(dT)) {
                calO.setTime(dT);
                calT.setTime(dO);
            } else {
                calO.setTime(dO);
                calT.setTime(dT);
            }
            while (!calO.after(calT)) {
                if (type.equalsIgnoreCase(DAY_TYPE_WEEKEND)) {
                    if ((calO.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                            || (calO.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                        list.add(format.format(calO.getTime()));
                    }
                } else if (type.equalsIgnoreCase(DAY_TYPE_WORKDAY)) {
                    if ((calO.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                            && (calO.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
                        list.add(format.format(calO.getTime()));
                    }
                }
                calO.add(GregorianCalendar.DATE, +1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }

    /**
     * 获取一段时间内，所有工作日 日期不分前后顺序，支持跨年。
     *
     * @param dateO   日期1
     * @param dateT   日期2
     * @param pattern 格式化字符串 如果传入格式为空，则为默认格式 yyyy-MM-dd
     * @return 所有工作日列表
     */
    public static List<String> getWorkdays(String dateO, String dateT, String pattern) {
        return getDates(dateO, dateT, pattern, DAY_TYPE_WORKDAY);
    }
}
