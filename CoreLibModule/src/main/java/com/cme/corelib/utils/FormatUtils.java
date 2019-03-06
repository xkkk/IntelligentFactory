package com.cme.corelib.utils;

import android.content.Context;

import com.cme.corelibmodule.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xiaozi on 2017/5/4.
 *
 */

public class FormatUtils {

    public static String longToDateCommen(long l, String style) {
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        String format = sdf.format(new Date(l));
        return format;
    }

    public static String longToDateCommen(long l) {
        return longToDateCommen(l, "yyyy-MM-dd");
    }

    public static String getDetailTime() {
        return longToDateCommen(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static long stringToLong(String from) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long time = sdf.parse(from).getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static long stringToLong(String from, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long time = sdf.parse(from).getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * 时间戳格式转换
     */
    private static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static String getConvTime(long timesamp) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat = "M月d日";
        String yearTimeFormat = "yyyy年M月d日";
        String am_pm = "";
        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨";
        } else if (hour >= 6 && hour < 12) {
            am_pm = "早上";
        } else if (hour == 12) {
            am_pm = "中午";
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午";
        } else if (hour >= 18) {
            am_pm = "晚上";
        }
        timeFormat = "M月d日 ";
        yearTimeFormat = "yyyy年M月d日";

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = am_pm+getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天";
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1];
                            } else {
                                result = getTime(timesamp, timeFormat);
                            }
                        } else {
                            result = getTime(timesamp, timeFormat);
                        }
                        break;
                    default:
                        result = getTime(timesamp, timeFormat);
                        break;
                }
            } else {
                result = getTime(timesamp, timeFormat);
            }
        } else {
            result = getYearTime(timesamp, yearTimeFormat);
        }
        return result;
    }

    public static String getNewChatTime(long timesamp) {
        String result;
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat = "M月d日";
        String yearTimeFormat = "yyyy年M月d日";
        String am_pm = "";
        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = " 凌晨";
        } else if (hour >= 6 && hour < 12) {
            am_pm = " 早上";
        } else if (hour == 12) {
            am_pm = " 中午";
        } else if (hour > 12 && hour < 18) {
            am_pm = " 下午";
        } else if (hour >= 18) {
            am_pm = " 晚上";
        }

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = am_pm + getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 " + am_pm + getHourAndMin(timesamp);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1]+" " + getHourAndMin(timesamp);
                            } else {
                                result = getTime(timesamp, timeFormat) + am_pm + getHourAndMin(timesamp);
                            }
                        } else {
                            result = getTime(timesamp, timeFormat) + am_pm + getHourAndMin(timesamp);
                        }
                        break;
                    default:
                        result = getTime(timesamp, timeFormat) + am_pm + getHourAndMin(timesamp);
                        break;
                }
            } else {
                result = getTime(timesamp, timeFormat) + am_pm + getHourAndMin(timesamp);
            }
        } else {
            result = getYearTime(timesamp, yearTimeFormat) + am_pm + getHourAndMin(timesamp);
        }
        return result;
    }

    /**
     * 当天的显示时间格式
     *
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 当天的显示时间格式
     *
     * @param duration
     * @return
     */
    public static String getVideoDurationText(int duration) {
        if (duration>=10) {
            return "0:"+duration;
        }else {
            return "0:0"+duration;
        }
    }

    /**
     * 不同一周的显示时间格式
     *
     * @param time
     * @param timeFormat
     * @return
     */
    public static String getTime(long time, String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    /**
     * 不同年的显示时间格式
     *
     * @param time
     * @param yearTimeFormat
     * @return
     */
    public static String getYearTime(long time, String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }

    public static boolean isCloseEnough(long var0, long var2) {
        long var4 = var0 - var2;
        if (var4 < 0L) {
            var4 = -var4;
        }

        return var4 < 5 * 60000L;
    }

    /**
     * @param createTime
     * @return 获取创建天数
     */
    public static String getCreateDays(long createTime) {
        long time = System.currentTimeMillis();
        time = time - createTime;
        float days = time * 1.0f / 1000 / 60 / 60 / 24;
        int day = Math.round(days);
        return String.valueOf(day);
    }

    public static String getDuration(Context context, long rel_time) {
        //获取当前时间
        Date date = new Date();
        long now_time = date.getTime();
        if (now_time - rel_time < 0) {
            return "刚刚";
        }
        String backStr = "";
        try {
            // 毫秒ms
            long diff = now_time - rel_time;

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays != 0) {
                if (diffDays < 30) {
                    if (1 < diffDays && diffDays < 2) {
                        backStr = context.getString(R.string.yesterday);
                    } else if (1 < diffDays && diffDays < 2) {
                        backStr = context.getString(R.string.The_day_before_yesterday);
                    } else {
                        backStr = String.valueOf(diffDays) + context.getString(R.string.Days_ago);
                    }
                } else {
                    backStr = getFormat(rel_time, "M月d日 HH:mm");
                }

            } else if (diffHours != 0) {
                backStr = String.valueOf(diffHours) + context.getString(R.string.An_hour_ago);

            } else if (diffMinutes != 0) {
                backStr = String.valueOf(diffMinutes) + context.getString(R.string.minutes_ago);

            } else {
                backStr = context.getString(R.string.just);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return backStr;
    }

    public static String getDayFormat(Context context, long rel_time) {
        //获取当前时间
        Date date = new Date();
        long now_time = date.getTime();
        if (now_time - rel_time < 0) {
            return context.getString(R.string.just);
        }
        String backStr = "";
        try {
            // 毫秒ms
            long diff = now_time - rel_time;

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays != 0) {
                if (diffDays < 1) {
                    backStr = getFormat(rel_time, "M月d日 HH:mm");
                } else {
                    backStr = getFormat(rel_time, "M月d日 HH:mm");
                }
            } else if (diffHours != 0) {
                backStr = String.valueOf(diffHours) + context.getString(R.string.An_hour_ago);

            } else if (diffMinutes != 0) {
                backStr = String.valueOf(diffMinutes) + context.getString(R.string.minutes_ago);

            } else {
                backStr = context.getString(R.string.just);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return backStr;
    }

    public static String getDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
        String format1 = sdf.format(new Date(time));
        String[] split = format1.split("-");
        String day = split[2];
        return day;
    }

    public static String getMoth(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String format1 = sdf.format(new Date(time));
        String[] split = format1.split("-");
        String moth = split[1];
        return moth;
    }

    public static String getFormat(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String format1 = sdf.format(new Date(time));
        return format1;
    }
}
