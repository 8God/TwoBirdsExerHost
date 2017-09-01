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

package com.twobirds.sdk.component.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * TextView工具类
 *
 * @author TwoBirds
 * @version 0.0.1
 */
public class TextViewUtil {

    private static <T extends View> T findView(View contentView, int viewId) {
        return (T) contentView.findViewById(viewId);
    }

    public static void setTextView(View contentView, int viewId, String text) {
        setTextView(contentView, viewId, text, false);
    }

    public static void setTextView(View contentView, int viewId, String text, boolean isEmptyGone) {
        TextView tv = findView(contentView, viewId);
        if (null != tv) {
            if (!TextUtils.isEmpty(text)) {
                tv.setText(text);
            } else {
                if (isEmptyGone) {
                    tv.setVisibility(View.GONE);
                } else {
                    tv.setText(null);
                }
            }
        }
    }
}
