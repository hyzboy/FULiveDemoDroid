package com.faceunity.fulivedemo.videoplayer;

import android.content.Context;
import android.widget.VideoView;

public class FullScreenVideoView extends VideoView
{
    /**
     * construction method
     *
     * @param context {@link Context} instance object
     */
    public FullScreenVideoView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
