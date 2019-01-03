package net.runelite.client.plugins.zulrah.overlays;

import net.runelite.api.Prayer;
import net.runelite.client.plugins.zulrah.ZulrahPlugin;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

class ZulrahImageManager
{
    private static final BufferedImage[] prayerBufferedImages = new BufferedImage[2];

    static BufferedImage getProtectionPrayerBufferedImage(Prayer prayer)
    {
        switch (prayer)
        {
            case PROTECT_FROM_MAGIC:
                if (prayerBufferedImages[0] == null)
                {
                    prayerBufferedImages[0] = getBufferedImage("/prayers/protect_from_magic.png");
                }
                return prayerBufferedImages[0];
            case PROTECT_FROM_MISSILES:
                if (prayerBufferedImages[1] == null)
                {
                    prayerBufferedImages[1] = getBufferedImage("/prayers/protect_from_missiles.png");
                }
                return prayerBufferedImages[1];
        }
        return null;
    }

    private static BufferedImage getBufferedImage(String path)
    {
        BufferedImage image = null;
        try
        {
            InputStream in = ZulrahPlugin.class.getResourceAsStream(path);
            image = ImageIO.read(in);
        }
        catch (IOException e)
        {
            //swallow exception
        }
        return image;
    }
}