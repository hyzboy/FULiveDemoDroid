package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderOpaqueExternal extends ShaderModule {

    private final String mFragmentShaderOpaqueExternal =
                      "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "  gl_FragColor = vec4(texture2D(sTexture, vTextureCoord).rgb,1.0);\n"
                    + "}\n";

    private int texture_location=-1;

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderOpaqueExternal)) {
            return (false);
        }

        texture_location= GLES20.glGetUniformLocation(mProgram,"sTexture");

        return (true);
    }

    public ShaderOpaqueExternal()
    {
        super("ShaderOpaqueExternal");
        init();
    }

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_location,0);
    }
}
