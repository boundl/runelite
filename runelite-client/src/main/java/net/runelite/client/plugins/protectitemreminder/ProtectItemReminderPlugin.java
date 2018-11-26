package net.runelite.client.plugins.protectitemreminder;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

@PluginDescriptor(
        name = "Protect Item Reminder",
        description = "Reminds Brian to protect item when in the wilderness.",
        tags = { "wilderness", "prayer", "protect", "item", "pking" },
        enabledByDefault = false
)
public class ProtectItemReminderPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ProtectItemReminderOverlay overlay;

    private Player localPlayer;

    public boolean shouldRemind = false;

    @Provides
    ProtectItemReminderConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ProtectItemReminderConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        shouldRemind = false;
        localPlayer = null;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() == GameState.LOGGED_IN)
        {
            localPlayer = client.getLocalPlayer();
        }
        else
        {
            localPlayer = null;
        }
    }

    int getWildernessLevelFrom(WorldPoint point)
    {
        int y = point.getY();               //v underground           //v above ground

        int wildernessLevel = clamp(y > 6400 ? ((y - 9920) / 8) + 1 : ((y - 3520) / 8) + 1, 0, 99);

        switch (client.getWorld())
        {
            case 337:
            case 371:
            case 417:
            case 392:
            case 324:
                wildernessLevel += 15;
            default: break;
        }

        return Math.max(0, wildernessLevel);
    }

    public static int clamp(int val, int min, int max)
    {
        return Math.max(min, Math.min(max, val));
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (getWildernessLevelFrom(localPlayer.getWorldLocation()) <= 0)
        {
            shouldRemind = false;
            return;
        }
        else
            shouldRemind = true;
    }

}
