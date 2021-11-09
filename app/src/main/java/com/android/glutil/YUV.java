package com.android.glutil;

public class YUV
{
    static {
        System.loadLibrary("YUV");
    }

    public static native void argb2nv21(byte[] nv21,byte[] rgba,int width,int height);
}
