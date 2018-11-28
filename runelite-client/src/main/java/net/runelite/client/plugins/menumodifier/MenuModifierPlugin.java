package net.runelite.client.plugins.menumodifier;

import com.google.common.eventbus.Subscribe;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.Arrays;

@PluginDescriptor(
        name = "Menu Modifier Plugin",
        description = "Changes right click menu for players",
        tags = { "menu", "modifier", "right", "click", "pk" }
)
public class MenuModifierPlugin extends Plugin
{
    @Inject
    private Client client;
    
    @Inject
    private MenuModifierConfig config;

    @Inject
    private MenuModifierInputListener inputListener;

    @Inject
    private KeyManager keyManager;

    @Provides
    MenuModifierConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(MenuModifierConfig.class);
    }
    
    @Override
    protected void startUp() throws Exception
    {
        keyManager.registerKeyListener(inputListener);
    }
    
    @Override
    protected void shutDown() throws Exception
    {
        keyManager.unregisterKeyListener(inputListener);
    }

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private boolean hotKeyPressed;
    
    private boolean inWilderness = false;
    
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
    public void onMenuEntryAdded(MenuEntryAdded menuEntryAdded)
    {
        /*if (!inWilderness)
            return;*/
    
        String option = Text.removeTags(menuEntryAdded.getOption()).toLowerCase();

        if (!option.contains("trade with") || !config.hideTradeWith())
            return;

        if (!option.contains("lookup") || !config.hideLookup())
            return;

        if (!option.contains("report") || !config.hideReport())
            return;

        if (!option.contains("examine") || !config.hideExamine())
            return;

        if (!option.contains("cancel") || !config.hideCancel())
            return;
        
        int identifier = menuEntryAdded.getIdentifier();
        
        Player[] players = client.getCachedPlayers();
        Player player = null;
    
        if (identifier >= 0 && identifier < players.length)
            player = players[identifier];
    
        if (player == null)
            return;

        if (option.contains("attack"))
        {
            if (player.isFriend() || (player.isClanMember() && config.hideClanmateIsFriendly()))
            {
                if (!config.hideAttackFriendly())
                    return;
            }
            else
                return;
        }
        
        MenuEntry[] menuEntries = client.getMenuEntries();
        if (menuEntries.length > 0)
            client.setMenuEntries(Arrays.copyOf(menuEntries, menuEntries.length - 1));
    }
    
    @Subscribe
    public void onGameTick(GameTick event)
    {
        Player localPlayer = client.getLocalPlayer();
        
        if (localPlayer == null)
            return;
        
        inWilderness = getWildernessLevelFrom(localPlayer.getWorldLocation()) > 0;
    }
}
