package net.runelite.client.plugins.timers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

@Singleton
class FreezeBarOverlay extends Overlay
{
    private static final Color BAR_FILL_COLOR = new Color(179, 224, 255);
    private static final Color BAR_BG_COLOR = Color.black;
    private static final Dimension FREEZE_BAR_SIZE = new Dimension(30, 5);

    private final Client client;
    private final TimersConfig config;
    private final TimersPlugin plugin;

    private boolean showingFreezeBar;

    @Inject
    private FreezeBarOverlay(final Client client, final TimersConfig config, final TimersPlugin plugin)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;

        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.showFreezeBar() || !showingFreezeBar)
        {
            return null;
        }

        final int height = client.getLocalPlayer().getLogicalHeight() + 14;
        final LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();
        final Point canvasPoint = Perspective.localToCanvas(client, localLocation, client.getPlane(), height);

        // Draw bar
        final int barX = canvasPoint.getX() - 15;
        final int barY = canvasPoint.getY();
        final int barWidth = FREEZE_BAR_SIZE.width;
        final int barHeight = FREEZE_BAR_SIZE.height;
        final float ratio = (float) plugin.freezeTimer.getTimer().getDuration().getSeconds() / 20.f;

        // Restricted by the width to prevent the bar from being too long while you are boosted above your real prayer level.
        final int progressFill = (int) Math.ceil(Math.min((barWidth * ratio), barWidth));

        graphics.setColor(BAR_BG_COLOR);
        graphics.fillRect(barX, barY, barWidth, barHeight);
        graphics.setColor(BAR_FILL_COLOR);
        graphics.fillRect(barX, barY, progressFill, barHeight);

        /*if ((plugin.isPrayersActive() || config.prayerFlickAlwaysOn())
                && (config.prayerFlickLocation().equals(PrayerFlickLocation.PRAYER_BAR)
                || config.prayerFlickLocation().equals(PrayerFlickLocation.BOTH)))
        {
            double t = plugin.getTickProgress();

            int xOffset = (int) (-Math.cos(t) * barWidth / 2) + barWidth / 2;

            graphics.setColor(FLICK_HELP_COLOR);
            graphics.fillRect(barX + xOffset, barY, 1, barHeight);
        }*/

        return new Dimension(barWidth, barHeight);
    }

    void onTick()
    {
        final Player localPlayer = client.getLocalPlayer();
        showingFreezeBar = true;

        if (localPlayer == null)
        {
            showingFreezeBar = false;
            return;
        }

        if (plugin.freezeTime <= 0)
        {
            showingFreezeBar = false;
            return;
        }

        if (localPlayer.getHealth() == -1)
        {
            showingFreezeBar = false;
        }
    }
}
