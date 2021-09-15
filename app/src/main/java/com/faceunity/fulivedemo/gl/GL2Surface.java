package com.faceunity.fulivedemo.gl;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;

public class GL2Surface extends GLClass
{
    private EGLDisplay  egl_display = EGL14.EGL_NO_DISPLAY;
    private EGLContext  egl_context = EGL14.EGL_NO_CONTEXT;
    private EGLConfig   egl_config  = null;
    private EGLSurface  egl_surface = null;

    public GL2Surface(String tag)
    {
        super("GL2Surface");
    }

    public void Init(EGLDisplay disp,EGLConfig conf,EGLContext cont)
    {
        egl_display=disp;
        egl_config =conf;
        egl_context=cont;
    }

    public boolean Create(int width,int height)
    {
        if(egl_display ==null|| egl_config ==null)
            return(false);

        int[] surface_attribs =
        {
            EGL14.EGL_WIDTH, width,
            EGL14.EGL_HEIGHT, height,
            EGL14.EGL_NONE
        };

        egl_surface = EGL14.eglCreatePbufferSurface(egl_display, egl_config, surface_attribs, 0);

        CheckGLError("eglCreatePbufferSurface");

        if (egl_surface == null)
            return(false);

        return(true);
    }

    public void Release()
    {
        EGL14.eglDestroySurface(egl_display,egl_surface);
        egl_surface=null;
    }

    public EGLSurface GetSurface()
    {
        return egl_surface;
    }

    public boolean MakeCurrent()
    {
        if(egl_surface==null)
            return(false);

        if (!EGL14.eglMakeCurrent(egl_display, egl_surface, egl_surface, egl_context)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }

        return(true);
    }

    public boolean isDraw(EGLSurface eglSurface)
    {
        return egl_context.equals(EGL14.eglGetCurrentContext()) &&
                eglSurface.equals(EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW));
    }

    public boolean isRead(EGLSurface eglSurface)
    {
        return egl_context.equals(EGL14.eglGetCurrentContext()) &&
                eglSurface.equals(EGL14.eglGetCurrentSurface(EGL14.EGL_READ));
    }

    public boolean SwapBuffer()
    {
        return EGL14.eglSwapBuffers(egl_display,egl_surface);
    }
}
