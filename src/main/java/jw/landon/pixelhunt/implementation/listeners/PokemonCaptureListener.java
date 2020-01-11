package jw.landon.pixelhunt.implementation.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import jw.landon.pixelhunt.PixelHunt;
import jw.landon.pixelhunt.api.hunts.Hunt;
import jw.landon.pixelhunt.api.hunts.HuntBoard;
import jw.landon.pixelhunt.api.hunts.HuntBoardRegistry;
import jw.landon.pixelhunt.api.hunts.rewards.Reward;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Listens for a successful capture to see if the Pokemon completes any active hunts.
 * If it does, rewards will be distributed to the player and the hunt will be removed.
 *
 * @author landonjw
 * @since  2.0.0
 */
public class PokemonCaptureListener {

    /**
     * Listens for a successful capture to see if the Pokemon completes any active hunts.
     * If it does, rewards will be distributed to the player and the hunt will be removed.
     *
     * @param event the event called when a pokemon is successfully captured
     */
    @SubscribeEvent
    public void onCapture(CaptureEvent.SuccessfulCapture event){
        Pokemon pokemon = event.getPokemon().getPokemonData();
        HuntBoardRegistry registry = PixelHunt.getHuntAPI().getHuntBoardRegistry();

        for(HuntBoard huntBoard : registry.getHuntBoards()){
            for(Hunt hunt : huntBoard.getActiveHunts(pokemon.getSpecies(), pokemon.getNature())){
                for(Reward reward : hunt.getRewards()){
                    reward.distributeReward((Player) event.player);
                }
                huntBoard.removeHunts(hunt);
            }
        }
    }

}