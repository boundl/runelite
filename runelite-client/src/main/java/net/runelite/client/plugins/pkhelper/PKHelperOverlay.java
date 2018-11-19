package net.runelite.client.plugins.pkhelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.api.Actor;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class PKHelperOverlay extends Overlay
{
    private final PKHelperService pkHelperService;
    private final PKHelperConfig config;

    @Inject
    private PKHelperOverlay(PKHelperConfig config, PKHelperService pkHelperService, PKHelperPlugin pkHelperPlugin)
    {
        this.config = config;
        this.pkHelperService = pkHelperService;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        pkHelperService.forEachPlayer((player, color) -> renderPlayerOverlay(graphics, player, color));
        return null;
    }

    private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color)
    {
        if (!config.drawPlayerNames() && !config.drawPlayerLevels())
            return;

        String text = "";
        if (config.drawPlayerLevels())
            text += "(" + Integer.toString(actor.getCombatLevel()) + ") ";

        if (config.drawPlayerNames())
            text += actor.getName().replace('\u00A0', ' ');

        Point textLocation = actor.getCanvasTextLocation(graphics, text, actor.getLogicalHeight() + 40);

        if (textLocation != null)
            OverlayUtil.renderTextLocation(graphics, textLocation, text, color);
    }
}