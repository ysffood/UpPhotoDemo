package com.medemo.newland.upphotodemo.View.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.medemo.newland.upphotodemo.R;
import com.medemo.newland.upphotodemo.utils.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * [加载图片]<BR>
 * [功能详细描述]
 *
 * @author zhangrui
 * @version [CarGang, 2013-3-28]
 */
public class AsyncBitmapLoader {

    private MemoryCache memoryCache = new MemoryCache();

    private static final String TAG = "AsyncBitmapLoader";

    //图片质量
    private static final int PHOTO_QUALITY = 100;

    // In memory cache.,软引用给注释掉了
//    private Map<String, SoftReference<Bitmap>> mCache;

    // MD5 hasher.
    private MessageDigest mDigest;

    //该类的对象
    private static AsyncBitmapLoader sAsyncBitmapLoader;

    /**
     * 存放图片目录
     */
    private String localPatch;

    /**
     * 线程池的个数
     */
    private static final int THREAD_NUMBER = 5;

    /**
     * 使用线程池加载
     */
    private static ExecutorService sExcutorService = Executors.newFixedThreadPool(THREAD_NUMBER);

    /**
     * 默认构造
     */
    private AsyncBitmapLoader() {
//        mCache = new HashMap<String, SoftReference<Bitmap>>();

        try {
            mDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5 algorithm.");
        }
    }

    public static AsyncBitmapLoader getInstance() {
        if (null == sAsyncBitmapLoader) {
            synchronized (AsyncBitmapLoader.class) {
                sAsyncBitmapLoader = new AsyncBitmapLoader();
            }
        }
        return sAsyncBitmapLoader;
    }

    /**
     * 刷新界面接口<BR>
     *
     * @author chendongsheng
     * @version [RCS Client V100R001C03, 2012-7-10]
     */
    public interface ImageLoaderCallback {
        /**
         * 刷新imageview<BR>
         *
         * @param iv     需要刷新imageview
         * @param bitmap 图片bitmap
         * @param tag    　用于界面展示的tag
         */
        void refresh(ImageView iv, Bitmap bitmap, String tag);
    }

    /**
     * 加载图片<BR>
     *
     * @param faceurl             图片URL
     * @param iv                  需要展示的view
     * @param imageLoaderCallback 回调的接口
     * @return 返回图片bitmap
     */
    public Bitmap loadImg(final String faceurl, final ImageView iv,
                          final ImageLoaderCallback imageLoaderCallback, final String tag, boolean isLoadOnlyFromCache) {
//        SoftReference<Bitmap> ref;
        Bitmap bitmap;

        synchronized (this) {
            bitmap = memoryCache.get(faceurl);

            if (bitmap != null) {
                imageLoaderCallback.refresh(iv,
                        bitmap,
                        tag);
                return bitmap;
            }
        }

        if (!isLoadOnlyFromCache) {
            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    Bitmap bm = (Bitmap) msg.obj;
                    if (imageLoaderCallback != null) {
                        imageLoaderCallback.refresh(iv,
                                bm,
                                tag);
                    }
                }

            };

            sExcutorService.submit(new Runnable() {
                @Override
                public void run() {
                    Bitmap bms = getBitmap(faceurl);
                    memoryCache.put(faceurl,
                            bms);
                    Message msg = handler.obtainMessage(0,
                            bms);
                    handler.sendMessage(msg);
//                mCache.put(faceurl, new SoftReference<Bitmap>(bms));
                }
            });
        }
        return null;
    }

    private String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();

        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }

        return builder.toString();
    }

    // MD5 hases are used to generate filenames based off a URL.
//    private String getMd5(String url)
//    {
//        mDigest.update(url.getBytes());
//        
//        return getHashString(mDigest);
//    }

    /**
     * 查询sd卡内图片<BR>
     *
     * @param faceurl 头像url
     * @return 头像bitmap
     */
    private Bitmap lookupFile(String faceurl) {
//        String hashedUrl = getMd5(faceurl);
//        FileInputStream fis = null;
        Bitmap bm = null;

//        try {
            if (faceurl.lastIndexOf("/") < 0) {
                return null;
            }
            String fileName = faceurl.substring(faceurl.lastIndexOf("/"));
            File file = new File(localPatch, fileName);
            if (file.isFile() && file.exists()) {
//                fis = new FileInputStream(file);
//                bm= BitmapUtil.getBitmapFromFile(localPatch+fileName,120,120);
                bm= BitmapFactory.decodeFile(localPatch+fileName);
            }
//            bm = BitmapFactory.decodeStream(fis);
//        } catch (FileNotFoundException e) {
//            Log.e(TAG, "file no exists");
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    Log.e(TAG, "Could not close file.");
//                    e.printStackTrace();
//                }
//            }
//        }
        return bm;
    }

    /**
     * 将图片保存到文件中 <BR>
     *
     * @param faceurl 图片url路径
     * @param bitmap  图片的bitmap
     * @param quality 图片质量
     */
    private void writeFile(String faceurl, Bitmap bitmap, int quality) {
        if (bitmap == null) {
            Log.w(TAG,
                    "Can't write file. Bitmap is null.");
            return;
        }
//        if (StringUtil.isNullOrEmpty(getSDPath()))
//        {
//            Log.w(TAG,
//                "SDcard no exists!");
//            return;
//        }

//        ByteArrayOutputStream baos = null;
        try {
//            String hashedUrl = getMd5(faceurl);
//            File dir = new File(localPatch);
//            if (!dir.exists())
//            {
//                dir.mkdirs();
//            }
            String fileName = faceurl.substring(faceurl.lastIndexOf("/"));
            File cachePicDir=new File(localPatch);
            if (!cachePicDir.exists()) {
                cachePicDir.mkdirs();//所以这一句不能丢
            }
            File bitmapFile = new File(localPatch, fileName);
            if (!bitmapFile.exists()) {

                bitmapFile.createNewFile();//必须有目录才能createfile

            }
            FileOutputStream fos = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();

//            baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG,
//                    quality,
//                    baos);
//            byte[] byrr = baos.toByteArray();
//            FileOutputStream fos = new FileOutputStream(bitmapFile);
//            BufferedOutputStream bf = new BufferedOutputStream(fos);
//            bf.write(byrr);
//            baos.close();
//            bf.close();
//            fos.flush();
//            fos.close();

            Log.d(TAG,
                    "Writing file: " + faceurl);

        } catch (IOException ioe) {
            Log.e(TAG,
                    ioe.getMessage());
        }
//        finally
//        {
//            try
//            {
//                if (baos != null)
//                {
//                    baos.flush();
//                    baos.close();
//                }
//            }
//            catch (IOException e)
//            {
//                Log.e(TAG,
//                    "Could not close file.");
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 加载图片，先从SD卡获取<BR>
     *
     * @param faceurl 图片url
     * @return 返回图片的bitmp
     */
    public Bitmap getBitmap(String faceurl) {

        URL url;
        Bitmap bitmap = null;
        //查询sd卡
        bitmap = lookupFile(faceurl);
        if (bitmap != null) {
            synchronized (this) {
                memoryCache.put(faceurl, bitmap);
//                mCache.put(faceurl, new SoftReference<Bitmap>(bitmap));
            }
            return bitmap;
        }

        try {
            url = new URL(faceurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());

            //写入文件
            writeFile(faceurl, bitmap, PHOTO_QUALITY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 存放头像的目录<BR>
     *
     * @return 头像前面URL
     */
    public String getSDPath(Context context) {
//        String sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState()
//                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
//        if (sdCardExist)
//        {
//            sdDir =photoPath;//获取跟目录
//        }
//        return sdDir;

        File dirFile = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile.getAbsolutePath();

    }

    public void drawPicture(String uri, final ImageView sitePhotoImageView, String localPatch, boolean isLoadOnlyFromCache) {
        sitePhotoImageView.setTag(uri);
        this.localPatch = localPatch;
        loadImg(uri, sitePhotoImageView,
                new ImageLoaderCallback() {

                    @Override
                    public void refresh(ImageView iv, Bitmap bitmap, String tag) {
                        if (!StringUtil.isNullOrEmpty(tag)
                                && tag.equals(iv.getTag())) {
                            if (bitmap != null) {
                                sitePhotoImageView.setImageBitmap(bitmap);
                            } else {
                                sitePhotoImageView.setImageResource(R.drawable.icon_head);
                            }
                        }
                    }
                }, uri, isLoadOnlyFromCache);

    }

}
