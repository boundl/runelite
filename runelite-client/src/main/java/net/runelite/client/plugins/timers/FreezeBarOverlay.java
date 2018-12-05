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
    private static final Dimension FREEZE_BAR_SIZE = new Dimension(30, 2);

    private final Client client;
    private final TimersConfig config;
    private final TimersPlugin plugin;

    private boolean showingFreezeBar;
    private float ratio = -1.f;

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
        if (!config.showFreezeBar() || plugin.freezeTimer == null)
            return null;

        final int start_tick = plugin.freezeTime;
        final int current_tick = client.getTickCount();
        final int duration_ticks = (int)(plugin.freezeTimer.getTimer().getDuration().getSeconds() / 0.6) + 1;
        final int spent_ticks = current_tick - start_tick;

        ratio = 1.0f - ((float)spent_ticks / (float)duration_ticks);

        if (ratio <= 0.f)
            return null;

        if (!showingFreezeBar)
            return null;

        final int height = client.getLocalPlayer().getLogicalHeight() + 10;
        final LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();
        final Point canvasPoint = Perspective.localToCanvas(client, localLocation, client.getPlane(), height);

        // Draw bar
        final int barX = canvasPoint.getX() - 15;
        final int barY = canvasPoint.getY() - config.freezeBarHeight() + 15;
        final int barWidth = FREEZE_BAR_SIZE.width;
        final int barHeight = FREEZE_BAR_SIZE.height;

        //Restricted by the width to prevent the bar from being too long
        final int progressFill = (int) Math.ceil(Math.min((barWidth * ratio), barWidth));

        graphics.setColor(BAR_BG_COLOR);
        graphics.fillRect(barX, barY, barWidth, barHeight);
        graphics.setColor(BAR_FILL_COLOR);
        graphics.fillRect(barX, barY, progressFill, barHeight);

        if (config.showFreezeBarCount())
            graphics.drawString(Integer.toString((int)Math.ceil(((duration_ticks - spent_ticks) * 0.6f))), barX + progressFill, barY);

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

        if (ratio >= 1.f || ratio <= 0.f)
        {
            showingFreezeBar = false;
            return;
        }

        if (plugin.freezeTimer == null)
        {
            showingFreezeBar = false;
            return;
        }

        if (plugin.freezeTime <= 0)
        {
            showingFreezeBar = false;
            return;
        }
    }
}
