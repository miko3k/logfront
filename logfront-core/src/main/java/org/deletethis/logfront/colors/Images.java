package org.deletethis.logfront.colors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Images {

    public static Image createCloseImage(int size, int border, int linewidth, Color color) {
        if(linewidth < 1) {
            linewidth = 1;
        }

        BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();

        /*
         *    a   b   c   d
         *   a+---+   +---+a
         *    |    \ /    |
         *   b+     \     +b
         *     \     \   /
         *      \     \ /
         *       \     \
         *      / \     \
         *     /   \     \
         *   c+     \     +c   
         *    |    / \    | 
         *   d+---+   +---+d  
         *    a   b   c   d
         */
        int a = border;
        int b = border + linewidth;
        int c = size - border - linewidth - 1;
        int d = size - border - 1;

        /*
         // draw bounds in red (to check if we cover desired area)
         g.setColor(Color.RED);
         g.drawLine(a, a, d, a);
         g.drawLine(d, a, d, d);
         g.drawLine(d, d, a, d);
         g.drawLine(a, d, a, a);
         */
        // fillPolygon seems to omit bottom and right pixel, so add one
//		int [] x = { a, a, b+1, d+1, d+1, c }; 
//		int [] y = { b+1, a, a, c, d+1, d+1 };
        int[] x = {a, a, b, d, d, c};
        int[] y = {b, a, a, c, d, d};

        // fillPolygon behaves strangely so we both draw and fill it
        g.setColor(color);
        g.fillPolygon(x, y, 6);
        g.drawPolygon(x, y, 6);

        // other one
        x = new int[] {c, d, d, b, a, a};
        y = new int[] {a, a, b, d, d, c};
        g.fillPolygon(x, y, 6);
        g.drawPolygon(x, y, 6);

        return result;
    }

    public static Image createArrowImage(int size, Color color, boolean flip, boolean rotate) {
        /*    a   b
         *   k+\          
         *    | \        
         *    |  \       
         *   l|   \       
         *    |   /  
         *    |  /     
         *    | /   
         *   m+/    
         *     ==M==
         */

        int M = size / 3;
        int a = (size - M) / 2;
        int b = a + M;
        int k = (size - M * 2) / 2;
        int l = k + M;
        int m = l + M;

        int[] x = new int[] {a, b, a};
        int[] y = new int[] {k, l, m};

        if(flip) {
            for(int i = 0; i < x.length; ++i) {
                x[i] = size - 1 - x[i];
            }
        }
        if(rotate) {
            int[] tmp = y;
            y = x;
            x = tmp;
        }
        BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.setColor(color);
        g.fillPolygon(x, y, 3);
        g.drawPolygon(x, y, 3);

        return result;
    }

}
