package jw.landon.pixelhunt.implementation.hunts;

import jw.landon.pixelhunt.api.hunts.HuntBoard;
import jw.landon.pixelhunt.api.hunts.HuntBoardRegistry;
import java.util.*;

/**
 * Implementation for {@link HuntBoardRegistry}
 *
 * @author landonjw
 * @since  2.0.0
 */
public class BaseHuntBoardRegistry implements HuntBoardRegistry {

    /** All hunt boards registed in the registry. */
    private Set<HuntBoard> huntBoards = new HashSet<>();

    /** {@inheritDoc} */
    @Override
    public Set<HuntBoard> getHuntBoards() {
        return new HashSet<>(huntBoards);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<HuntBoard> getHuntBoard(String huntBoardName) {
        for(HuntBoard huntBoard : huntBoards){
            if(huntBoard.getHuntBoardName().equalsIgnoreCase(huntBoardName)){
                return Optional.of(huntBoard);
            }
        }
        return Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    public void addHuntBoard(HuntBoard huntBoard) {
        Objects.requireNonNull(huntBoard, "hunt board must not be null");
        if(getHuntBoard(huntBoard.getHuntBoardName()).isPresent()){
            throw new IllegalArgumentException("hunt board already exists with name");
        }
        huntBoards.add(huntBoard);
    }

    /** {@inheritDoc} */
    @Override
    public void removeHuntBoard(HuntBoard huntBoard) {
        Objects.requireNonNull(huntBoard, "hunt board must not be null");
        huntBoards.remove(huntBoard);
    }

}
