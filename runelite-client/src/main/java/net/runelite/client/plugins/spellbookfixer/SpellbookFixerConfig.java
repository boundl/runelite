package net.runelite.client.plugins.spellbookfixer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("spellbookfixer")
public interface SpellbookFixerConfig extends Config
{
    @ConfigItem(position = 0, keyName = "shouldHideBloodBlitz", name = "Hide Blood Blitz", description = "Toggle on to hide blood blitz (can't be filtered otherwise).")
    default boolean shouldHideBloodBlitz()
    {
        return false;
    }

    //ice blitz
    @ConfigItem(position = 1, keyName = "shouldModifyIceBlitz", name = "Ice Blitz", description = "Toggle on to enable ice blitz modifications.")
    default boolean shouldModifyIceBlitz() { return false; }
    @ConfigItem(position = 2, keyName = "getBlitzPositionX", name = "Ice Blitz Pos X", description = "Modifies the X-axis position of ice blitz.")
    default int getBlitzPositionX()
    {
        return 80;
    }
    @ConfigItem(position = 3, keyName = "getBlitzPositionY", name = "Ice Blitz Pos Y", description = "Modifies the Y-axis position of ice blitz.")
    default int getBlitzPositionY()
    {
        return 80;
    }
    @ConfigItem(position = 4, keyName = "getBlitzSize", name = "Ice Blitz Size", description = "Modifies the width of ice blitz.")
    default int getBlitzSize()
    {
        return 64;
    }

    //ice barrage
    @ConfigItem(position = 5, keyName = "shouldModifyIceBarrage", name = "Ice Barrage", description = "Toggle on to enable ice Barrage modifications.")
    default boolean shouldModifyIceBarrage() { return false; }
    @ConfigItem(position = 6, keyName = "getBarragePositionX", name = "Ice Barrage Pos X", description = "Modifies the X-axis position of ice barrage.")
    default int getBarragePositionX()
    {
        return 0;
    }
    @ConfigItem(position = 7, keyName = "getBarragePositionY", name = "Ice Barrage Pos X", description = "Modifies the X-axis position of ice barrage.")
    default int getBarragePositionY()
    {
        return 0;
    }
    @ConfigItem(position = 8, keyName = "getBarrageSize", name = "Ice Barrage Size", description = "Modifies the width position of ice barrage.")
    default int getBarrageSize()
    {
        return 80;
    }
}
