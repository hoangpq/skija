package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;

public class MaskFiltersScene implements Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        
        try (Paint fill   = new Paint().setColor(0xFFe76f51);
             Path  figure = new Path().moveTo(75, 19).lineTo(140, 131).lineTo(10, 131).closePath()
                              .moveTo(75, 56).lineTo(42, 113).lineTo(107, 113).closePath();)
        {
            MaskFilter outer = MaskFilter.blur(MaskFilter.BlurStyle.OUTER, 5);
            MaskFilter inner = MaskFilter.blur(MaskFilter.BlurStyle.INNER, 5);
            MaskFilter shader = MaskFilter.shader(Shader.linearGradient(10, 0, 140, 0, new int[] { 0xFF000000, 0x00000000 }));
            byte[] table = new byte[256];
            for (int i = 0; i < 256; ++i) {
                table[i] = (byte) (255 - i);
            }

            MaskFilter[][] filters = new MaskFilter[][] {
                new MaskFilter[] {
                    MaskFilter.blur(MaskFilter.BlurStyle.NORMAL, 10),
                    MaskFilter.blur(MaskFilter.BlurStyle.NORMAL, 5),
                    MaskFilter.blur(MaskFilter.BlurStyle.SOLID, 5),
                    outer,
                    inner,
                    MaskFilter.blur(MaskFilter.BlurStyle.NORMAL, 5, false),
                    shader
                }
            };

            for (MaskFilter[] fs: filters) {
                canvas.save();
                for (MaskFilter f: fs) {
                    fill.setMaskFilter(f);
                    canvas.drawPath(figure, fill);
                    canvas.translate(150, 0);
                    f.close();
                }
                canvas.restore();
                canvas.translate(0, 150);
            }
        }
    }
}
