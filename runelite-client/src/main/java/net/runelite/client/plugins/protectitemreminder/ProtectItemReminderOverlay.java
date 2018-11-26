package net.runelite.client.plugins.protectitemreminder;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class ProtectItemReminderOverlay extends Overlay
{
    private final Client client;
    private final ProtectItemReminderPlugin plugin;

    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private ProtectItemReminderOverlay(Client client, ProtectItemReminderPlugin plugin)
    {
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        setPosition(OverlayPosition.DETACHED);

        this.client = client;
        this.plugin = plugin;
    }
    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();

        if (!plugin.shouldRemind)
            return null;

        panelComponent.getChildren().add(TitleComponent.builder().text("ENABLE PROTECT ITEM").color(Color.RED).build());
        panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth("ENABLE PROTECT ITEM") + 10, 0));

        return panelComponent.render(graphics);
    }
}
