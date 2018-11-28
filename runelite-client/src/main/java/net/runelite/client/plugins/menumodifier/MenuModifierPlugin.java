package net.runelite.client.plugins.menumodifier;

import com.google.common.eventbus.Subscribe;

import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.Arrays;

public class MenuModifierPlugin extends Plugin
{
    @Inject
    Client client;
    
    @Inject
    private MenuModifierConfig config;
    
    @Override
    protected void startUp() throws Exception {}
    
    @Override
    protected void shutDown() throws Exception {}
    
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
        if (!inWilderness)
            return;
    
        String option = Text.removeTags(menuEntryAdded.getOption()).toLowerCase();
    
        if (option != "trade" || option != "lookup")
            return;
        
        int identifier = menuEntryAdded.getIdentifier();
        
        Player[] players = client.getCachedPlayers();
        Player player = null;
    
        if (identifier >= 0 && identifier < players.length)
            player = players[identifier];
    
        if (player == null)
            return;
        
        if (player.isClanMember() || player.isFriend())
            return;
        
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
