package com.faceunity.fulivedemo.drawobject;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.util.Log;

import com.faceunity.fulivedemo.videoplayer.VideoPlayer;

/**
 * 绘制影片
 */
public class DrawVideo extends DrawObject
{
    private VideoPlayer player=null;
    private GL2VideoTexture video_texture=null;
    private ShaderOpaqueExternal shader=new ShaderOpaqueExternal();

    public DrawVideo(Context con, SurfaceTexture.OnFrameAvailableListener listener)
    {
        super(ObjectType.Video);

        player=new VideoPlayer(con);
        video_texture = new GL2VideoTexture(player);
        video_texture.create(listener);
    }

    public void init(String name)
    {
        player.setDataSource(name);
    }

    @Override
    public void start()
    {
        if(player!=null)
            player.startPlay();
    }

    private String TAG=getClass().getSimpleName();
    @Override
    public void update()
    {
        Log.e(TAG, "update: " );
        if(video_texture!=null)
            video_texture.update();
    }

    @Override
    public void draw()
    {
        Log.e(TAG, "draw: " );
        GLES20.glDisable(GLES20.GL_BLEND);
        shader.begin();
            video_texture.bind(0);
            render_layout.bind(shader.maPositionHandle);
            texture_uv.bind(shader.maTexCoordHandle);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();
    }
}
