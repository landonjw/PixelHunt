package jw.landon.pixelhunt.api.hunts;

/**
 * The base of the Hunt API.
 *
 * @author landonjw
 * @since  2.0.0
 */
public interface HuntAPI {

    /**
     * Gets PixelHunt's hunt manager, containing all active hunts.
     *
     * @return hunt manager containng all active hunts
     */
    HuntManager getHuntManager();

    /**
     * Creates a new hunt builder
     *
     * @return new hunt builder
     */
    Hunt.Builder getHuntBuilder();

}
