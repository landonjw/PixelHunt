package jw.landon.pixelhunt.implementation.hunts;

import jw.landon.pixelhunt.api.hunts.HuntAPI;
import jw.landon.pixelhunt.api.hunts.HuntBoard;
import jw.landon.pixelhunt.api.hunts.HuntBoardRegistry;

/**
 * Implementation for {@link HuntAPI}
 *
 * @author landonjw
 * @since  2.0.0
 */
public class BaseHuntAPI implements HuntAPI {

    /** The hunt manager containing all active hunts. */
    private HuntBoardRegistry huntManager = new BaseHuntBoardRegistry();

    /** {@inheritDoc} */
    @Override
    public HuntBoardRegistry getHuntBoardRegistry() {
        return huntManager;
    }

    /** {@inheritDoc} */
    @Override
    public BaseHunt.Builder getHuntBuilder() {
        return BaseHunt.builder();
    }

    /** {@inheritDoc} */
    @Override
    public HuntBoard createHuntBoard(String huntBoardName, int numSlots) {
        return new BaseHuntBoard(huntBoardName, numSlots);
    }

}
