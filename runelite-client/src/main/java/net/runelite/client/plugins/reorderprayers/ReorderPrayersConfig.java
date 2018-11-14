/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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
package net.runelite.client.plugins.reorderprayers;

import net.runelite.api.Prayer;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(ReorderPrayersPlugin.CONFIG_GROUP_KEY)
public interface ReorderPrayersConfig extends Config
{

	@ConfigItem(
		keyName = ReorderPrayersPlugin.CONFIG_UNLOCK_REORDERING_KEY,
		name = "Unlock Prayer Reordering",
		description = "Configures whether or not you can reorder the prayers",
		position = 1
	)
	default boolean unlockPrayerReordering()
	{
		return false;
	}

	@ConfigItem(
		keyName = ReorderPrayersPlugin.CONFIG_UNLOCK_REORDERING_KEY,
		name = "",
		description = ""
	)
	void unlockPrayerReordering(boolean unlock);

	@ConfigItem(
		keyName = ReorderPrayersPlugin.CONFIG_PRAYER_ORDER_KEY,
		name = "Prayer Order",
		description = "Configures the order of the prayers",
		hidden = true,
		position = 2
	)
	default String prayerOrder()
	{
		return ReorderPrayersPlugin.prayerOrderToString(Prayer.values());
	}

	@ConfigItem(
		keyName = ReorderPrayersPlugin.CONFIG_PRAYER_ORDER_KEY,
		name = "",
		description = ""
	)
	void prayerOrder(String prayerOrder);

	@ConfigItem(
			position = 2,
			keyName = "hideThickSkin",
			name = "Hide Thick Skin",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideThickSkin() { return false; }

	@ConfigItem(
			position = 3,
			keyName = "hideBurstOfStrength",
			name = "Hide Burst of Strength",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideBurstOfStrength() { return false; }

	@ConfigItem(
			position = 4,
			keyName = "hideClarityOfThought",
			name = "Hides Clarity of Thought",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideClarityOfThought() { return false; }

	@ConfigItem(
			position = 5,
			keyName = "hideSharpEye",
			name = "Hide Sharp Eye",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideSharpEye() { return false; }

	@ConfigItem(
			position = 6,
			keyName = "hideMysticWill",
			name = "Hide Mystic Will",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideMysticWill() { return false; }

	@ConfigItem(
			position = 7,
			keyName = "hideRockSkin",
			name = "Hide Rock Skin",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideRockSkin() { return false; }

	@ConfigItem(
			position = 8,
			keyName = "hideSuperhumanStrength",
			name = "Hide Superhuman Strength",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideSuperhumanStrength() { return false; }

	@ConfigItem(
			position = 9,
			keyName = "hideImprovedReflexes",
			name = "Hide Improved Reflexes",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideImprovedReflexes() { return false; }

	@ConfigItem(
			position = 10,
			keyName = "hideRapidRestore",
			name = "Hide Rapid Restore",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideRapidRestore() { return false; }

	@ConfigItem(
			position = 11,
			keyName = "hideRapidHeal",
			name = "Hide Rapid Heal",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideRapidHeal() { return false; }

	@ConfigItem(
			position = 12,
			keyName = "hideProtectItem",
			name = "Hide Protect Item",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideProtectItem() { return false; }

	@ConfigItem(
			position = 13,
			keyName = "hideHawkEye",
			name = "Hide Hawk Eye",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideHawkEye() { return false; }

	@ConfigItem(
			position = 14,
			keyName = "hideMysticLore",
			name = "Hide Mystic Lore",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideMysticLore() { return false; }

	@ConfigItem(
			position = 15,
			keyName = "hideSteelSkin",
			name = "Hide Steel Skin",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideSteelSkin() { return false; }

	@ConfigItem(
			position = 16,
			keyName = "hideUltimateStrength",
			name = "Hide Ultimate Strength",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideUltimateStrength() { return false; }

	@ConfigItem(
			position = 17,
			keyName = "hideIncredibleReflexes",
			name = "Hide Incredible Reflexes",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideIncredibleReflexes() { return false; }

	@ConfigItem(
			position = 18,
			keyName = "hideProtectFromMagic",
			name = "Hide Protect From Magic",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideProtectFromMagic() { return false; }

	@ConfigItem(
			position = 19,
			keyName = "hideProtectFromMissiles",
			name = "Hide Protect From Missiles",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideProtectFromMissiles() { return false; }

	@ConfigItem(
			position = 20,
			keyName = "hideProtectFromMelee",
			name = "Hide Protect From Melee",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideProtectFromMelee() { return false; }

	@ConfigItem(
			position = 21,
			keyName = "hideEagleEye",
			name = "Hide Eagle Eye",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideEagleEye() { return false; }

	@ConfigItem(
			position = 22,
			keyName = "hideMysticMight",
			name = "Hide Mystic Might",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideMysticMight() { return false; }

	@ConfigItem(
			position = 23,
			keyName = "hideRetribution",
			name = "Hide Retribution",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideRetribution() { return false; }

	@ConfigItem(
			position = 24,
			keyName = "hideRedemption",
			name = "Hide Redemption",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideRedemption() { return false; }

	@ConfigItem(
			position = 25,
			keyName = "hideSmite",
			name = "Hide Smite",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideSmite() { return false; }

	@ConfigItem(
			position = 26,
			keyName = "hidePreserve",
			name = "Hide Preserve",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hidePreserve() { return false; }

	@ConfigItem(
			position = 27,
			keyName = "hideChivalry",
			name = "Hide Chivalry",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideChivalry() { return false; }

	@ConfigItem(
			position = 28,
			keyName = "hidePiety",
			name = "Hide Piety",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hidePiety() { return false; }

	@ConfigItem(
			position = 29,
			keyName = "hideRigour",
			name = "Hide Rigour",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideRigour() { return false; }

	@ConfigItem(
			position = 30,
			keyName = "hideAugury",
			name = "Hide Augury",
			description = "Hides the widget icon for this prayer."
	)
	default boolean hideAugury() { return false; }


}
