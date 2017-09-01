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

package com.twobirds.sdk.component.custom.image.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * ImageLoader图片加载完成监听器
 *
 * @author TwoBirds
 * @version 0.0.1
 */
public interface IImageOnLoadingCompleteListener {
    public void onLoadingComplete(ImageView imageView, Bitmap bitmap);
}
