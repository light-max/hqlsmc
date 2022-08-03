package com.hql.scm.util.datetranslate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    public static String format(long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }
}
