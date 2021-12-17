#extension GL_OES_EGL_image_external : require
precision highp float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;

void main()
{
  vec2  pos  =vTextureCoord*vec2(0.5,1.0);
  vec3  rgb  =texture2D(sTexture,pos).rgb;
  pos.x+=0.5;
  float alpha=texture2D(sTexture,pos).r;
  gl_FragColor = vec4(rgb,alpha);
}