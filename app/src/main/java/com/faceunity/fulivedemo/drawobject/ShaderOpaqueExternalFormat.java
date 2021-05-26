package com.faceunity.fulivedemo.drawobject;

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

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderOpaqueExternal)) {
            return (false);
        }

        return (true);
    }

    public ShaderOpaqueExternal()
    {
        super("ShaderOpaqueExternal");
        init();
    }
}
