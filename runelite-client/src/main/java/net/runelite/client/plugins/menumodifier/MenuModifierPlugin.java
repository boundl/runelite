package net.runelite.client.plugins.menumodifier;

import net.runelite.client.eventbus.Subscribe;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.MiscUtils;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.Arrays;

@PluginDescriptor(
        name = "!Menu Modifier Plugin",
        description = "Changes right click menu for players",
        tags = { "menu", "modifier", "right", "click", "pk", "bogla" }
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
    
    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded menuEntryAdded)
    {
        if (!inWilderness)
            return;

        if (hotKeyPressed)
            return;
    
        String option = Text.removeTags(menuEntryAdded.getOption()).toLowerCase();

        if ((option.contains("trade with") && config.hideTradeWith())
            || (option.contains("lookup") && config.hideLookup())
            || (option.contains("report") && config.hideReport())
            || (option.contains("examine") && config.hideExamine())
            || (option.contains("cancel") && config.hideCancel()))
        {
            int identifier = menuEntryAdded.getIdentifier();

            Player[] players = client.getCachedPlayers();
            Player player = null;

            if (identifier >= 0 && identifier < players.length)
                player = players[identifier];

            if (player == null)
                return;

            //allow trading with friends/clanmates
            if (option.contains("trade with") && (player.isFriend() || player.isClanMember()))
                return;

            MenuEntry[] menuEntries = client.getMenuEntries();

            if (menuEntries.length > 0)
                client.setMenuEntries(Arrays.copyOf(menuEntries, menuEntries.length - 1));
        }
    }
    
    @Subscribe
    public void onGameTick(GameTick event)
    {
        Player localPlayer = client.getLocalPlayer();
        
        if (localPlayer == null)
            return;
        
        inWilderness = MiscUtils.getWildernessLevelFrom(client, localPlayer.getWorldLocation()) > 0;
    }
}
