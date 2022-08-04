package com.ordwen.odailyquests.quests.player.progression.checkers;

import com.ordwen.odailyquests.configuration.essentials.Synchronization;
import com.ordwen.odailyquests.configuration.functionalities.DisabledWorlds;
import com.ordwen.odailyquests.quests.player.progression.AbstractProgressionIncreaser;
import com.ordwen.odailyquests.quests.player.progression.types.AbstractQuest;
import com.ordwen.odailyquests.quests.player.progression.types.ItemQuest;
import com.ordwen.odailyquests.quests.QuestType;
import com.ordwen.odailyquests.quests.player.QuestsManager;
import com.ordwen.odailyquests.quests.player.progression.Progression;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class AbstractItemChecker extends AbstractProgressionIncreaser {

    /**
     * Increase player quest progression.
     *
     * @param player    the player to increase progression for.
     * @param itemStack the item to increase progression for.
     * @param amount    the amount to increase progression by.
     * @param questType the quest type to increase progression for.
     */
    public void setPlayerQuestProgression(Player player, ItemStack itemStack, int amount, QuestType questType) {
        if (DisabledWorlds.isWorldDisabled(player.getWorld().getName())) {
            return;
        }

        final HashMap<AbstractQuest, Progression> playerQuests = QuestsManager.getActiveQuests().get(player.getName()).getPlayerQuests();

        for (AbstractQuest abstractQuest : playerQuests.keySet()) {

            final Progression progression = playerQuests.get(abstractQuest);
            if (!progression.isAchieved() && abstractQuest.getType() == questType) {
                if (abstractQuest instanceof ItemQuest quest) {
                    boolean isRequiredItem;

                    if (quest.getRequiredItem() == null) isRequiredItem = true;
                    else isRequiredItem = (quest.getRequiredItem().isSimilar(itemStack));

                    if (isRequiredItem) {
                        increaseProgression(player, progression, abstractQuest, amount);
                        if (!Synchronization.isSynchronised()) {
                            break;
                        }
                    }
                }
            }
        }
    }
}