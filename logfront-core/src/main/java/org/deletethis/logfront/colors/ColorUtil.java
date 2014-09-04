package org.deletethis.logfront.colors;

import java.awt.Color;

import org.deletethis.logfront.message.Name;
import org.deletethis.logfront.util.HSL;

public class ColorUtil {

    public static float getColorLuminance(Color color) {
        return HSL.get(color)[2];
    }

    public static boolean isLuminanceBright(float luminance) {
        return luminance > 0.5;

    }

    public static boolean isColorBright(Color color) {
        return isLuminanceBright(getColorLuminance(color));
    }

    private static float stringHue(String str) {
        int hash = str.hashCode();
        if(hash < 0) {
            hash = -hash;
        }

        return ((float) hash / (float) Integer.MAX_VALUE);
    }

    public static float getThreadNameHue(String threadName) {
        return stringHue(threadName);
    }

    public static float getLoggerHue(Name name) {
        float h = stringHue(name.getName());

        if(name.getParent() == null) {
            return h;
        } else {
            h = getLoggerHue(name.getParent()) + (h * 0.8f) - 0.4f;
            if(h < 0f) {
                h += 1f;
            } else if(h > 1f) {
                h -= 1f;
            }

            return h;
        }
    }

    public static Color between(Color c1, Color c2) {
        return new Color(
                (c1.getRed() + c2.getRed()) / 2,
                (c1.getGreen() + c2.getGreen()) / 2,
                (c1.getBlue() + c2.getBlue()) / 2);

    }

    public static Color colorMult(Color c, float f) {
        int r = (int) (c.getRed() * f);
        int g = (int) (c.getGreen() * f);
        int b = (int) (c.getBlue() * f);
        if(f > 1f) {
            if(r > 255) {
                r = 255;
            }
            if(g > 255) {
                g = 255;
            }
            if(b > 255) {
                b = 255;
            }
        }
        return new Color(r, g, b);
    }
}
