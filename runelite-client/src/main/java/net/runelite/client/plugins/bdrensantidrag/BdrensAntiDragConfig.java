package net.runelite.client.plugins.bdrensantidrag;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("antiDrag3")
public interface BdrensAntiDragConfig extends Config
{
    @ConfigItem(
            keyName = "dragDelay",
            name = "Drag Delay",
            description = "Configures the inventory drag delay in client ticks (20ms)",
            position = 1
    )
    default int dragDelay()
    {
        return 600 / 20; // one game tick
    }
}

