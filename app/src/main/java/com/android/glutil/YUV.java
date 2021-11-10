package com.android.glutil;

import java.nio.Buffer;

public class YUV
{
    static {
        System.loadLibrary("YUV");
    }

    public static native void argb2nv21(byte[] nv21, Buffer rgba, int width, int height);
}
