package net.runelite.client.plugins.bdrensantidrag;

import java.awt.event.InputEvent;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

import java.awt.event.KeyEvent;

@ConfigGroup("antiDrag3")
public interface BdrensAntiDragConfig extends Config
{
    @ConfigItem(keyName = "dragDelay", name = "Drag Delay", description = "Configures the inventory drag delay in client ticks (20ms)", position = 1)
    default int dragDelay() { return 600 / 20; } // one game tick
    
    @ConfigItem(keyName = "disableKey", name = "Disable hotkey", description = "Disables anti-drag when held", position = 2)
    default Keybind disableKey() { return new Keybind(KeyEvent.VK_ALT, InputEvent.ALT_DOWN_MASK); }
}

