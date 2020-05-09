package com.sabah.neuro

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

val THRESHOLD = 128

class PictureConverter {
    fun convert(path: String) {
        val input =
            File("/Users/sabahtalateh/Downloads/cell_images/Parasitized/C33P1thinF_IMG_20150619_114756a_cell_180.png");
        val image = ImageIO.read(input);
        val width = image.width;
        val height = image.height;
        for (i in 0 until height) {
            for (j in 0 until width) {
                val color = Color(image.getRGB(j, i))
                val result = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
                val newColor = if (result < THRESHOLD) Color.BLACK else Color.WHITE
                image.setRGB(j, i, newColor.rgb)
            }

        }
        val ouptut = File("/Users/sabahtalateh/kkk/image.png");
        ImageIO.write(image, "png", ouptut);
    }
}
