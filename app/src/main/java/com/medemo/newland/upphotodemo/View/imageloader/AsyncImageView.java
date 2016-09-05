package com.medemo.newland.upphotodemo.View.imageloader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * [异步加载图片控件]<BR>
 * [功能详细描述]
 * @author zhangrui
 * @version [IPark, 2013-6-15] 
 */
public class AsyncImageView extends ImageView
{

    public AsyncImageView(Context context)
    {
        super(context);
    }
    
    public AsyncImageView(Context context, AttributeSet attrs) {
     
    super( context, attrs );
    }
      
    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
     
    super( context, attrs, defStyle );
    }
    
    /**
     * [设置控件图片]<BR>
     * [功能详细描述]
     * @param uri
     * @param localPatch
     * @param isLoadOnlyFromCache
     * @author zhangrui
     */
    public void draw(String uri, String localPatch, boolean isLoadOnlyFromCache){
        AsyncBitmapLoader.getInstance().drawPicture(uri, this, localPatch, isLoadOnlyFromCache);
    }

}
