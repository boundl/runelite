package net.runelite.client.plugins.zulrah.phase;

import net.runelite.api.coords.WorldPoint;

public enum ZulrahLocation
{
    NORTH, SOUTH, EAST, WEST;

    public static ZulrahLocation valueOf(WorldPoint start, WorldPoint current)
    {
        int dx = start.getX() - current.getX();
        int dy = start.getY() - current.getY();

        if (dx == -10 && dy == 2)
            return ZulrahLocation.EAST;
        else if (dx == 10 && dy == 2)
            return ZulrahLocation.WEST;
        else if (dx == 0 && dy == 11)
            return ZulrahLocation.SOUTH;
        else if (dx == 0 && dy == 0)
            return ZulrahLocation.NORTH;
        else
            return null;
    }
}