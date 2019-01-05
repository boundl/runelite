package net.runelite.client.plugins.spellbookfixer;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOpened;
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
    public void onGameTick(GameTick event)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
            return;

        if (config.shouldModifyIceBarrage())
            modifySpell(WidgetInfo.SPELL_ICE_BARRAGE, config.getBarragePositionX(), config.getBarragePositionY(), config.getBarrageSize());

        if (config.shouldModifyIceBlitz())
            modifySpell(WidgetInfo.SPELL_ICE_BLITZ, config.getBlitzPositionX(), config.getBlitzPositionY(), config.getBlitzSize());

        if (config.shouldModifyVengeance())
            modifySpell(WidgetInfo.SPELL_VENGEANCE, config.getVengeancePositionX(), config.getVengeancePositionY(), config.getVengeanceSize());

        if (config.shouldModifyTeleBlock())
            modifySpell(WidgetInfo.SPELL_TELE_BLOCK, config.getTeleBlockPositionX(), config.getTeleBlockPositionY(), config.getTeleBlockSize());

        if (config.shouldModifyEntangle())
            modifySpell(WidgetInfo.SPELL_ENTANGLE, config.getEntanglePositionX(), config.getEntanglePositionY(), config.getEntangleSize());

        setSpellHidden(WidgetInfo.SPELL_BLOOD_BLITZ, config.shouldHideOthers());
        setSpellHidden(WidgetInfo.SPELL_VENGEANCE_OTHER, config.shouldHideOthers());
        setSpellHidden(WidgetInfo.SPELL_BIND, config.shouldHideOthers());
        setSpellHidden(WidgetInfo.SPELL_SNARE, config.shouldHideOthers());
    }

    private void modifySpell(WidgetInfo widgetInfo, int posX, int posY, int size)
    {
        Widget widget = client.getWidget(widgetInfo);

        if (widget == null)
            return;

        widget.setOriginalX(posX);
        widget.setOriginalY(posY);
        widget.setOriginalWidth(size);
        widget.setOriginalHeight(size);
        widget.revalidate();
    }

    private void setSpellHidden(WidgetInfo widgetInfo, boolean hidden)
    {
        Widget widget = client.getWidget(widgetInfo);

        if (widget == null)
            return;

        widget.setHidden(hidden);
    }

}
