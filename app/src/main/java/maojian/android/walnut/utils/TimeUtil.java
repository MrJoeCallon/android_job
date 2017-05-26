package maojian.android.walnut.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joe on 2017/4/15.
 */
public class TimeUtil {
    public static String getTimeLong(String simpleDate)  {
        String dateString;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        try {
            begin = df.parse(simpleDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return simpleDate;
        }
        Date end = new Date();
        long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒

        int day = (int) (between / (24 * 3600));
        if (day >= 30) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            dateString = formatter.format(begin.getTime());
            return dateString;
        }
        if (day > 1) {
            return day + " day(s)";
        }

        int hour = (int) (between % (24 * 3600) / 3600);
        if (hour > 24) {
            return "Yesterday";
        }
        if (hour > 1) {
            return hour + " hours";
        }

        if (hour == 1) {
            return hour + " hour";
        }

        int minute = (int) (between % 3600 / 60);

        if (minute > 1) {
            return minute + " mins";
        }

        if (minute == 1) {
            return minute + " minute";
        }

        int second = (int) (between % 60);
        return second + " seconds";
    }
}
