package com.faceunity.fulivedemo.videoplayer;

/**
 * media player state listener define.
 */
public interface MediaPlayerStateListener {

    /**
     * on play start state call back
     *
     * @param path {@link String} video file path
     */
    void onPlayStart(String path);

    /**
     * play completed state call back
     */
    void onPlayCompleted();

    /**
     * on play stop state call back
     */
    void onPlayStop();
}
