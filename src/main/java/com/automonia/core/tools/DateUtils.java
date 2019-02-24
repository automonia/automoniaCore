package com.automonia.core.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @作者 温腾
 * @创建时间 2019年01月25日 21:06
 */
public enum DateUtils {
    singleton;

    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //////////////////////

    public String getStringWithPattern(Date date, String pattern) {
        return (new SimpleDateFormat(pattern)).format(date);
    }

    /**
     * 判断expireDate相对于referenceDate是否过期了。
     * referenceDate为空情况下，则与当前时间进行比较
     *
     * @param expireDate    失效时间
     * @param referenceDate 衡量失效的时间
     */
    public Boolean dateIsExpire(Date expireDate, Date referenceDate) {
        if (expireDate == null) {
            return false;
        }

        if (referenceDate == null) {
            referenceDate = new Date();
        }
        return expireDate.before(referenceDate);
    }

    /**
     * 判断expireDate相对于当前时间而言是否过期了
     *
     * @param expireDate 失效时间
     * @return true 过期了，false 未过期
     */
    public Boolean dateIsExpire(Date expireDate) {
        return dateIsExpire(expireDate, new Date());
    }

    /**
     * 将日期对象转化为字符串，格式是 yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期对象
     * @return 日期对象的字符串格式内容
     */
    public String getDateTimeString(Date date) {
        if (date == null) {
            return null;
        }
        return dateTimeFormat.format(date);
    }


    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串转化为日期对象
     *
     * @param dateStr 日期的字符串
     * @return 日期对象
     */
    public Date getDateTime(String dateStr) {
        if (StringUtils.singleton.isEmpty(dateStr)) {
            return null;
        }
        try {
            return dateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取当前时间days天后的时间
     */
    public Date getDateAfterDay(Date date, Integer days) {
        if (days == null) {
            return null;
        }
        if (date == null) {
            date = new Date();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 获取date时间 + minute分钟后的时间对象
     */
    public Date getDateAfterMin(Date date, Integer minute) {
        if (minute == null) {
            return null;
        }
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }


    /**
     * 获取date时间 + month月后的时间对象
     */
    public Date getDateAfterMonth(Date date, Integer month) {
        if (month == null || date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 获取当天的最开始时间的日期对象
     */
    public Date getFirstDayInDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取当天时间对象的最后时间的日期对象
     */
    public Date getLastDayInDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * @return 获取所在周的第一天
     */
    public Date getFirstDayInWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    /**
     * @return 获取所在周的最后一天
     */
    public Date getLastDayInWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayInWeek(date));
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7);
        return calendar.getTime();
    }

    /**
     * @return 获取所在月的第一天
     */
    public Date getFirstDayInMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * @return 获取所在月的最后一天
     */
    public Date getLastDayInMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        return calendar.getTime();
    }

    /**
     * @return 获取所在季度的第一天
     */
    public Date getFirstDayInQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
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
    public Date getLastDayInQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayInQuarter(date));
        calendar.add(Calendar.MONTH, 3);
        return calendar.getTime();
    }

    /**
     * @return 获取所在年份的第一天
     */
    public Date getFirstDayInYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    /**
     * @return 获取所在年份的最后一天
     */
    public Date getLastDayInYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayInYear(date));
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }


}










