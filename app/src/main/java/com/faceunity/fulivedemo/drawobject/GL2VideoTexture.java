package com.faceunity.fulivedemo.drawobject;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;

import com.faceunity.fulivedemo.gl.GL2Texture;
import com.faceunity.fulivedemo.videoplayer.MediaPlayerStateListener;
import com.faceunity.fulivedemo.videoplayer.VideoPlayer;

import java.io.IOException;

public class GL2VideoTexture extends GL2Texture implements MediaPlayerStateListener,SurfaceTexture.OnFrameAvailableListener
{
    private final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;
    private SurfaceTexture surfaceTexture=null;
    private Surface surface=null;
    private VideoPlayer player=null;

    private boolean updateSurface=false;

    public GL2VideoTexture(VideoPlayer vp)
    {
        super("GL2VideoTexture");

        init(GL_TEXTURE_EXTERNAL_OES);

        player=vp;
        player.setOnMediaPlayerStateListener(this);
    }

    public void create()
    {
        surfaceTexture = new SurfaceTexture(super.texture_id);
        surfaceTexture.setOnFrameAvailableListener(this);

        surface = new Surface(surfaceTexture);

        if(player!=null)
        {
            player.configure(surface);
            player.prepare();
        }

        surface.release();
    };

    public void update()
    {
        synchronized (this)
        {
            if(updateSurface)
            {
                if(surfaceTexture!=null)
                    surfaceTexture.updateTexImage();

                updateSurface=false;
            }
        }
    }

    @Override
    public void onPlayStart(String path)
    {
        if (player != null) {
            player.prepare();
            player.startPlay();
        }
    }

    @Override
    public void onPlayCompleted()
    {
        if (player != null) {
            player.startPlay();
        }
    }

    @Override
    public void onPlayStop(){}

    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture)
    {
        updateSurface=true;
    }
}
