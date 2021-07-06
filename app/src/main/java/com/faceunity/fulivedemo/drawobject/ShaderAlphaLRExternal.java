package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderAlphaLRExternal extends ShaderModule
{
    private final String mFragmentShaderAlphaLRExternal =
            "#extension GL_OES_EGL_image_external : require\n"
                    + "precision highp float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "  vec2  pos  =vTextureCoord*vec2(0.5,1.0);\n"
                    + "  vec3  rgb  =texture2D(sTexture,pos).rgb;\n"
                    + "  pos.x+=0.5;"
                    + "  float alpha=texture2D(sTexture,pos).r;\n"
                    + "  gl_FragColor = vec4(rgb,alpha);\n"
                    + "}\n";

    private int texture_location=-1;

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderAlphaLRExternal)) {
            return (false);
        }

        texture_location=GLES20.glGetUniformLocation(mProgram,"sTexture");

        return (true);
    }

    public ShaderAlphaLRExternal()
    {
        super("ShaderAlphaLRExternal");
        init();
    }

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_location,0);
    }
}
