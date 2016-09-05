# UpPhotoDemo
图片选择器，自定义头像控件，压缩，功能
compile 'com.squareup.picasso:picasso:2.5.2'

# 首次进入页面的时候加载头像,传入头像的后台地址url

img_up.draw("your photo url", dirFile.getAbsolutePath(), false);

# 选择头像

int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
boolean showCamera = true;
int maxNum = 1;
Intent intentPic = new Intent(MainActivity.this,MultiImageSelectorActivity.class);
intentPic.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
intentPic.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
intentPic.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
startActivityForResult(intentPic, 1);

# 压缩图片方法

threadToCompress(bitmap, saveFile);
