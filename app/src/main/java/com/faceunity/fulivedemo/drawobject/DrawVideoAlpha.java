package com.faceunity.fulivedemo.drawobject;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.util.Log;

import com.faceunity.fulivedemo.gl.ShaderModule;
import com.faceunity.fulivedemo.videoplayer.VideoPlayer;

public class DrawVideoAlpha extends DrawObject
{
    private VideoPlayer rgb_player =null;
    private GL2VideoTexture rgb_texture =null;
    private VideoPlayer alpha_player =null;
    private GL2VideoTexture alpha_texture =null;
    private ShaderModule shader=new ShaderAlphaExternal();

    public DrawVideoAlpha(Context con, SurfaceTexture.OnFrameAvailableListener listener)
    {
        super(ObjectType.Video);

        rgb_player =new VideoPlayer(con);
        rgb_texture = new GL2VideoTexture(rgb_player);
        rgb_texture.create(listener);

        alpha_player =new VideoPlayer(con);
        alpha_texture = new GL2VideoTexture(alpha_player);
        alpha_texture.create(listener);
    }

    public void init(String rgb_name,String a_name)
    {
        rgb_player.setDataSource(rgb_name);
        alpha_player.setDataSource(a_name);
    }

    @Override
    public void start()
    {
        if(rgb_player !=null)
            rgb_player.startPlay();

        if(alpha_player!=null)
            alpha_player.startPlay();
    }

    private String TAG=getClass().getSimpleName();

    @Override
    public void update()
    {
        Log.e(TAG, "update: " );

        if(rgb_texture !=null)
            rgb_texture.update();

        if(alpha_texture!=null)
            alpha_texture.update();
    }

    @Override
    public void draw()
    {
        //Log.e(TAG, "draw: " );
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
        shader.begin();
            rgb_texture.bind(0);
            alpha_texture.bind(1);
            render_layout.bind(shader.maPositionHandle);
            texture_uv.bind(shader.maTexCoordHandle);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();
    }
}
