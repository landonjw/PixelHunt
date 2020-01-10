package jw.landon.pixelhunt.api.hunts.rewards;

import jw.landon.pixelhunt.api.hunts.Hunt;
import org.spongepowered.api.entity.living.player.Player;

/**
 * A reward to be given to a player when they complete a {@link Hunt}.
 *
 * @author landonjw
 * @since  2.0.0
 */
public interface Reward {

    /**
     * Distributes the reward to a player.
     */
    void distributeReward(Player player);

    /**
     * Gets a description of the reward. This is displayed on the hunt board.
     *
     * @return A description of the reward.
     */
    String getDescription();

}
