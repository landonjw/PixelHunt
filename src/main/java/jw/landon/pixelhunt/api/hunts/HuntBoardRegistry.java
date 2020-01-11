package jw.landon.pixelhunt.api.hunts;

import java.util.Optional;
import java.util.Set;

/**
 * Registry that contains all hunt boards.
 *
 * <p>If a hunt board is not added to this registry, players
 * will not be able to view or complete the hunts included,
 * unless external implementations are supplied for those.</p>
 *
 * @author landonjw
 * @since  1.0.0
 */
public interface HuntBoardRegistry {

    /**
     * Gets all registered hunt boards.
     *
     * @return all registered hunt boards
     */
    Set<HuntBoard> getHuntBoards();

    /**
     * Gets a hunt board if one is found with the given name.
     *
     * @param huntBoardName the name to grab hunt board for
     * @return hunt board with the given name if one is found
     * @throws NullPointerException if name is null
     */
    Optional<HuntBoard> getHuntBoard(String huntBoardName);

    /**
     * Adds a hunt board to the registry.
     *
     * @param huntBoard the hunt board to add
     * @throws NullPointerException     if the hunt board is null
     * @throws IllegalArgumentException if the hunt board is already registered
     */
    void addHuntBoard(HuntBoard huntBoard);

    /**
     * Removes a hunt board from the registry.
     *
     * @param huntBoard the hunt board to remove
     * @throws NullPointerException if the hunt board is null
     */
    void removeHuntBoard(HuntBoard huntBoard);

}
