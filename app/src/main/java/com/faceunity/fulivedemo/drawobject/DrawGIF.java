package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.GL2Texture;

import java.io.IOException;

import pl.droidsonroids.gif.GifOptions;
import pl.droidsonroids.gif.GifTexImage2D;
import pl.droidsonroids.gif.InputSource;

public class DrawGIF extends DrawObject
{
    private GL2Texture tex=new GL2Texture();
    private GifTexImage2D gif_tex=null;
    private int last_frame=-1;

    private ShaderAlpha shader=new ShaderAlpha();

    public DrawGIF()
    {
        super(ObjectType.GIF);
    }

    public boolean Load(String filename) throws IOException
    {
        InputSource.FileSource file=new InputSource.FileSource(filename);

        GifOptions options=new GifOptions();

        options.setInIsOpaque(false);

        tex.init(GLES20.GL_TEXTURE_2D);
        tex.bind();;

        gif_tex=new GifTexImage2D(file,options);
        gif_tex.glTexImage2D(GLES20.GL_TEXTURE_2D,0);

        return(true);
    }

    @Override
    public void start()
    {
        gif_tex.startDecoderThread();
    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw()
    {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
        shader.begin();

            tex.bind(0);
            if(last_frame!=GetCurrentFrameIndex())
            {
                last_frame=GetCurrentFrameIndex();
                gif_tex.glTexImage2D(GLES20.GL_TEXTURE_2D,0);
            }

            render_layout.bind(shader.maPositionHandle);
            texture_uv.bind(shader.maTexCoordHandle);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();

        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public int GetGifWidth()
    {
        return gif_tex.getWidth();
    }
    public int GetGifHeight()
    {
        return gif_tex.getHeight();
    }

    public int GetGifNumberOfFrame()
    {
        return gif_tex.getNumberOfFrames();
    }

    public int GetCurrentFrameIndex()
    {
        return gif_tex.getCurrentFrameIndex();
    }

    public boolean isLastFrame()
    {
        return(gif_tex.getCurrentFrameIndex()== gif_tex.getNumberOfFrames()-1);
    }
}
