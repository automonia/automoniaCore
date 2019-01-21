package com.automonia.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public enum DateUtils {

    singleton;


    /**
     * 日期时间格式对象
     */
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期格式对象
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 时间格式对象
     */
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");


    /**
     * yyyyMMddHHmmss  yyyy-MM-dd HH:mm:ss
     * 将Date对象转换为特定格式的字符串，
     * 不建议过多调用这个函数，如果默认的时间格式符合，就调相应的函数
     *
     * @param pattern 转换格式
     * @return 转换后的时间字符串
     */
    public String getStringWithPattern(Date date, String pattern) {
        return (new SimpleDateFormat(pattern)).format(date);
    }

    /**
     * 将日期对象转成日期时间格式的字符串
     *
     * @param date 日期对象
     * @return 日期时间字符串
     */
    public String getDateTimeString(Date date) {
        if (date == null) {
            return null;
        }
        return dateTimeFormat.format(date);
    }

    /**
     * 将日期对象转成日期格式的字符串
     *
     * @param date 日期对象
     * @return 日期字符串
     */
    public String getDateString(Date date) {
        if (date == null) {
            return null;
        }
        return dateFormat.format(date);
    }

    /**
     * 将日期对象转成时间格式的字符串
     *
     * @param date 日期对象
     * @return 时间字符串
     */
    public String getTimeString(Date date) {
        return timeFormat.format(date);
    }

    /**
     * 对日期时间字符串解析转成相应的日期对象
     * <p>
     * 这个函数会做判定匹配，如果知道特定的格式，请勿调用该函数
     * 不建议多次调用
     *
     * @param dateStr 日期时间字符串
     * @return 日期对象
     */
    public Date getAllFormatDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        // 将所有.xxx开头的时间格式的字符串都返回null进行处理
        if (dateStr.startsWith(".")) {
            return null;
        }
        try {
            if (dateStr.matches("\\d{1,4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.\\d")) {
                return dateTimeFormat.parse(dateStr.substring(0, 19));
            } else if (dateStr.matches("\\d{1,4}-\\d{1,2}-\\d{1,2}")) {
                return dateFormat.parse(dateStr);
            } else if (dateStr.matches("\\d{1,4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                return dateTimeFormat.parse(dateStr);
            } else if (dateStr.matches("\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                return timeFormat.parse(dateStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对日期格式字符串进行解析生成日期对象
     *
     * @param dateStr 日期字符串
     * @return 日期对象
     * @throws ParseException
     */
    public Date getDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getDate(String dateStr, String format) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对日期时间格式字符串进行解析生成日期时间对象
     *
     * @param dateTimeStr 日期时间字符串
     * @return 日期时间对象
     * @throws ParseException
     */
    public Date getDateTime(String dateTimeStr) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        try {
            return dateTimeFormat.parse(dateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对时间格式字符串进行解析生成时间对象
     *
     * @param timeStr 时间字符串
     * @return 时间对象
     * @throws ParseException
     */
    public Date getTime(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            return null;
        }
        try {
            return timeFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间days天后的时间
     *
     * @param days 控制往后的天数,
     * @return 时间对象
     */
    public Date getDateAfterDay(Integer days) {
        if (days == null) {
            return new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public Date getDateAfterMin(Integer minute) {
        if (minute == null) {
            return new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 获取两个日期之间相差的天数
     *
     * @param startDay 开始日期
     * @param endDay   结束日期
     * @return 相差的天数
     */
    public Integer getDaysBetween(Date startDay, Date endDay) {
        if (startDay == null || endDay == null) {
            return null;
        }
        long dayMillis = endDay.getTime() - startDay.getTime();
        Long days = dayMillis / (1000 * 3600 * 24);
        return days.intValue();
    }

    public Integer getDaysBetweenInDay(Date startDay, Date endDay) {
        if (startDay == null || endDay == null) {
            return null;
        }
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.setTime(startDay);
        endCalendar.setTime(endDay);

        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);

        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);

        return getDaysBetween(startCalendar.getTime(), endCalendar.getTime());
    }

    public Date getFirstDayInDay() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public Date getLastDayInDay() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * @return 获取所在周的第一天
     */
    public Date getFirstDayInWeek() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    /**
     * @return 获取所在周的最后一天
     */
    public Date getLastDayInWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayInWeek());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7);
        return calendar.getTime();
    }

    /**
     * @return 获取所在月的第一天
     */
    public Date getFirstDayInMonth() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * @return 获取所在月的最后一天
     */
    public Date getLastDayInMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        return calendar.getTime();
    }

    /**
     * @return 获取所在季度的第一天
     */
    public Date getFirstDayInQuarter() {
        Calendar calendar = Calendar.getInstance();
        Integer currentMonth = calendar.get(Calendar.MONTH) + 1;

        if (currentMonth >= 1 && currentMonth <= 3) {
            calendar.set(Calendar.MONTH, 0);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            calendar.set(Calendar.MONTH, 3);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            calendar.set(Calendar.MONTH, 6);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            calendar.set(Calendar.MONTH, 9);
        }
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * @return 获取所在季度的最后一天
     */
    public Date getLastDayInQuarter() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayInQuarter());
        calendar.add(Calendar.MONTH, 3);
        return calendar.getTime();
    }

    /**
     * @return 获取所在年份的第一天
     */
    public Date getFirstDayInYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    /**
     * @return 获取所在年份的最后一天
     */
    public Date getLastDayInYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayInYear());
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 获取时间的个性化阅读信息
     * x天前 ／ 刚刚(1天内)
     *
     * @param date 时间
     * @return
     */
    public String getTimeLine(Date date) {
        if (date == null) {
            return "";
        }
        Integer days = getDaysBetweenInDay(date, new Date());
        return (days == 0 ? "刚刚" : (days + "天前"));
    }

    /**
     * 获取date所在的星期。
     *
     * @param date 日期对象
     * @return 获取date所在的星期。
     */
    public String getWeekInfo(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week <= 0) {
            return "周日";
        } else if (week == 1) {
            return "周一";
        } else if (week == 2) {
            return "周二";
        } else if (week == 3) {
            return "周三";
        } else if (week == 4) {
            return "周四";
        } else if (week == 5) {
            return "周五";
        } else {
            return "周六";
        }
    }

//    /**
//     * 是否是今天
//     *
//     * @param date 日期
//     * @return true 今天。 false 不是今天
//     */
//    public Boolean isToday(Date date) {
//        if (date == null) {
//            return false;
//        }
//
//        String todayString = getDateString(new Date());
//        String dateString = getDateString(date);
//
//        return todayString.equals(dateString);
//    }

//    /**
//     * 是否是昨天
//     *
//     * @param date 日期
//     * @return true 昨天， false 不是昨天
//     */
//    public Boolean isYesterday(Date date) {
//        if (date == null) {
//            return false;
//        }
//
//        Date yesterday = getDateAfterDay(-1);
//        String yesterdayString = getDateString(yesterday);
//        String dateString = getDateString(date);
//
//        return yesterdayString.equals(dateString);
//    }

}
