#extension GL_OES_EGL_image_external : require
precision highp float;

varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;

void main()
{
  gl_FragColor = vec4(texture2D(sTexture, vTextureCoord).rgb,1.0);
}