package net.runelite.client.plugins.zulrah;

import com.google.inject.Binder;
import com.google.inject.Provides;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.Query;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.NPCQuery;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.zulrah.overlays.ZulrahInfoOverlay;
import net.runelite.client.plugins.zulrah.overlays.ZulrahOverlay;
import net.runelite.client.plugins.zulrah.patterns.ZulrahPattern;
import net.runelite.client.plugins.zulrah.patterns.ZulrahPatternA;
import net.runelite.client.plugins.zulrah.patterns.ZulrahPatternB;
import net.runelite.client.plugins.zulrah.patterns.ZulrahPatternC;
import net.runelite.client.plugins.zulrah.patterns.ZulrahPatternD;
import net.runelite.client.plugins.zulrah.phase.ZulrahPhase;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.QueryRunner;

@PluginDescriptor(
        name = "!Zulrah plugin"
)
public class ZulrahPlugin extends Plugin
{
    @Inject
    private OverlayManager overlayManager;

    @Inject
    QueryRunner queryRunner;

    @Inject
    Client client;

    @Inject
    ZulrahConfig config;

    @Inject
    ZulrahOverlay overlay;

    @Inject
    ZulrahInfoOverlay infoOverlay;

    private final ZulrahPattern[] patterns = new ZulrahPattern[]
            {
                    new ZulrahPatternA(),
                    new ZulrahPatternB(),
                    new ZulrahPatternC(),
                    new ZulrahPatternD()
            };

    private ZulrahInstance instance;

    @Override
    public void configure(Binder binder)
    {
        binder.bind(ZulrahOverlay.class);
    }

    @Provides
    ZulrahConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ZulrahConfig.class);
    }

    public Collection<Overlay> getOverlays()
    {
        return Arrays.asList(overlay, infoOverlay);
    }

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
        overlayManager.add(infoOverlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        overlayManager.remove(infoOverlay);
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (!config.enabled() || client.getGameState() != GameState.LOGGED_IN)
            return;

        NPC zulrah = findZulrah();
        if (zulrah == null)
        {
            if (instance != null)
                instance = null;

            return;
        }

        if (instance == null)
            instance = new ZulrahInstance(zulrah);

        ZulrahPhase currentPhase = ZulrahPhase.valueOf(zulrah, instance.getStartLocation());

        if (instance.getPhase() == null)
        {
            instance.setPhase(currentPhase);
        }
        else if (!instance.getPhase().equals(currentPhase))
        {
            ZulrahPhase previousPhase = instance.getPhase();
            instance.setPhase(currentPhase);
            instance.nextStage();
        }

        ZulrahPattern pattern = instance.getPattern();
        if (pattern == null)
        {
            int potential = 0;
            ZulrahPattern potentialPattern = null;

            for (ZulrahPattern p : patterns)
            {
                if (p.stageMatches(instance.getStage(), instance.getPhase()))
                {
                    potential++;
                    potentialPattern = p;
                }
            }

            if (potential == 1)
            {
                instance.setPattern(potentialPattern);
            }
        }
        else if (pattern.canReset(instance.getStage()) && (instance.getPhase() == null || instance.getPhase().equals(pattern.get(0))))
        {
            instance.reset();
        }
    }

    private NPC findZulrah()
    {
        Query query = new NPCQuery().nameEquals("Zulrah");
        NPC[] result = queryRunner.runQuery(query);
        return result.length == 1 ? result[0] : null;
    }

    public ZulrahInstance getInstance()
    {
        return instance;
    }
}