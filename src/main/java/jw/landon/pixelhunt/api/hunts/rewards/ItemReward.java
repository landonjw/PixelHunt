package jw.landon.pixelhunt.api.hunts.rewards;

import jw.landon.pixelhunt.api.hunts.Hunt;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Objects;

/**
 * An item to be given to a player that completed a {@link Hunt}.
 *
 * @author landonjw
 * @since  2.0.0
 */
public class ItemReward extends RewardBase {

    /** The item to reward. */
    private ItemStack item;

    /**
     * Constructor for the item reward.
     *
     * @param item the item to reward
     * @param description the description for the reward, shown on the hunt board,
     *                    accepts colour codes
     * @throws NullPointerException if item is null
     * @throws NullPointerException if description is null
     */
    public ItemReward(ItemStack item, String description){
        super(description);
        this.item = Objects.requireNonNull(item, "item must not be null");
    }

    /** {@inheritDoc} */
    @Override
    public void distributeReward(Player player) {
        player.getInventory().offer(item);
    }

}
