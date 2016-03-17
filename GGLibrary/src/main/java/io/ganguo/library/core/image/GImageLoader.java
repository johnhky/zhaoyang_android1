//package io.ganguo.library.core.image;
//
//import android.app.ActivityManager;
//import android.app.Application;
//import android.content.Context;
//import android.view.View;
//
//import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
//import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.imageaware.ImageAware;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
//import com.nostra13.universalimageloader.utils.L;
//
//import io.ganguo.library.Config;
//import io.ganguo.library.R;
//import io.ganguo.library.util.Strings;
//
///**
// * Universal-Image-Loader
// * <p/>
// * https://github.com/nostra13/Android-Universal-Image-Loader
// * <p/>
// * Created by Tony on 11/24/14.
// */
//public class GImageLoader extends ImageLoader {
//
//    private volatile static GImageLoader instance;
//
//    /**
//     * Returns singleton class instance
//     */
//    public static GImageLoader getInstance() {
//        if (instance == null) {
//            synchronized (ImageLoader.class) {
//                if (instance == null) {
//                    instance = new GImageLoader();
//                }
//            }
//        }
//        return instance;
//    }
//
//    protected GImageLoader() {
//
//    }
//
//    /**
//     * init configuration
//     *
//     * @param application
//     */
//    public void init(Application application) {
//        init(application, 0);
//    }
//
//    /**
//     * init configuration
//     *
//     * @param application
//     * @param placeHolder
//     */
//    public void init(Application application, int placeHolder) {
//        int memClass = ((ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
//        int cacheSize = 1024 * 1024 * memClass / 8;  // 1/8 of system ram
//
//        DisplayImageOptions defaultOptions = new DisplayImageOptions
//                .Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .displayer(new FadeInBitmapDisplayer(600))
//                .showImageOnLoading(placeHolder)
//                .build();
//
//        // File cacheDir = StorageUtils.getCacheDirectory(context);
//        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
//                .Builder(application.getApplicationContext())
//                .threadPoolSize(2)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(cacheSize))
//                .memoryCacheSize(cacheSize)
//                .diskCache(new UnlimitedDiskCache(Config.getImageCachePath())) // default
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileCount(100)
//                .defaultDisplayImageOptions(defaultOptions)
//                .build();
//
//        super.init(configuration);
//
//        L.writeLogs(false);
//    }
//
//    /**
//     * 解决ImageLoader重复显示同一个图片问题
//     *
//     * @param uri
//     * @param imageAware
//     * @param options
//     * @param listener
//     * @param progressListener
//     */
//    @Override
//    public void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
//                             ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
//
//        if (Strings.isEmpty(uri)) {
//            return;
//        }
//        if (imageAware == null || imageAware.getWrappedView() == null) {
//            super.displayImage(uri, imageAware, options, listener, progressListener);
//            return;
//        }
//
//        View view = imageAware.getWrappedView();
//        if (view != null && !Strings.isEquals(uri, view.getTag(R.string.tag_image_loader_uri) + "")) {
//
//            super.displayImage(uri, imageAware, options, listener, progressListener);
//
//            view.setTag(R.string.tag_image_loader_uri, uri);
//        }
//    }
//
//}
