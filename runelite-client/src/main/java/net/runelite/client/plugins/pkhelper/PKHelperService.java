package net.runelite.client.plugins.pkhelper;

import java.awt.Color;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

@Singleton
public class PKHelperService
{
    private final Client client;
    private final PKHelperConfig config;

    @Inject
    private PKHelperService(Client client, PKHelperConfig config)
    {
        this.config = config;
        this.client = client;
    }

    int getWildernessLevelFrom(WorldPoint point)
    {
        int y = point.getY();               //v underground           //v above ground
        int wildernessLevel = y > 6400 ? ((y - 9920) / 8) + 1 : ((y - 3520) / 8) + 1;
        return wildernessLevel > 0 ? wildernessLevel : 15; //if wildy level is below zero we assume it's a pvp world which is -15 to 15 level difference
    }

    public static int clamp(int val, int min, int max)
    {
        return Math.max(min, Math.min(max, val));
    }

    public void forEachPlayer(final BiConsumer<Player, Color> consumer)
    {
        if (!config.highlightOwnPlayer() && !config.highlightFriends())
            return;

        final Player localPlayer = client.getLocalPlayer();

        for (Player player : client.getPlayers())
        {
            if (player == null || player.getName() == null)
                continue;

            int lvlDelta =  player.getCombatLevel() - localPlayer.getCombatLevel();
            int wildyLvl = getWildernessLevelFrom(player.getWorldLocation());

            if (Math.abs(lvlDelta) > wildyLvl)
                continue;

            if (player == localPlayer && config.highlightOwnPlayer())
            {
                consumer.accept(player, config.getOwnPlayerColor());
            }
            else if (config.highlightFriends() && player.isFriend())
            {
                consumer.accept(player, config.getFriendColor());
            }
            else if (player != localPlayer && !player.isFriend())
            {
                int R = clamp((int)(((float)(lvlDelta + wildyLvl) / (float)(wildyLvl * 2)) * 255.f), 0, 255);
                int G = clamp(255 - R, 0, 255);

                consumer.accept(player, Color.getHSBColor(Color.RGBtoHSB(R, G, 0, null)[0], 1.f, 1.f));
            }
        }
    }
}
