package jw.landon.pixelhunt.api.hunts.rewards;

import jw.landon.pixelhunt.api.hunts.Hunt;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A section of code that is to be executed for a player that completed a {@link Hunt}.
 *
 * @author landonjw
 * @since  2.0.0
 */
public class CallbackReward extends RewardBase {

    /** The consumer to invoke when the reward is distributed. */
    private Consumer<Player> consumer;

    /**
     * Constructor for the callback reward.
     *
     * @param callback    the callback to invoke when the reward is distributed
     * @param description the description for the reward, shown on the hunt board,
     *                    accepts colour codes
     * @throws NullPointerException if callback is null
     * @throws NullPointerException if description is null
     */
    public CallbackReward(Consumer<Player> callback, String description){
        super(description);
        this.consumer = Objects.requireNonNull(callback, "callback must not be null");
    }

    /**
     * Constructor for the callback reward.
     *
     * @param callback    the callback to invoke when the reward is distributed
     * @param description the description for the reward, shown on the hunt board,
     *                    accepts colour codes
     * @throws NullPointerException if callback is null
     * @throws NullPointerException if description is null
     */
    public CallbackReward(Runnable callback, String description){
        this((player) -> callback.run(), description);
    }

    /** {@inheritDoc} */
    @Override
    public void distributeReward(Player player) {
        consumer.accept(player);
    }

}
