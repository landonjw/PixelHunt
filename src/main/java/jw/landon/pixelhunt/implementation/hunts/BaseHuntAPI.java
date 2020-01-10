package jw.landon.pixelhunt.implementation.hunts;

import jw.landon.pixelhunt.api.hunts.HuntAPI;
import jw.landon.pixelhunt.api.hunts.HuntManager;

/**
 * Implementation for {@link HuntAPI}
 *
 * @author landonjw
 * @since  2.0.0
 */
public class BaseHuntAPI implements HuntAPI {

    /** The hunt manager containing all active hunts. */
    private HuntManager huntManager = new BaseHuntManager();

    /** {@inheritDoc} */
    @Override
    public HuntManager getHuntManager() {
        return huntManager;
    }

    /** {@inheritDoc} */
    @Override
    public BaseHunt.Builder getHuntBuilder() {
        return BaseHunt.builder();
    }

}
