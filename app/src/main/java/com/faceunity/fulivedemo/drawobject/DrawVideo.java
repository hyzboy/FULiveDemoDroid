package com.faceunity.fulivedemo.drawobject;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.util.Log;

import com.faceunity.fulivedemo.gl.ShaderModule;
import com.faceunity.fulivedemo.videoplayer.VideoPlayer;

/**
 * 绘制影片
 */
public class DrawVideo extends DrawObject
{
    private VideoPlayer player=null;
    private GL2VideoTexture video_texture=null;

    public DrawVideo(Context con)
    {
        super(ObjectType.Video,new ShaderOpaqueExternal());

        player=new VideoPlayer(con);
        video_texture = new GL2VideoTexture(player);
        video_texture.create();
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
        video_texture.update();
    }

    @Override
    public void draw()
    {
        Log.e(TAG, "draw: " );
        GLES20.glDisable(GLES20.GL_BLEND);
        shader.begin();
            video_texture.bind(0);
            shader.SetDirection(direction);
            shader.SetMirror(mirror);
            shader.SetFlip(flip);
            shader.BindPosition(render_layout);
            shader.BindTexCoord(texture_uv);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();
    }
}
