package com.twobirds.sdk.common.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateParser {

    public static final String parseStringTxString(String sourceStr, SimpleDateFormat sourceReg, SimpleDateFormat targetReg) {
        String targetStr = null;

        if (!TextUtils.isEmpty(sourceStr)) {
            if (null != sourceReg && null != targetReg) {
                try {
                    Date targetDate = sourceReg.parse(sourceStr);
                    targetStr = targetReg.format(targetDate);
                } catch (ParseException e) {
                    targetStr = null;
                    return targetStr;
                }
            }
        }
        return targetStr;
    }
}
