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

package com.twobirds.sdk.common.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 重定向Fragment接口
 *
 * @author TwoBirds
 * @version 0.0.1
 * @since
 *
 */
public interface IFragmentRedirecter {

	/**
	 * 重定向Fragment
	 *
	 * @param fragment 目标fragment
	 * @param isBackStackSupported 是否支持回退
	 */
	public void redirect(final Fragment fragment, final boolean isBackStackSupported);

	/**
	 * 回退Fragment
	 *
	 * @param backFragmentArgs 回退Fragment集合
	 */
	public void popBack(Bundle backFragmentArgs);
}
