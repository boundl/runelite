/*
 * Copyright (c) 2018, DennisDeV <https://github.com/DevDennis>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.antidrag3;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Anti Drag",
        description = "Prevent dragging an item for a specified delay",
        tags = {"antidrag", "delay", "inventory", "items"}
)
public class AntiDrag3Plugin extends Plugin implements KeyListener
{
    private static final int DEFAULT_DELAY = 5;

    @Inject
    private Client client;

    @Inject
    private AntiDrag3Config config;

    @Inject
    private KeyManager keyManager;

    @Provides
    AntiDrag3Config getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(AntiDrag3Config.class);
    }

    private int getWildernessLevelFrom(WorldPoint point)
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

    private static int clamp(int val, int min, int max)
    {
        return Math.max(min, Math.min(max, val));
    }

    private boolean inWilderness()
    {
        Player localPlayer = client.getLocalPlayer();

        if (localPlayer == null)
            return false;

        return getWildernessLevelFrom(localPlayer.getWorldLocation()) > 0;
    }

    private boolean isInWilderness = false;

    @Override
    protected void startUp() throws Exception
    {
        keyManager.registerKeyListener(this);

        if (inWilderness())
            client.setInventoryDragDelay(config.dragDelay());
        else
            client.setInventoryDragDelay(DEFAULT_DELAY);
    }

    @Override
    protected void shutDown() throws Exception
    {
        client.setInventoryDragDelay(DEFAULT_DELAY);
        keyManager.unregisterKeyListener(this);
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        boolean inWild = inWilderness();

        if (inWild != isInWilderness)
        {
            if (inWilderness())
                client.setInventoryDragDelay(config.dragDelay());
            else
                client.setInventoryDragDelay(DEFAULT_DELAY);
        }

        isInWilderness = inWild;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (!inWilderness())
            return;

        if (e.getKeyCode() == KeyEvent.VK_SHIFT)
        {
            client.setInventoryDragDelay(DEFAULT_DELAY);
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (!inWilderness())
            return;

        if (e.getKeyCode() == KeyEvent.VK_SHIFT)
        {
            client.setInventoryDragDelay(config.dragDelay());
        }
    }

    @Subscribe
    public void onFocusChanged(FocusChanged focusChanged)
    {
        if (!inWilderness())
            return;

        if (!focusChanged.isFocused())
        {
            client.setInventoryDragDelay(config.dragDelay());
        }
    }
}
