package jw.landon.pixelhunt.api.hunts;

/**
 * The base of the Hunt API.
 *
 * @author landonjw
 * @since  2.0.0
 */
public interface HuntAPI {

    /**
     * Gets the hunt board registry, containing all registered hunt boards
     *
     * @return hunt manager containng all active hunts
     */
    HuntBoardRegistry getHuntBoardRegistry();

    /**
     * Creates a new hunt builder.
     *
     * @return new hunt builder
     */
    Hunt.Builder getHuntBuilder();

    /**
     * Creates a new hunt board with a certain name.
     *
     * @param huntBoardName the name to give the hunt board
     * @param numSlots      the number of slots to maintain active hunts in
     * @return new hunt board with the given name
     * @throws NullPointerException     if the name is null
     * @throws IllegalArgumentException if the number of slots is less than 0
     */
    HuntBoard createHuntBoard(String huntBoardName, int numSlots);

}