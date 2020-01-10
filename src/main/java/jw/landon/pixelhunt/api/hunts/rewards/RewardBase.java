package jw.landon.pixelhunt.api.hunts.rewards;

import java.util.Objects;

/**
 * Base for a {@link Reward}, implementing the description.
 *
 * @author landonjw
 * @since  2.0.0
 */
public abstract class RewardBase implements Reward {

    /** The description for the reward. */
    private String description;

    /**
     * Constructor for the reward base.
     *
     * @param description the description for the reward, shown on the hunt board,
     *                    accepts colour codes
     */
    public RewardBase(String description){
        this.description = Objects.requireNonNull(description, "description must not be null");
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return description;
    }

}
