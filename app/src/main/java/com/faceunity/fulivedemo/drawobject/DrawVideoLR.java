package com.faceunity.fulivedemo.drawobject;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.faceunity.fulivedemo.gl.ShaderModule;
import com.faceunity.fulivedemo.videoplayer.VideoPlayer;

public class DrawVideoLR extends DrawObject
{
    private VideoPlayer player=null;
    private GL2VideoTexture video_texture=null;

    public DrawVideoLR(Context con)
    {
        super(ObjectType.VideoAlphaLR,new ShaderAlphaLRExternal());

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
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
        shader.begin();
            video_texture.bind(0);
            shader.BindPosition(render_layout);
            shader.BindTexCoord(texture_uv);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();
    }
}
