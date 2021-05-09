package com.faceunity.fulivedemo.gl;

public class RenderLayout extends GL2FloatBuffer
{
    private float[] vertices_data=null;

    public void init()
    {
        vertices_data=new float[8];
        init(8);
    }

    /**
     * 设置布局<br>
     * 坐标使用左上角为0,0，右下角为1,1的数值范围。例如设置田字型格就是
     * 左坐标  上坐标   宽度     高度
     * 左上角格：   0.0     0.0     0.5     0.5
     * 左下角格：   0.0     0.5     0.5     0.5
     * 右上角格：   0.5     0.0     0.5     0.5
     * 右下角格：   0.5     0.5     0.5     0.5
     *
     * @param l     左坐标
     * @param t     上坐标
     * @param w     宽
     * @param h     高
     */
    public boolean set(float l, float t, float w, float h)
    {
        l *= 2;
        t *= 2;
        w *= 2;
        h *= 2;

        l -= 1;
        t -= 1;

        float b = -(t + h);
        float r = l + w;

        t *= -1;

        vertices_data[0] = l;
        vertices_data[1] = b;
        vertices_data[2] = r;
        vertices_data[3] = b;
        vertices_data[4] = l;
        vertices_data[5] = t;
        vertices_data[6] = r;
        vertices_data[7] = t;

        setData(vertices_data);
        return(true);
    }
}
