package io.ganguo.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片处理工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class Images {

    /**
     * 保存为PNG
     *
     * @param bitmap
     * @param to
     * @throws IOException
     */
    public static void savePNG(Bitmap bitmap, int quality, File to) throws IOException {
        OutputStream out = new FileOutputStream(to);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
        } finally {
            out.close();
        }
    }

    /**
     * 保存为JPEG
     *
     * @param bitmap
     * @param to
     * @throws IOException
     */
    public static void saveJPEG(Bitmap bitmap, int quality, File to) throws IOException {
        OutputStream out = new FileOutputStream(to);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } finally {
            out.close();
        }
    }

    /**
     * 压缩bitmap到指定大小
     * TODO 添加支持Exif
     *
     * @param from
     * @param to
     * @param limitKB (kb)
     * @throws IOException
     */
    public static void compress(File from, File to, int limitKB) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(from.getAbsolutePath());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // scale
            int quality = 100;
            int interval = 10;
            // Store the bitmap into output stream(no compress)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            // Compress by loop
            while (os.toByteArray().length / 1024 > limitKB && quality > interval) {
                // Clean up os
                os.reset();
                // interval 10
                quality -= interval;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            }
            // Generate compressed image file
            FileOutputStream fos = new FileOutputStream(to);
            try {
                fos.write(os.toByteArray());
                fos.flush();
            } finally {
                fos.close();
            }
        } finally {
            os.close();

            recycle(bitmap);
        }
    }


    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取图片中的缩略图 JPEG Exif
     *
     * @param path
     * @return
     */
    public static Bitmap getThumbnail(String path) throws IOException {
        ExifInterface exif = new ExifInterface(path);
        byte[] bytes = exif.getThumbnail();
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * View to Bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap toBitmap(View view) {
        if (view == null) {
            return null;
        }
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        // 如果已经带有宽度和高度，那直接拿出来使用，就不再进行计算，加快很多
        int width = -1;
        int height = -1;
        if (view.getLayoutParams() != null) {
            width = view.getLayoutParams().width;
            height = view.getLayoutParams().height;
        }
        if (width > 0 && height > 0) {
            // 已有宽高
            view.measure(width, height);
        } else {
            // 计算宽高
            view.measure(View.MeasureSpec.makeMeasureSpec(
                            0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
        }
        view.layout(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap toBitmap(Drawable drawable) {
        // is BitmapDrawable
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // draw to canvas
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 旋转bitmap
     *
     * @param bitmap
     * @param degrees
     * @return bitmap
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        // bitmap new
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true
        );
        return resizedBitmap;
    }

    /**
     * 从asset中读取bitmap
     *
     * @param context
     * @param name
     * @return bitmap
     * @throws IOException
     */
    public static Bitmap getAssetBitmap(Context context, String name) throws IOException {
        InputStream in = context.getAssets().open(name);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(in);
        } finally {
            in.close();
        }
        return bitmap;
    }

    /**
     * recycle bitmap
     *
     * @param bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * recycle drawable
     * -> recycle(Bitmap bitmap)
     *
     * @param drawable
     */
    public static void recycle(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof BitmapDrawable) {
            recycle(((BitmapDrawable) drawable).getBitmap());
        }
    }

    /**
     * recycle imageView
     * -> recycle(Bitmap bitmap)
     *
     * @param imageView
     */
    public static void recycle(ImageView imageView) {
        if (imageView == null) {
            return;
        }
        Drawable drawable = imageView.getDrawable();
        imageView.setImageBitmap(null);
        imageView.setImageDrawable(null);
        recycle(drawable);
    }

    /**
     * 毛玻璃效果
     *
     * @param sentBitmap
     * @param radius
     * @return
     */
    public static Bitmap fastblur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
