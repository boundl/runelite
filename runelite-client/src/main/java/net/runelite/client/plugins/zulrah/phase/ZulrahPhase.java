package net.runelite.client.plugins.zulrah.phase;

import net.runelite.api.NPC;
import net.runelite.api.Prayer;
import net.runelite.api.coords.WorldPoint;
import java.awt.Color;

public class ZulrahPhase
{
    private static final Color RANGE_COLOR = new Color(0, 255, 0, 150);
    private static final Color MAGIC_COLOR = new Color(0, 0, 255, 150);
    private static final Color MELEE_COLOR = new Color(255, 0, 0, 150);
    private static final Color JAD_COLOR = new Color(255, 255, 0, 150);

    private final ZulrahLocation zulrahLocation;
    private final ZulrahType type;
    private final boolean jad;
    private final StandLocation standLocation;
    private final Prayer prayer;

    public ZulrahPhase(ZulrahLocation zulrahLocation, ZulrahType type, boolean jad, StandLocation standLocation, Prayer prayer)
    {
        this.zulrahLocation = zulrahLocation;
        this.type = type;
        this.jad = jad;
        this.standLocation = standLocation;
        this.prayer = prayer;
    }

    public static ZulrahPhase valueOf(NPC zulrah, WorldPoint start)
    {
        ZulrahLocation zulrahLocation = ZulrahLocation.valueOf(start, zulrah.getWorldLocation());
        ZulrahType zulrahType = ZulrahType.valueOf(zulrah.getId());

        if (zulrahLocation == null || zulrahType == null)
            return null;

        StandLocation standLocation = zulrahType == ZulrahType.MAGIC ? StandLocation.PILLAR_WEST_OUTSIDE : StandLocation.TOP_EAST;
        Prayer prayer = zulrahType == ZulrahType.MAGIC ? Prayer.PROTECT_FROM_MAGIC : null;

        return new ZulrahPhase(zulrahLocation, zulrahType, false, standLocation, prayer);
    }

    // world location
    public WorldPoint getZulrahTile(WorldPoint startTile)
    {
        // NORTH doesn't need changing because it is the start
        switch (zulrahLocation)
        {
            case SOUTH:
                return new WorldPoint(startTile.getX(), startTile.getY() - 11, 0);
            case EAST:
                return new WorldPoint(startTile.getX() + 10, startTile.getY() - 2, 0);
            case WEST:
                return new WorldPoint(startTile.getX() - 10, startTile.getY() - 2, 0);
        }
        return startTile;
    }

    // world location
    public WorldPoint getStandTile(WorldPoint startTile)
    {
        switch (standLocation)
        {
            case WEST:
                return new WorldPoint(startTile.getX() - 5, startTile.getY(), 0);
            case EAST:
                return new WorldPoint(startTile.getX() + 5, startTile.getY() - 2, 0);
            case SOUTH:
                return new WorldPoint(startTile.getX(), startTile.getY() - 6, 0);
            case SOUTH_WEST:
                return new WorldPoint(startTile.getX() - 4, startTile.getY() - 4, 0);
            case SOUTH_EAST:
                return new WorldPoint(startTile.getX() + 2, startTile.getY() - 6, 0);
            case TOP_EAST:
                return new WorldPoint(startTile.getX() + 6, startTile.getY() + 2, 0);
            case TOP_WEST:
                return new WorldPoint(startTile.getX() - 4, startTile.getY() + 3, 0);
            case PILLAR_WEST_INSIDE:
                return new WorldPoint(startTile.getX() - 4, startTile.getY() - 3, 0);
            case PILLAR_WEST_OUTSIDE:
                return new WorldPoint(startTile.getX() - 5, startTile.getY() - 3, 0);
            case PILLAR_EAST_INSIDE:
                return new WorldPoint(startTile.getX() + 4, startTile.getY() - 3, 0);
            case PILLAR_EAST_OUTSIDE:
                return new WorldPoint(startTile.getX() + 4, startTile.getY() - 4, 0);
        }
        return startTile;
    }

    public ZulrahLocation getZulrahLocation()
    {
        return zulrahLocation;
    }

    public ZulrahType getType()
    {
        return type;
    }

    public boolean isJad()
    {
        return jad;
    }

    public StandLocation getStandLocation()
    {
        return standLocation;
    }

    public Prayer getPrayer()
    {
        return prayer;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        ZulrahPhase other = (ZulrahPhase) obj;
        return this.jad == other.jad && this.zulrahLocation == other.zulrahLocation && this.type == other.type;
    }

    public Color getColor()
    {
        if (jad)
        {
            return JAD_COLOR;
        }
        else
        {
            switch (type)
            {
                case RANGE:
                    return RANGE_COLOR;
                case MAGIC:
                    return MAGIC_COLOR;
                case MELEE:
                    return MELEE_COLOR;
            }
        }
        return RANGE_COLOR;
    }

    public Color getColor(int alpha)
    {
        Color col = getColor();

        return new Color(col.getRed(), col.getGreen(), col.getBlue(), alpha);
    }
}