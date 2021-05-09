package com.faceunity.fulivedemo.drawobject;

import android.media.MediaPlayer;
import android.opengl.GLES20;

/**
 * 绘制影片
 */
public class DrawVideo extends DrawObject
{
    private MediaPlayer player;
    private GL2VideoTexture video_texture=null;
    private ShaderOpaque shader=new ShaderOpaque();

    public DrawVideo(MediaPlayer mp)
    {
        super(ObjectType.Video);

        player=mp;
        video_texture=new GL2VideoTexture();
        video_texture.init(player);
    }

    @Override
    public void update()
    {}

    @Override
    public void draw()
    {
        GLES20.glDisable(GLES20.GL_BLEND);
        shader.begin();
            video_texture.bind(0);
            render_layout.bind(shader.maPositionHandle);
            texture_uv.bind(shader.maTexCoordHandle);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();
    }

    public void play()
    {
        player.start();
    }

    public void stop()
    {
        player.stop();
    }

    public void restart()
    {
        player.reset();
    }
}
