package net.runelite.client.plugins.nexthitnotifier;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class NextHitNotifierOverlay extends Overlay
{
    private final Client client;
    private final NextHitNotifierPlugin plugin;
    private final NextHitNotifierConfig config;

    private final PanelComponent panelComponent = new PanelComponent();
    private final Dimension panelSize = new Dimension(26, 0);

    @Inject
    private NextHitNotifierOverlay(Client client, NextHitNotifierPlugin plugin, NextHitNotifierConfig config)
    {
        setPosition(OverlayPosition.BOTTOM_RIGHT);
        setPosition(OverlayPosition.DETACHED);

        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();
        panelComponent.setPreferredSize(panelSize);

        String lastHitText = Integer.toString(plugin.lastHit);
        int lastHit = plugin.lastHit;

        if (plugin.showTime < 0)
        {
            lastHitText = "";
            lastHit = 0;
        }

        int r = (int)Math.min(Math.floor(lastHit / 50) * 255, 255);
        int g = 255 - r;

        Color textColor = Color.getHSBColor(Color.RGBtoHSB(r, g, 0, null)[0], 1.f, 1.f);

        panelComponent.getChildren().add(TitleComponent.builder().text(lastHitText).color(textColor).build());

        return panelComponent.render(graphics);
    }
}