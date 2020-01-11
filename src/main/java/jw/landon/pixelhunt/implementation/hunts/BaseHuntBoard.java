package jw.landon.pixelhunt.implementation.hunts;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import jw.landon.pixelhunt.PixelHunt;
import jw.landon.pixelhunt.api.hunts.Hunt;
import jw.landon.pixelhunt.api.hunts.HuntBoard;
import jw.landon.pixelhunt.implementation.config.ConfigManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BaseHuntBoard implements HuntBoard {

    /** Map of each hunt and the instant it was added and became active. */
    private Map<Hunt, Instant> activeHunts = new HashMap<>();
    /** Name of the hunt board. */
    private String name;
    /** Number of slots to maintain hunts for. */
    private int numSlots;

    protected BaseHuntBoard(String huntBoardName, int numSlots){
        this.name = Objects.requireNonNull(huntBoardName, "name must not be null");
        if(numSlots < 0){
            throw new IllegalArgumentException("number of slots must be greater than or equal to 0");
        }
        this.numSlots = numSlots;
    }

    public String getHuntBoardName(){
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public void addHunts(Hunt... hunts){
        for(Hunt hunt : hunts){
        activeHunts.put(hunt, Instant.now());
                Task.builder()
                        .execute(() -> removeHunts(hunt))
                        .delay(hunt.getHuntDuration(TimeUnit.SECONDS), TimeUnit.SECONDS)
                        .submit(PixelHunt.getInstance());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void removeHunts(Hunt... hunts){
        for(Hunt hunt : hunts){
            activeHunts.remove(hunt);
        }
        fillHunts();
    }

    /**
     * Fills any unfilled spots on the hunt board upon hunt removal.
     */
    private void fillHunts(){
        CommentedConfigurationNode generalNode = ConfigManager.getConfigNode("General");
        long huntDuration = generalNode.getNode("Hunt-Duration-Minutes").getLong(60);

        if(activeHunts.size() < numSlots){
            Hunt hunt = PixelHunt.getHuntAPI().getHuntBuilder()
                    .randomHunt();
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Hunt> getActiveHunts(){
        return new ArrayList<>(activeHunts.keySet());
    }

    /** {@inheritDoc} */
    @Override
    public List<Hunt> getActiveHunts(EnumSpecies species, EnumNature nature){
        List<Hunt> validHunts = new ArrayList<>();

        for(Hunt hunt : activeHunts.keySet()){
            if(hunt.getHuntedSpecies() == species && hunt.isHuntedNature(nature)){
                validHunts.add(hunt);
            }
        }
        return validHunts;
    }

    /** {@inheritDoc} */
    @Override
    public List<Hunt> getActiveHunts(EnumSpecies species){
        List<Hunt> validHunts = new ArrayList<>();

        for(Hunt hunt : activeHunts.keySet()){
            if(hunt.getHuntedSpecies() == species){
                validHunts.add(hunt);
            }
        }
        return validHunts;
    }

    /** {@inheritDoc} */
    @Override
    public PaginationList getHuntBoardPage() {
        return HuntBoardPage.of(new HashMap<>(activeHunts));
    }

}
