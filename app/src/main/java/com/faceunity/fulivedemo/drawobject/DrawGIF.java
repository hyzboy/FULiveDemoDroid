package com.faceunity.fulivedemo.drawobject;

import static android.graphics.Bitmap.createBitmap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.opengl.GLES20;
import android.util.Size;

import androidx.annotation.NonNull;

import com.faceunity.fulivedemo.gl.GL2Texture;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifOptions;
import pl.droidsonroids.gif.GifTexImage2D;
import pl.droidsonroids.gif.InputSource;

public class DrawGIF extends DrawObject
{
    private GifTexImage2D gif_tex=null;
    private ShaderAlpha shader=new ShaderAlpha();

    public DrawGIF()
    {
        super(ObjectType.GIF,false);
    }

    public boolean Load(String filename) throws IOException
    {
        InputSource.FileSource file=new InputSource.FileSource(filename);

        GifOptions options=new GifOptions();

        options.setInIsOpaque(false);

        gif_tex=new GifTexImage2D(file,options);


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
            gif_tex.glTexImage2D(GLES20.GL_TEXTURE_2D,0);
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
