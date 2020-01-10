package jw.landon.pixelhunt.api.hunts;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import jw.landon.pixelhunt.implementation.hunts.BaseHunt;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.List;

/**
 * Contains all active {@link Hunt}s for PixelHunt.
 *
 * @author landonjw
 * @since  2.0.0
 */
public interface HuntManager {

    /**
     * Adds hunts to the manager, making them active.
     *
     * @param hunts hunts to add
     * @throws IllegalArgumentException if a hunt is null
     */
    void addHunts(BaseHunt... hunts);

    /**
     * Removes hunts from the manager.
     *
     * @param hunts hunts to remove
     * @throws IllegalArgumentException if a hunt is null
     */
    void removeHunts(BaseHunt... hunts);

    /**
     * Gets a list of all active hunts.
     *
     * @return list of all active hunts
     */
    List<BaseHunt> getActiveHunts();

    /**
     * Gets a list of all active hunts of a specific species.
     *
     * @param species the species to get all active hunts for
     * @return list of all active hunts of a given species
     * @throws NullPointerException if species is null
     */
    List<BaseHunt> getActiveHunts(EnumSpecies species);

    /**
     * Gets a list of all active hunts of a specific species and nature.
     *
     * @param species the species to get all active hunts for
     * @param nature  the nature to get all active hunts for
     * @return list of all active hunts of a given species and nature
     * @throws NullPointerException if species is null
     * @throws NullPointerException if nature is null
     */
    List<BaseHunt> getActiveHunts(EnumSpecies species, EnumNature nature);

    /**
     * Gets a page containing the information on all active hunts.
     *
     * @return page containing the information on all active hunts
     */
    PaginationList getHuntBoard();

}
