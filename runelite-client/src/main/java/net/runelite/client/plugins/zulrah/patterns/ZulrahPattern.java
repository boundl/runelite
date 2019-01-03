package net.runelite.client.plugins.zulrah.patterns;

import java.util.ArrayList;
import java.util.List;
import net.runelite.api.Prayer;
import net.runelite.client.plugins.zulrah.phase.StandLocation;
import net.runelite.client.plugins.zulrah.phase.ZulrahLocation;
import net.runelite.client.plugins.zulrah.phase.ZulrahPhase;
import net.runelite.client.plugins.zulrah.phase.ZulrahType;

public abstract class ZulrahPattern
{
    private final List<ZulrahPhase> pattern = new ArrayList<>();

    protected final void add(ZulrahLocation loc, ZulrahType type, StandLocation standLocation, Prayer prayer)
    {
        add(loc, type, standLocation, false, prayer);
    }

    protected final void addJad(ZulrahLocation loc, ZulrahType type, StandLocation standLocation, Prayer prayer)
    {
        add(loc, type, standLocation, true, prayer);
    }

    private void add(ZulrahLocation loc, ZulrahType type, StandLocation standLocation, boolean jad, Prayer prayer)
    {
        pattern.add(new ZulrahPhase(loc, type, jad, standLocation, prayer));
    }

    public ZulrahPhase get(int index)
    {
        if (index >= pattern.size())
        {
            return null;
        }

        return pattern.get(index);
    }

    public boolean stageMatches(int index, ZulrahPhase instance)
    {
        ZulrahPhase patternInstance = get(index);
        return patternInstance != null && patternInstance.equals(instance);
    }

    public boolean canReset(int index)
    {
        return index >= pattern.size();
    }
}