package org.deletethis.logfront.util;

import java.awt.Color;

/**
 * taken from
 * http://ariya.blogspot.sk/2008/07/converting-between-hsl-and-hsv.html
 *
 * @author miko
 *
 */
public class HSL {

    private static float clamp01(float f) {
        if(f < 0f) {
            return 0;
        } else if(f >= 1f) {
            return 0.99999f; // didn't work properly with 1f
        } else {
            return f;
        }

    }

    private static void clamp01(float[] f) {
        for(int i = 0; i < f.length; ++i) {
            f[i] = clamp01(f[i]);
        }
    }

    private static void hsb_to_hsl(float h, float s, float b, float[] out) {
        h = clamp01(h);
        s = clamp01(s);
        b = clamp01(b);
        out[0] = h;
        out[2] = (2f - s) * b;
        out[1] = s * b;
        out[1] /= (out[2] <= 1) ? (out[2]) : 2 - (out[2]);
        out[2] /= 2f;
        clamp01(out);
    }

    private static void hsl_to_hsb(float hh, float ss, float ll, float[] out) {
        hh = clamp01(hh);
        ss = clamp01(ss);
        ll = clamp01(ll);
        out[0] = hh;
        ll *= 2f;
        ss *= (ll <= 1) ? ll : 2f - ll;
        out[2] = (ll + ss) / 2f;
        out[1] = (2 * ss) / (ll + ss);
        clamp01(out);
    }

    public static Color create(float h, float s, float l) {
        float[] arr = new float[ 3 ];
        hsl_to_hsb(h, s, l, arr);

        return new Color(Color.HSBtoRGB(arr[0], arr[1], arr[2]));
    }

    public static float[] get(Color c) {
        return get(c.getRed(), c.getGreen(), c.getBlue());
    }

    public static float[] get(int r, int g, int b) {
        float[] arr = Color.RGBtoHSB(r, g, b, null);
        hsb_to_hsl(arr[0], arr[1], arr[2], arr);
        return arr;
    }
}
