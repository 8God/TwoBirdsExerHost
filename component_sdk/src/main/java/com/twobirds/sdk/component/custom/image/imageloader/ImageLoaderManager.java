/**
 *
 * Copyright (c) 2015 Chutong Technologies All rights reserved.
 *
 */

/**
 * Version Control
 *
 * | version | date        | author         | description
 *   0.0.1     2015.11.30    TwoBirds     初始化
 *
 */

package com.twobirds.sdk.component.custom.image.imageloader;

import android.app.ActivityManager;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

import com.twobirds.sdk.common.util.FileUtil;
import com.twobirds.sdk.component.R;

/**
 * ImageLoader初始化
 *
 * @author TwoBirds
 * @version 0.0.1
 */
public class ImageLoaderManager {

    public ImageLoaderManager() {

    }

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, ImageLoaderConfiguration config) {
        if (null == context || isInit()) {
            return;
        }

        if (null == config) {
            config = getPreferConfiguration(context);
            if (null == config) {
                config = ImageLoaderConfiguration.createDefault(context);
            }
        }

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config); 
    }

    private ImageLoaderConfiguration getPreferConfiguration(Context context) {
        File cacheDir = FileUtil.getCacheDir(context, "imageloader");
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.ic_launcher).cacheOnDisc().cacheInMemory().imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

        final int memClass = ((ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 4;
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCache(new LruMemoryCache(cacheSize))
                // .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPriority(Thread.MIN_PRIORITY).memoryCacheSize(cacheSize).discCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 40MB
                .imageDownloader(new ExtendedImageDownloader(context, 5 * 1000, 20 * 1000))
                        // connectTimeout (5 s), readTimeout (20 s)
                .threadPoolSize(5).denyCacheImageMultipleSizesInMemory().defaultDisplayImageOptions(defaultOptions).tasksProcessingOrder(QueueProcessingType.LIFO)/*
                                                                                                                                                           * .
                                                                                                                                                           * )
                                                                                                                                                           */.build();

        return config;
    }

    public boolean isInit() {
        return ImageLoader.getInstance().isInited();
    }
}
