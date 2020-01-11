package jw.landon.pixelhunt.api.hunts;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import jw.landon.pixelhunt.PixelHunt;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.List;

/**
 * Contains a list of active {@link Hunt}.
 *
 * @author landonjw
 * @since  2.0.0
 */
public interface HuntBoard {

    /**
     * Gets the name of the hunt board.
     *
     * @return name of the hunt board
     */
    String getHuntBoardName();

    /**
     * Adds hunts to the manager, making them active.
     *
     * @param hunts hunts to add
     * @throws IllegalArgumentException if a hunt is null
     */
    void addHunts(Hunt... hunts);

    /**
     * Removes hunts from the manager.
     *
     * @param hunts hunts to remove
     * @throws IllegalArgumentException if a hunt is null
     */
    void removeHunts(Hunt... hunts);

    /**
     * Gets a list of all active hunts.
     *
     * @return list of all active hunts
     */
    List<Hunt> getActiveHunts();

    /**
     * Gets a list of all active hunts of a specific species.
     *
     * @param species the species to get all active hunts for
     * @return list of all active hunts of a given species
     * @throws NullPointerException if species is null
     */
    List<Hunt> getActiveHunts(EnumSpecies species);

    /**
     * Gets a list of all active hunts of a specific species and nature.
     *
     * @param species the species to get all active hunts for
     * @param nature  the nature to get all active hunts for
     * @return list of all active hunts of a given species and nature
     * @throws NullPointerException if species is null
     * @throws NullPointerException if nature is null
     */
    List<Hunt> getActiveHunts(EnumSpecies species, EnumNature nature);

    /**
     * Gets a page containing the information on all active hunts.
     *
     * @return page containing the information on all active hunts
     */
    PaginationList getHuntBoardPage();

    /**
     * Creates a new hunt board with a certain name.
     *
     * @param huntBoardName the name to give the hunt board
     * @param numSlots      the number of slots to maintain active hunts in
     * @return new hunt board with the given name
     * @throws NullPointerException     if the name is null
     * @throws IllegalArgumentException if the number of slots is less than 0
     */
    static HuntBoard create(String huntBoardName, int numSlots){
        return PixelHunt.getHuntAPI().createHuntBoard(huntBoardName, numSlots);
    }

}
