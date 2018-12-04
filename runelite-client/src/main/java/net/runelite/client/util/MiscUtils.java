package net.runelite.client.util;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.coords.WorldPoint;

public class MiscUtils
{
    public static int getWildernessLevelFrom(Client client, WorldPoint point)
    {
        if (client == null)
            return 0;

        if (point == null)
            return 0;

        int y = point.getY();
                                                //v underground        //v above ground
        int wildernessLevel = y > 6400 ? ((y - 9920) / 8) + 1 : ((y - 3520) / 8) + 1;
        
        if (wildernessLevel > 56 || wildernessLevel < 0)
            wildernessLevel = 0;
        
        if (client.getWorldType().stream().anyMatch(x -> x == WorldType.PVP || x == WorldType.PVP_HIGH_RISK))
        {
            wildernessLevel += 15;
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
