package com.faceunity.fulivedemo.drawobject;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;

import com.faceunity.fulivedemo.gl.GL2Texture;

import java.io.IOException;

public class GL2VideoTexture extends GL2Texture implements SurfaceTexture.OnFrameAvailableListener{

    private final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    public SurfaceTexture surfaceTexture;

    public void init(MediaPlayer player)
    {
        init(GL_TEXTURE_EXTERNAL_OES);

        surfaceTexture = new SurfaceTexture(texture_id);
        surfaceTexture.setOnFrameAvailableListener(this);

        Surface surface = new Surface(surfaceTexture);
        player.setSurface(surface);
        surface.release();

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surface)
    {
        surfaceTexture.updateTexImage();
    }
}
