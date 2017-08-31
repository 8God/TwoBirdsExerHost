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

/**
 * 文本格式工具类
 *
 * @author TwoBirds
 * @version 0.0.1
 */
public class TextFormatUtil {
	
	private static String DOUBLE_BLANK = "\u3000\u3000";
	private static String PARAGRAPH_SYMBOL = "\n\n";
	
	public static String format(String contentStr) {
		StringBuffer buf = new StringBuffer();
		String paragraphs[] = contentStr.split(PARAGRAPH_SYMBOL);
		for(int i = 0;i < paragraphs.length;i++) {
			buf.append(DOUBLE_BLANK + paragraphs[i] + PARAGRAPH_SYMBOL);
		}
		
		return buf.toString();
	}

}
