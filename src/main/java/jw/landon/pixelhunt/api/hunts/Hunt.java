package jw.landon.pixelhunt.api.hunts;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import jw.landon.pixelhunt.PixelHunt;
import jw.landon.pixelhunt.api.hunts.rewards.Reward;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A hunt that requires players to catch a Pokemon of a specific species and nature, giving rewards upon completion.
 *
 * @author landonjw
 * @since  2.0.0
 */
public interface Hunt {

    /**
     * Gets the species that is being hunted.
     *
     * @return the species that is being hunted
     */
    EnumSpecies getHuntedSpecies();

    /**
     * Gets a list of natures that are being hunted.
     *
     * @return a list of natures that are being hunted
     */
    List<EnumNature> getHuntedNatures();

    /**
     * Checks if a specific nature is being hunted.
     *
     * @param nature the nature to check
     * @return true if the nature is being hunted, false if it isn't
     * @throws NullPointerException if the nature is null
     */
    boolean isHuntedNature(EnumNature nature);

    /**
     * Gets a list of rewards to be given upon completion of the hunt.
     *
     * @return list of reward to be given upon completion of the hunt
     */
    List<Reward> getRewards();

    /**
     * Gets the amount of time the hunt is active for.
     *
     * @param unit the unit of time to get duration in
     * @return the amount of time the hunt is active for
     * @throws NullPointerException if the unit is null
     */
    long getHuntDuration(TimeUnit unit);

    /**
     * Gets a builder responsible for creating a hunt.
     *
     * @return builder responsible for creating a hunt
     */
    static Builder builder(){
        return PixelHunt.getHuntAPI().getHuntBuilder();
    }

    /**
     * Builder for a hunt object.
     */
    interface Builder{

        /**
         * Sets the species that is to be hunted.
         *
         * @param species species that is to be hunted
         * @return builder with the species set
         * @throws NullPointerException if species is null
         */
        Builder setSpecies(EnumSpecies species);

        /**
         * Sets the natures that are to be hunted.
         *
         * @param natures natures that are to be hunted
         * @return builder with the natures set
         * @throws IllegalArgumentException if a nature is null
         */
        Builder setNatures(EnumNature... natures);

        /**
         * Sets rewards to be given to a player upon completion of the hunt.
         *
         * @param rewards rewards to be given to a player upon competion of the hunt
         * @return builder with the rewards set
         * @throws IllegalArgumentException if a reward is null
         */
        Builder setRewards(Reward... rewards);

        /**
         * Sets the duration the hunt will be active for.
         *
         * @param duration duration hunt will be active for
         * @param unit     unit of time for duration
         * @return builder with the duration set
         * @throws IllegalArgumentException if duration is below or equal to 0
         * @throws NullPointerException     if time unit is null
         */
        Builder setHuntDuration(long duration, TimeUnit unit);

        /**
         * Generates a hunt with a random species, random natures, duration from configuration and random rewards.
         *
         * @return hunt with a random species, random natures, duration from configuration and random rewards
         */
        Hunt randomHunt();

        /**
         * Generates a hunt with a random species, random natures, duration from configuration and random rewards.
         * Selects random species from the list supplied.
         *
         * @param species species to select from
         * @return hunt with a random species, random natures, duration from configuration and random rewards
         * @throws IllegalArgumentException if a species is null
         */
        Hunt randomHuntFrom(EnumSpecies... species);

        /**
         * Generates a hunt with a random species, random natures, duration from configuration and random rewards.
         * Selects a random species, excluding species from the list supplied.
         *
         * @param species species to select from
         * @return hunt with a random species, random natures, duration from configuration and random rewards
         * @throws IllegalArgumentException if a species is null
         */
        Hunt randomHuntExcluding(EnumSpecies... species);

        /**
         * Builds a new hunt from the properties in the builder.
         *
         * <p>If no natures are specified up to this point, 4 random
         * unique natures will be selected and applied to the hunt.</p>
         *
         * <p>If no rewards are specified up to this point, they will
         * be randomly selected and applied to the hunt.</p>
         *
         * @return a new hunt with the properties in the builder
         * @throws IllegalStateException if no species is set
         */
        Hunt build();

    }

}
