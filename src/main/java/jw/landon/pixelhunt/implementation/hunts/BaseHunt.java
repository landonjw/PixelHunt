package jw.landon.pixelhunt.implementation.hunts;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import jw.landon.pixelhunt.api.hunts.Hunt;
import jw.landon.pixelhunt.api.hunts.rewards.Reward;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Implementation for {@link Hunt}
 *
 * @author landonjw
 * @since  2.0.0
 */
public class BaseHunt implements Hunt {

    /** The species being hunted. */
    private EnumSpecies huntedSpecies;
    /** The natures being hunted. */
    private List<EnumNature> huntedNatures;
    /** The rewards given upon completion of the hunt. */
    private List<Reward> huntRewards;
    /** The duration of the hunt in seconds. */
    private long duration;

    /**
     * Constructor for the hunt.
     *
     * @param huntedSpecies the species being hunted
     * @param huntedNatures the natures being hunted
     * @param rewards       the rewards given upon completion of the hunt
     * @param durationValue the duration of the hunt in seconds
     * @throws NullPointerException     if species is null
     * @throws NullPointerException     if nature list is null
     * @throws IllegalArgumentException if a nature is null
     * @throws IllegalArgumentException if duration value is below or equal to 0
     */
    protected BaseHunt(EnumSpecies huntedSpecies, List<EnumNature> huntedNatures,
                       List<Reward> rewards, long durationValue){

        this.huntedSpecies = Objects.requireNonNull(huntedSpecies, "species must not be null");
        Objects.requireNonNull(huntedNatures, "nature list must not be null");
        ArrayHelper.validateArrayNonNull(huntedNatures.toArray(new EnumNature[0]));
        this.huntedNatures = huntedNatures;
        if(durationValue <= 0){
            throw new IllegalArgumentException("duration value must be greater than 0");
        }
        this.duration = durationValue;
    }

    /** {@inheritDoc} */
    @Override
    public EnumSpecies getHuntedSpecies(){
        return huntedSpecies;
    }

    /** {@inheritDoc} */
    @Override
    public List<EnumNature> getHuntedNatures(){
        return huntedNatures;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isHuntedNature(EnumNature nature){
        for(EnumNature huntedNature : huntedNatures){
            if(huntedNature == nature){
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public List<Reward> getRewards(){
        return new ArrayList<>(huntRewards);
    }

    /** {@inheritDoc} */
    @Override
    public long getHuntDuration(TimeUnit unit) {
        return unit.convert(duration, TimeUnit.SECONDS);
    }

    public static Builder builder(){
        return new Builder();
    }

    /**
     * Implementation for {@link Hunt.Builder}.
     */
    public static class Builder implements Hunt.Builder {

        /** The species to be hunted. */
        private EnumSpecies species;
        /** The natures to be hunted. */
        private List<EnumNature> natures = new ArrayList<>();
        /** The rewards to be given upon completion of the hunt. */
        private List<Reward> rewards = new ArrayList<>();
        /** The duration of the hunt in seconds. */
        private long duration = 3600;

        /** {@inheritDoc} */
        @Override
        public Builder setSpecies(EnumSpecies species){
            this.species = Objects.requireNonNull(species);
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Builder setNatures(EnumNature... natures){
            if(natures.length > 0){
                ArrayHelper.validateArrayNonNull(natures);
                this.natures = Arrays.asList(natures);
            }
            else{
                this.natures = null;
            }
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Hunt.Builder setRewards(Reward... rewards) {
            if(rewards.length > 0){
                ArrayHelper.validateArrayNonNull(rewards);
                this.rewards = Arrays.asList(rewards);
            }
            else{
                this.rewards = new ArrayList<>();
            }
            return null;
        }

        /** {@inheritDoc} */
        @Override
        public Builder setHuntDuration(long duration, TimeUnit unit){
            if(duration <= 0){
                throw new IllegalArgumentException("time value must be greater than 0");
            }
            Objects.requireNonNull(unit, "time unit must not be null");
            this.duration = unit.toSeconds(duration);
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BaseHunt randomHunt(EnumSpecies... excludedSpecies){
            List<EnumSpecies> excludedSpeciesList = Arrays.asList(excludedSpecies);
            EnumSpecies randomSpecies;

            if(!excludedSpeciesList.isEmpty()){
                do{
                    randomSpecies = EnumSpecies.randomPoke();
                }
                while(containsSpecies(excludedSpeciesList, randomSpecies));
            }
            else{
                randomSpecies = EnumSpecies.randomPoke();
            }

            this.species = randomSpecies;
            return build();
        }

        /** {@inheritDoc} */
        @Override
        public BaseHunt build(){
            if(species == null){
                throw new IllegalStateException("species must be set");
            }
            if(natures.isEmpty()){
                for(int i = 0; i < 4; i++){
                    EnumNature nature ;
                    do{
                        nature = EnumNature.getRandomNature();
                    }
                    while(containsNature(natures, nature));

                    natures.add(nature);
                }
            }

            return new BaseHunt(species, natures, rewards, duration);
        }

        /**
         * Checks if a list of natures contains a specific nature.
         *
         * @param natureList the list of natures
         * @param nature     the nature to check
         * @return true if nature is within nature list, false if it isn't
         */
        private boolean containsNature(List<EnumNature> natureList, EnumNature nature){
            for(EnumNature natureElement : natureList){
                if(natureElement == nature){
                    return true;
                }
            }
            return false;
        }

        /**
         * Checks if a list of species contains a specific species.
         *
         * @param speciesList the list of species
         * @param species     the species to check
         * @return true if species is within species list, false if it isn't
         */
        private boolean containsSpecies(List<EnumSpecies> speciesList, EnumSpecies species){
            for(EnumSpecies speciesElement : speciesList){
                if(speciesElement == species){
                    return true;
                }
            }
            return false;
        }

    }

}
