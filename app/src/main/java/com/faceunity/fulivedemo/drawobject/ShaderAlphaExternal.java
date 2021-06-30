package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderAlphaExternal extends ShaderModule
{
    private final String mFragmentShaderAlphaExternal =
            "#extension GL_OES_EGL_image_external : require\n"
                    + "precision highp float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTextureRGB;\n"
                    + "uniform samplerExternalOES sTextureAlpha;\n"
                    + "void main() {\n"
                    + "  vec3  rgb  =texture2D(sTextureRGB,   vTextureCoord).rgb;\n"
                    + "  float alpha=texture2D(sTextureAlpha, vTextureCoord).r;\n"
                    + "  gl_FragColor = vec4(rgb,alpha);\n"
                    + "}\n";

    private int texture_rgb_location=-1;
    private int texture_alpha_location=-1;

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderAlphaExternal)) {
            return (false);
        }

        texture_rgb_location=GLES20.glGetUniformLocation(mProgram,"sTextureRGB");
        texture_alpha_location=GLES20.glGetUniformLocation(mProgram,"sTextureAlpha");

        return (true);
    }

    public ShaderAlphaExternal()
    {
        super("ShaderAlphaExternal");
        init();
    }

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_rgb_location,0);
        GLES20.glUniform1i(texture_alpha_location,1);
    }
}
