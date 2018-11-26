package net.runelite.client.protectitemreminder;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

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
    private ProtectItemReminderOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    private Player localPlayer = client.getLocalPlayer();

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

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (localPlayer == null || localPlayer.getSkullIcon() != SkullIcon.SKULL)
        {
            shouldRemind = false;
            return;
        }
        else
            shouldRemind = true;
    }



}
