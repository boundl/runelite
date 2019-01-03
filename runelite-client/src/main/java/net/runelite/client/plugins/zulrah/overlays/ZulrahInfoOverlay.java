package net.runelite.client.plugins.zulrah.overlays;

import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.client.plugins.zulrah.ZulrahInstance;
import net.runelite.client.plugins.zulrah.ZulrahPlugin;
import net.runelite.client.plugins.zulrah.phase.ZulrahPhase;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ZulrahInfoOverlay extends Overlay
{
    private final Client client;
    private final ZulrahPlugin plugin;
    private final PanelComponent imagePanelComponent = new PanelComponent();

    @Inject
    ZulrahInfoOverlay(Client client, ZulrahPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.BOTTOM_RIGHT);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        ZulrahInstance instance = plugin.getInstance();

        if (instance == null)
            return null;

        imagePanelComponent.getChildren().clear();

        ZulrahPhase currentPhase = instance.getPhase();

        if (currentPhase == null)
            return null;

        if (currentPhase.isJad())
            imagePanelComponent.getChildren().add(TitleComponent.builder().text("JAD PHASE").build());

        Prayer prayer = currentPhase.isJad() ? null : currentPhase.getPrayer();

        if (prayer == null || client.isPrayerActive(prayer))
            return null;

        imagePanelComponent.getChildren().add(TitleComponent.builder().text("Switch!").build());

        BufferedImage prayerImage = ZulrahImageManager.getProtectionPrayerBufferedImage(prayer);
        imagePanelComponent.getChildren().add(new ImageComponent(prayerImage));

        return imagePanelComponent.render(graphics);
    }


}