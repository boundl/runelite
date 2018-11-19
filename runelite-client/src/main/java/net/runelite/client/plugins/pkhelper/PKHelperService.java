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

    public void forEachPlayer(final BiConsumer<Player, Color> consumer)
    {
        final Player localPlayer = client.getLocalPlayer();

        for (Player player : client.getPlayers())
        {
            if (player == null || player.getName() == null)
                continue;

            int lvlDelta =  player.getCombatLevel() - localPlayer.getCombatLevel();
            int wildyLvl = getWildernessLevelFrom(player.getWorldLocation());

            if (wildyLvl <= 0)
                continue;

            if (Math.abs(lvlDelta) > wildyLvl)
                continue;

            if (player == localPlayer && config.highlightOwnPlayer())
            {
                consumer.accept(player, config.getOwnPlayerColor());
            }
            else if (config.highlightFriends() && (player.isFriend() || player.isClanMember()))
            {
                consumer.accept(player, config.getFriendColor());
            }
            else if (player != localPlayer && !player.isFriend() && !player.isClanMember())
            {
                int R = clamp((int)(((float)(lvlDelta + wildyLvl) / (float)(wildyLvl * 2)) * 255.f), 0, 255);
                int G = clamp(255 - R, 0, 255);

                consumer.accept(player, Color.getHSBColor(Color.RGBtoHSB(R, G, 0, null)[0], 1.f, 1.f));
            }
        }
    }
}
