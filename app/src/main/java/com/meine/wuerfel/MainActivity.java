package com.meine.wuerfel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import java.util.Arrays;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(new CubeView(this));
    }

    static class CubeView extends View {
        final float[][] v = {
            {-1,-1,-1},{1,-1,-1},{1,1,-1},{-1,1,-1},
            {-1,-1,1},{1,-1,1},{1,1,1},{-1,1,1}};
        final int[][] f = {
            {0,1,2,3},{4,5,6,7},{0,1,5,4},
            {2,3,7,6},{0,3,7,4},{1,2,6,5}};
        final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        float a = 0;

        CubeView(Context c) {
            super(c);
        }

        @Override
        protected void onDraw(Canvas c) {
            c.drawColor(Color.rgb(15, 15, 25));
            a += 0.02f;
            float w = getWidth(), h = getHeight();
            float s = Math.min(w, h) * 0.2f;
            float ca = (float) Math.cos(a), sa = (float) Math.sin(a);
            float cb = (float) Math.cos(a * 0.7f), sb = (float) Math.sin(a * 0.7f);
            float[][] r = new float[8][3];
            for (int i = 0; i < 8; i++) {
                float x = v[i][0], y = v[i][1], z = v[i][2];
                float x1 = x * ca + z * sa;
                float z1 = -x * sa + z * ca;
                float y1 = y * cb - z1 * sb;
                float z2 = y * sb + z1 * cb;
                r[i][0] = x1;
                r[i][1] = y1;
                r[i][2] = z2;
            }
            final float[] d = new float[6];
            for (int i = 0; i < 6; i++) {
                for (int k : f[i]) d[i] += r[k][2];
            }
            Integer[] order = {0, 1, 2, 3, 4, 5};
            Arrays.sort(order, (m, n) -> Float.compare(d[n], d[m]));
            for (int idx : order) {
                Path path = new Path();
                for (int j = 0; j < 4; j++) {
                    int k = f[idx][j];
                    float sc = 4f / (r[k][2] + 4f);
                    float px = w / 2 + r[k][0] * s * sc;
                    float py = h / 2 + r[k][1] * s * sc;
                    if (j == 0) path.moveTo(px, py);
                    else path.lineTo(px, py);
                }
                path.close();
                float t = (-d[idx] / 4f + 1.5f) / 3f;
                t = Math.max(0f, Math.min(1f, t));
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.rgb((int) (30 + 40 * t), (int) (80 + 90 * t), (int) (180 + 75 * t)));
                c.drawPath(path, p);
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(4f);
                p.setColor(Color.rgb(190, 215, 255));
                c.drawPath(path, p);
            }
            postInvalidateOnAnimation();
        }
    }
}
