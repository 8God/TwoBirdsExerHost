/**
 *
 * Copyright (c) 2015 Chutong Technologies All rights reserved.
 *
 */

/**
 * Version Control
 *
 * | version | date        | author         | description
 *   0.0.1     2015.11.30    TwoBirds     整理代码
 *
 */

package com.twobirds.sdk.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 年龄工具类
 *
 * @author TwoBirds
 * @version 0.0.1
 */

public class AgeUtil {

    /**
     * 获得年龄
     *
     * @param birthOfDate 生日日期
     * @return 年龄
     * @since 0.0.1
     */
    public static int getAge(final Date birthOfDate) {
        int age = -1;
        Calendar calendar = Calendar.getInstance();
        
        if (!calendar.before(birthOfDate)) {
            int yearNow = calendar.get(Calendar.YEAR);
            int monthNow = calendar.get(Calendar.MONTH)+1;
            int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
            
            calendar.setTime(birthOfDate);
            int yearBirth = calendar.get(Calendar.YEAR);
            int monthBirth = calendar.get(Calendar.MONTH);
            int dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH);
            
            age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        --age;
                    } // if (dayOfMonthNow < dayOfMonthBirth)
                } else {
                    --age;
                }
            } // if (monthNow <= monthBirth)
        } // if (!calendar.before(birthOfDate))
        
        return age;
    } // getAge(final Date birthOfDate)

    /**
     * 获得年龄字符串
     *
     * @param birthOfDate 生日日期
     * @return 年龄字符串
     * @since 0.0.1
     */
    public static String getAgeStr(final Date birthOfDate) {
        StringBuilder ageSB = new StringBuilder();
        final int age = getAge(birthOfDate);
        if (-1 != age) {
            ageSB.append(age);
            ageSB.append("岁");
        }
        
        return ageSB.toString();
    }
        
}
