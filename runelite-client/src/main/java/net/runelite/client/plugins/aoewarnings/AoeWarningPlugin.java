package net.runelite.client.plugins.aoewarnings;

import com.google.inject.Binder;
import com.google.inject.Provides;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "AoE warnings"
)
public class AoeWarningPlugin extends Plugin
{
	@Inject
	AoeWarningOverlay overlay;

	@Inject
	AoeWarningConfig config;

	@Inject
	private OverlayManager overlayManager;

	private final Map<Projectile, AoeProjectile> projectiles = new HashMap<>();

	@Override
	public void configure(Binder binder)
	{
		binder.bind(AoeWarningOverlay.class);
	}

	@Provides
	AoeWarningConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AoeWarningConfig.class);
	}

	public Overlay getOverlay()
	{
		return overlay;
	}

	Map<Projectile, AoeProjectile> getProjectiles()
	{
		return projectiles;
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	/**
	 * Called when a projectile is set to move towards a point. For
	 * projectiles that target the ground, like AoE projectiles from
	 * Lizardman Shamans, this is only called once
	 *
	 * @param event Projectile moved event
	 */
	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		Projectile projectile = event.getProjectile();

		// AoE projectiles do not target anything
		if (projectile.getInteracting() != null)
		{
			return;
		}

		int projectileId = projectile.getId();
		AoeProjectileInfo aoeProjectileInfo = AoeProjectileInfo.getById(projectileId);
		if (aoeProjectileInfo != null && isConfigEnabledForProjectileId(projectileId))
		{
			LocalPoint targetPoint = event.getPosition();
			AoeProjectile aoeProjectile = new AoeProjectile(Instant.now(), targetPoint, aoeProjectileInfo);
			projectiles.put(projectile, aoeProjectile);
		}
	}

	private boolean isConfigEnabledForProjectileId(int projectileId)
	{
		AoeProjectileInfo projectileInfo = AoeProjectileInfo.getById(projectileId);
		if (projectileInfo == null)
		{
			return false;
		}

		switch (projectileInfo)
		{
			case LIZARDMAN_SHAMAN_AOE:
				return config.isShamansEnabled();
			case CRAZY_ARCHAEOLOGIST_AOE:
				return config.isArchaeologistEnabled();
			case ICE_DEMON_RANGED_AOE:
			case ICE_DEMON_ICE_BARRAGE_AOE:
				return config.isIceDemonEnabled();
			case VASA_AWAKEN_AOE:
			case VASA_RANGED_AOE:
				return config.isVasaEnabled();
			case TEKTON_METEOR_AOE:
				return config.isTektonEnabled();
			case VORKATH_BOMB:
			case VORKATH_POISON_POOL:
			case VORKATH_SPAWN:
			case VORKATH_TICK_FIRE:
				return config.isVorkathEnabled();
			case VETION_LIGHTNING:
				return config.isVetionEnabled();
			case CHAOS_FANATIC:
				return config.isChaosFanaticEnabled();
			case GALVEK_BOMB:
			case GALVEK_MINE:
				return config.isGalvekEnabled();
			case OLM_FALLING_CRYSTAL:
			case OLM_BURNING:
				return config.isOlmEnabled();
			case CORPOREAL_BEAST:
			case CORPOREAL_BEAST_DARK_CORE:
				return config.isCorpEnabled();
			case WINTERTODT_SNOW_FALL:
				return config.isWintertodtEnabled();
		}

		return false;
	}
}
