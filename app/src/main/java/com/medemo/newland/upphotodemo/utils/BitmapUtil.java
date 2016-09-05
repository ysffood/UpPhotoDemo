package com.medemo.newland.upphotodemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rigor on 2016/8/18.
 */

public class BitmapUtil {
    /**
     * 缩放图片像素点，用户显示
     * @param path
     * @return
     */
    public static Bitmap getBitmapFromFile(String path) {
        int width=1024;
        int height=1024;
        BitmapFactory.Options opts = null;
        if (width > 0 && height > 0) {
            opts = new BitmapFactory.Options();          //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            // 计算图片缩放比例
            final int minSideLength = Math.min(width, height);
            opts.inSampleSize = computeSampleSize(opts, minSideLength,
                    width * height);           //这里一定要将其设置回false，因为之前我们将其设置成了true
            opts.inJustDecodeBounds = false;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
        }
        try {
            //图片角度校正
            int degrees=readPictureDegree(path);
            Bitmap bitmap= BitmapFactory.decodeFile(path, opts);
            return rotateBitmap(bitmap,degrees);
//            return BitmapFactory.decodeFile(patch, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }
    private static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 压缩图片文件，用于上传
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
//        Bitmap bitmap=BitmapFactory.decodeFile(bmpPatch);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;//100表示不压缩
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 200) {
            options -= 10;
            if (options <= 0) {
                break;
            }
            baos.reset();
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static Bitmap showlargeSizePic(String bitmapPatch){
//
//        BitmapFactory.Options bfOptions=new BitmapFactory.Options();
//        bfOptions.inDither=false;
//        bfOptions.inPurgeable=true;
//        bfOptions.inTempStorage=new byte[120 * 1024];
//        bfOptions.inJustDecodeBounds = true;
//        File file = new File(bitmapPatch);
//        FileInputStream fs=null;
//        Bitmap bmp = null;
//        try {
//            fs = new FileInputStream(file);
//            if(fs != null)
//                bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bmp;
//    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     * @param bitmap 原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }
}
