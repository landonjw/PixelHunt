package jw.landon.pixelhunt.implementation.hunts;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import jw.landon.pixelhunt.PixelHunt;
import jw.landon.pixelhunt.api.hunts.Hunt;
import jw.landon.pixelhunt.api.hunts.HuntManager;
import jw.landon.pixelhunt.implementation.config.ConfigManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Implementation for {@link HuntManager}
 *
 * @author landonjw
 * @since  2.0.0
 */
public class BaseHuntManager implements HuntManager {

    /** Map of each hunt and the instant it was added and became active. */
    private Map<BaseHunt, Instant> activeHunts = new HashMap<>();
    /** Map of each hunt and the task that is set to remove the hunt if it isn't completed. */
    private Map<BaseHunt, Task> huntRemovalTasks = new HashMap<>();

    /** {@inheritDoc} */
    @Override
    public void addHunts(BaseHunt... hunts){
        for(BaseHunt hunt : hunts){
            activeHunts.put(hunt, Instant.now());
            huntRemovalTasks.put(
                    hunt,
                    Task.builder()
                            .execute(() -> removeHunts(hunt))
                            .delay(hunt.getHuntDuration(TimeUnit.SECONDS), TimeUnit.SECONDS)
                            .submit(PixelHunt.getInstance())
            );
        }
    }

    /** {@inheritDoc} */
    @Override
    public void removeHunts(BaseHunt... hunts){
        for(BaseHunt hunt : hunts){
            activeHunts.remove(hunt);
            huntRemovalTasks.get(hunt).cancel();
            huntRemovalTasks.remove(hunt);
        }
        fillHunts();
    }

    /**
     * Fills any unfilled spots on the hunt board upon hunt removal.
     */
    private void fillHunts(){
        CommentedConfigurationNode generalNode = ConfigManager.getConfigNode("General");

        int numHunts = generalNode.getNode("Number-Hunts").getInt(4);
        long huntDuration = generalNode.getNode("Hunt-Duration-Minutes").getLong(60);

        if(activeHunts.size() < numHunts){
            Hunt hunt = PixelHunt.getHuntAPI().getHuntBuilder()
                    .randomHunt();
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<BaseHunt> getActiveHunts(){
        return new ArrayList<>(activeHunts.keySet());
    }

    /** {@inheritDoc} */
    @Override
    public List<BaseHunt> getActiveHunts(EnumSpecies species, EnumNature nature){
        List<BaseHunt> validHunts = new ArrayList<>();

        for(BaseHunt hunt : activeHunts.keySet()){
            if(hunt.getHuntedSpecies() == species && hunt.isHuntedNature(nature)){
                validHunts.add(hunt);
            }
        }
        return validHunts;
    }

    /** {@inheritDoc} */
    @Override
    public List<BaseHunt> getActiveHunts(EnumSpecies species){
        List<BaseHunt> validHunts = new ArrayList<>();

        for(BaseHunt hunt : activeHunts.keySet()){
            if(hunt.getHuntedSpecies() == species){
                validHunts.add(hunt);
            }
        }
        return validHunts;
    }

    /** {@inheritDoc} */
    @Override
    public PaginationList getHuntBoard() {
        return BaseHuntBoard.of(new HashMap<>(activeHunts));
    }

}
