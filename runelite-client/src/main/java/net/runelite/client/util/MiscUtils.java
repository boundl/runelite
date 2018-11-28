package net.runelite.client.util;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

public class MiscUtils
{
    public static int getWildernessLevelFrom(Client client, WorldPoint point)
    {
        int y = point.getY();               //v underground           //v above ground

        int wildernessLevel = clamp(y > 6400 ? ((y - 9920) / 8) + 1 : ((y - 3520) / 8) + 1, 0, 99);

        switch (client.getWorld())
        {
            case 417:
            case 392:
            case 345:
            case 343:
            case 324:
                wildernessLevel += 15;
            default:
                break;
        }

        return Math.max(0, wildernessLevel);
    }

    public static int clamp(int val, int min, int max)
    {
        return Math.max(min, Math.min(max, val));
    }

    public static boolean inWilderness(Client client)
    {
        Player localPlayer = client.getLocalPlayer();

        if (localPlayer == null)
            return false;

        return getWildernessLevelFrom(client, localPlayer.getWorldLocation()) > 0;
    }
}
