package com.medemo.newland.upphotodemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.medemo.newland.upphotodemo.View.imageloader.CircleImageView;
import com.medemo.newland.upphotodemo.multi_image_selector.MultiImageSelectorActivity;
import com.medemo.newland.upphotodemo.utils.BitmapUtil;
import com.medemo.newland.upphotodemo.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.bt_up_file) Button file_select;
    @Bind(R.id.bt_submit) Button file_submit;
    @Bind(R.id.img_up) CircleImageView img_up;

    // 待上传文件地址
    private String headFileName;
    private static String cachePicPatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initListener();

        File dirFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        // 首次进入页面的时候加载头像,传入头像的后台地址url
        img_up.draw("your photo url", dirFile.getAbsolutePath(), false);

    }

    private void initListener(){
        file_select.setOnClickListener(this);
        file_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == file_select){
            // 选择头像
            int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
            boolean showCamera = true;
            int maxNum = 1;
            Intent intentPic = new Intent(MainActivity.this,
                    MultiImageSelectorActivity.class);
            intentPic.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
                    showCamera);
            intentPic.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
                    maxNum);
            intentPic.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                    selectedMode);
            startActivityForResult(intentPic, 1);
        }else if (view == file_submit){
            // 将文件转成String
            String fileParms = file2Base64(headFileName);
            if (TextUtils.isEmpty(fileParms)){
                Toast.makeText(MainActivity.this,"上传失败,重新上传",Toast.LENGTH_SHORT).show();
                return;
            }
            // 调用后台接口,提交到服务器
            Toast.makeText(MainActivity.this,fileParms,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && null != data) {
            ArrayList<String> resultList = data.getStringArrayListExtra
                    (MultiImageSelectorActivity.EXTRA_RESULT);
            if (resultList.size() > 0) {
                String picturePath = resultList.get(0);
                headFileName = picturePath.substring(picturePath.lastIndexOf("/"));
//                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                Bitmap bitmap= BitmapUtil.getBitmapFromFile(picturePath);
//                Bitmap bitmap=BitmapUtil.showlargeSizePic(picturePath);

                //显示缩放尺寸图片
                img_up.setImageBitmap(bitmap);

                File dirFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                File saveFile = new File(dirFile, headFileName);
                if (!saveFile.exists()) {
                    file_submit.setEnabled(false);
                    file_submit.setText("正在压缩");
                    file_submit.setTextColor(Color.GRAY);
                    threadToCompress(bitmap, saveFile);
                }
//                else{
//                    // 获取图片并显示
//                    Bitmap itmap = BitmapFactory.decodeFile(saveFile.getPath());
//                    headImageView.setImageBitmap(itmap);
//                }
            }
        }
    }

    /**
     * 开启压缩线程,压缩完后会合并到UI线程
     * @param bitmap 压缩图片
     * @param saveFile 保存的文件
     */
    private void threadToCompress(final Bitmap bitmap, final File saveFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BitmapUtil.compressBmpToFile(bitmap, saveFile);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("MainActivity",headFileName);
                        Toast.makeText(MainActivity.this,headFileName,Toast.LENGTH_LONG).show();
//                        headImageView.setImageBitmap(compressBitmap);
                        file_submit.setTextColor(Color.RED);
                        file_submit.setText("提交");
                        file_submit.setEnabled(true);
                    }

                });
            }
        }).start();
    }

    /**
     * 将文件转成string类型
     * @param fileName 文件名
     * @return 文件String字节数组
     */
    private String file2Base64(String fileName){
        File file = new File(getCachePicPatch(), fileName);
        String file_byte = "";
        try {
            file_byte = StringUtil.encodeBase64File(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(file_byte)) {
            Toast.makeText(MainActivity.this, "上传失败，请重新选择照片", Toast.LENGTH_SHORT);
            return null;
        }
        return file_byte;
    }

    public String getCachePicPatch() {
        if (StringUtil.isNullOrEmpty(cachePicPatch)) {
            File cachePicDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!cachePicDir.exists()) {
                cachePicDir.mkdirs();
            }
            cachePicPatch = cachePicDir.getAbsolutePath();
        }
        return cachePicPatch;
    }
}
