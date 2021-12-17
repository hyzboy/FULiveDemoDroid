attribute vec2 aPosition;
attribute vec2 aTextureCoord;
varying vec2 vTextureCoord;
uniform int direction;
uniform int mirror;
uniform int flip;
uniform mat4 projection_matrix;

void main()
{
  float x,y;
  vec2 tc;

  if(mirror==1)
    x=1.0-aTextureCoord.x;
  else
    x=aTextureCoord.x;

  if(flip==1)
    y=1.0-aTextureCoord.y;
  else
    y=aTextureCoord.y;

  if(direction==0)
    tc=vec2(x,y);
  else
    tc=vec2(y,1.0-x);

  vTextureCoord = tc;

  gl_Position = projection_matrix*vec4(aPosition,0.0,1.0);
}