package net.runelite.client.plugins.spellbookfixer;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;


@PluginDescriptor(
        name = "!Spellbook Fixer",
        description = "Resize and filter spellbook for PKing",
        tags = {"resize", "spellbook", "magic", "spell", "pk", "book", "filter", "bogla"}
)
@Slf4j
public class SpellbookFixerPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    SpellbookFixerConfig config;

    @Provides
    SpellbookFixerConfig provideConfig(ConfigManager configManager) { return configManager.getConfig(SpellbookFixerConfig.class); }

    @Override
    protected void startUp() throws Exception { }

    @Override
    protected void shutDown() throws Exception { }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event)
    {
        if (event.getGroupId() == WidgetID.SPELLBOOK_GROUP_ID)
        {
            //log.debug("do something....");
        }
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
            return;

        modifyIceBarrage();
        modifyIceBlitz();
        modifyBloodBlitz();
    }

    private void modifyIceBarrage()
    {
        if (!config.shouldModifyIceBarrage())
            return;

        Widget widget_ice_barrage = client.getWidget(WidgetInfo.SPELL_ICE_BARRAGE);

        if (widget_ice_barrage == null)
            return;

        widget_ice_barrage.setOriginalX(config.getBarragePositionX());
        widget_ice_barrage.setOriginalY(config.getBarragePositionY());
        widget_ice_barrage.setOriginalWidth(config.getBarrageSize());
        widget_ice_barrage.setOriginalHeight(config.getBarrageSize());
        widget_ice_barrage.revalidate();
    }

    private void modifyIceBlitz()
    {
        if (!config.shouldModifyIceBlitz())
            return;

        Widget widget_ice_blitz = client.getWidget(WidgetInfo.SPELL_ICE_BLITZ);

        if (widget_ice_blitz == null)
            return;

        widget_ice_blitz.setOriginalX(config.getBlitzPositionX());
        widget_ice_blitz.setOriginalY(config.getBlitzPositionY());
        widget_ice_blitz.setOriginalWidth(config.getBlitzSize());
        widget_ice_blitz.setOriginalHeight(config.getBlitzSize());
        widget_ice_blitz.revalidate();
    }

    private void modifyBloodBlitz()
    {
        if (!config.shouldHideBloodBlitz())
            return;

        Widget widget_blood_blitz = client.getWidget(WidgetInfo.SPELL_BLOOD_BLITZ);

        if (widget_blood_blitz == null)
            return;

        widget_blood_blitz.setHidden(true);
    }

}
