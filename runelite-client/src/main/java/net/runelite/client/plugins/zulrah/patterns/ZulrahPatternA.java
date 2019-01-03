package net.runelite.client.plugins.zulrah.patterns;

import net.runelite.api.Prayer;
import net.runelite.client.plugins.zulrah.phase.StandLocation;
import net.runelite.client.plugins.zulrah.phase.ZulrahLocation;
import net.runelite.client.plugins.zulrah.phase.ZulrahType;

public class ZulrahPatternA extends ZulrahPattern
{
    public ZulrahPatternA()
    {
        add(ZulrahLocation.NORTH, ZulrahType.RANGE, StandLocation.TOP_EAST, null);
        add(ZulrahLocation.NORTH, ZulrahType.MELEE, StandLocation.TOP_EAST, null);
        add(ZulrahLocation.NORTH, ZulrahType.MAGIC, StandLocation.TOP_EAST, Prayer.PROTECT_FROM_MAGIC);
        add(ZulrahLocation.SOUTH, ZulrahType.RANGE, StandLocation.PILLAR_WEST_INSIDE, Prayer.PROTECT_FROM_MISSILES);
        add(ZulrahLocation.NORTH, ZulrahType.MELEE, StandLocation.PILLAR_WEST_INSIDE, null);
        add(ZulrahLocation.WEST, ZulrahType.MAGIC, StandLocation.PILLAR_WEST_INSIDE, Prayer.PROTECT_FROM_MAGIC);
        add(ZulrahLocation.SOUTH, ZulrahType.RANGE, StandLocation.PILLAR_EAST_OUTSIDE, null);
        add(ZulrahLocation.SOUTH, ZulrahType.MAGIC, StandLocation.PILLAR_EAST_OUTSIDE, Prayer.PROTECT_FROM_MAGIC);
        addJad(ZulrahLocation.WEST, ZulrahType.RANGE, StandLocation.TOP_WEST, Prayer.PROTECT_FROM_MISSILES);
        add(ZulrahLocation.NORTH, ZulrahType.MELEE, StandLocation.TOP_EAST, null);
    }

    @Override
    public String toString()
    {
        return "Pattern A";
    }
}