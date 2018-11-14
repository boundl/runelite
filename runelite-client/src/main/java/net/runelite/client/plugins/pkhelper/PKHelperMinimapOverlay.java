package net.runelite.client.plugins.pkhelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class PKHelperMinimapOverlay extends Overlay
{
    private final PKHelperService pkHelperService;

    @Inject
    private PKHelperMinimapOverlay(PKHelperService pkHelperService)
    {
        this.pkHelperService = pkHelperService;
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        pkHelperService.forEachPlayer((player, color) -> renderPlayerOverlay(graphics, player, color));
        return null;
}

    private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color)
    {
        final net.runelite.api.Point minimapLocation = actor.getMinimapLocation();

        if (minimapLocation != null)
            OverlayUtil.renderTextLocation(graphics, minimapLocation, Integer.toString(actor.getCombatLevel()), color);
    }
}
