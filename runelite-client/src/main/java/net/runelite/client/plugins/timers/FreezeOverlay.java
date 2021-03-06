package net.runelite.client.plugins.timers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.runelite.api.Perspective;
import net.runelite.api.Point;
//import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import  net.runelite.client.plugins.timers.FreezeManager;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;
import net.runelite.api.Actor;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TextComponent;


@Singleton
public class FreezeOverlay extends Overlay
{
    private final FreezeManager freezeManager;

    @Inject
    private SpriteManager spriteManager;

    @Inject
    private ItemManager itemManager;

    TimersConfig config;

    private static final Color BAR_FILL_COLOR = new Color(179, 224, 255);
    private static final Color BAR_BG_COLOR = Color.black;
    private static final Dimension FREEZE_BAR_SIZE = new Dimension(30, 2);

    @Inject
    private FreezeOverlay(FreezeManager freezeManager, TimersConfig config)
    {
        this.freezeManager = freezeManager;
        this.config = config;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        for (Map.Entry<String, FreezeInfo> entry : freezeManager.getFreezeInfo())
        {
            FreezeInfo info = entry.getValue();
            Actor actor = info.getActor();

            // TODO: Resize image
            // look at InfoBoxManager

            int offset = actor.getLogicalHeight() - 180;
            BufferedImage freezeImage = info.getGameTimer().getImage(itemManager, spriteManager);
            Point imageLocation = actor.getCanvasImageLocation(freezeImage, offset);

            //int barOffset = actor.getLogicalHeight() + 10;
            //Point barLocation = actor.getCanvasTextLocation(graphics, "frozen", barOffset);

            if (imageLocation != null)
            {
                // Render image
                OverlayUtil.renderImageLocation(graphics, imageLocation, freezeImage);
                // Render caption
                final TextComponent textComponent = new TextComponent();
                textComponent.setColor(Color.WHITE);
                textComponent.setText(info.getTimer().getText());
                textComponent.setPosition(new java.awt.Point(imageLocation.getX(), imageLocation.getY()));
                textComponent.render(graphics);
            }

            /*if (barLocation != null)
            {
                float ratio = ((float)info.getTimer().getTimer().getDuration().getSeconds() / 20.f);

                // Draw bar
                final int barX = barLocation.getX() - 15;
                final int barY = barLocation.getY() - config.freezeBarHeight() + 15;
                final int barWidth = FREEZE_BAR_SIZE.width;
                final int barHeight = FREEZE_BAR_SIZE.height;

                //Restricted by the width to prevent the bar from being too long
                final int progressFill = (int)Math.ceil(Math.min((barWidth * ratio), barWidth));

                graphics.setColor(BAR_BG_COLOR);
                graphics.fillRect(barX, barY, barWidth, barHeight);
                graphics.setColor(BAR_FILL_COLOR);
                graphics.fillRect(barX, barY, progressFill, barHeight);
            }*/
        }
        return null;

    }

}