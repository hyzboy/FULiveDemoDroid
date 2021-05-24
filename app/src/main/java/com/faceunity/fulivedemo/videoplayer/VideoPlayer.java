/*
 * Copyright (c) 2020. Engineer-Jsp
 *
 * Any document of this project is owned by Engineer-Jsp;
 * Without the permission of the company, it is forbidden to send any
 * documents of this project to anyone who has nothing to do with the project.
 * About the project author , You can visit he's other open source projects or he's blog
 *
 * CSDN   : https://blog.csdn.net/jspping
 * GitHub : https://github.com/Mr-Jiang
 *
 * Once again explanation, it is forbidden to disclose any documents of the project
 * to anyone who has nothing to do with the project without the permission of
 * the Engineer-Jsp otherwise legal liability will be pursued according to law.
 */
package com.faceunity.fulivedemo.videoplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.IOException;

/**
 * mp4 player.
 *
 * @author : Engineer-Jsp
 * @date : Created by 2020/10/12 15:50:47
 */
public class VideoPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final String TAG = "VideoPlayer";

    /**
     * {@link Context} instance object
     */
    private Context context;

    /**
     * {@link MediaPlayer} instance object
     */
    private MediaPlayer mMediaPlayer;

    /**
     * {@link MediaPlayerStateListener} instance object
     */
    private com.faceunity.fulivedemo.videoplayer.MediaPlayerStateListener mediaPlayerStateListener;

    /**
     * video file absolute path
     */
    private String path;

    /**
     * construction method , initialization {@link MediaPlayer} instance object
     *
     * @param context   {@link Context} instance object
     */
    public VideoPlayer(Context context) {
        this.context = context;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    /**
     * set media player state listener
     *
     * @param mediaPlayerStateListener {@link MediaPlayerStateListener}
     */
    public void setOnMediaPlayerStateListener(com.faceunity.fulivedemo.videoplayer.MediaPlayerStateListener mediaPlayerStateListener) {
        this.mediaPlayerStateListener = mediaPlayerStateListener;
    }

    /**
     * set data source
     *
     * @param path video file absolute path
     */
    public void setDataSource(String path) {
        Log.e(TAG, "setDataSource path = " + path);
        this.path = path;
        if (isUrlValid()) {
            if (mediaPlayerStateListener != null) {
                mediaPlayerStateListener.onPlayStart(path);
            }
        }
    }

    /**
     * determine whether it is a valid url
     *
     * @return true means video file valid
     */
    private boolean isUrlValid() {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File videoFile = new File(path);
        if (!videoFile.exists() || videoFile.isDirectory() || !videoFile.canRead()) {
            return false;
        }
        return true;
    }

    /**
     * configure mp4 player parameter
     *
     * @param surface {@link Surface} instance object
     */
    public void configure(Surface surface) {
        if (mMediaPlayer != null) {
            Log.d(TAG, "configure...");
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setScreenOnWhilePlaying(true);
        }
    }

    /**
     * media player prepare
     */
    public void prepare() {
        if (isUrlValid() && mMediaPlayer != null) {
            Log.e(TAG, "prepare...");
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                Log.e(TAG, "IOException : " + e.toString());
            }
        }
    }

    /**
     * start play
     */
    public void startPlay() {
        if (isUrlValid() && mMediaPlayer != null) {
            Log.e(TAG, "startPlay...");
            mMediaPlayer.start();
        }
    }

    /**
     * stop play
     */
    public void stopPlay() {
        Log.e(TAG, "stopPlay...");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            if (mediaPlayerStateListener != null) {
                mediaPlayerStateListener.onPlayStop();
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e(TAG, "onCompletion...");
        if (mediaPlayerStateListener != null) {
            mediaPlayerStateListener.onPlayCompleted();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Log.e(TAG, "onError...");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        return true;
    }

}