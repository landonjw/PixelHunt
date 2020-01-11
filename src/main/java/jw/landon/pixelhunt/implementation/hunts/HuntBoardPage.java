package jw.landon.pixelhunt.implementation.hunts;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import jw.landon.pixelhunt.api.hunts.Hunt;
import jw.landon.pixelhunt.implementation.config.ConfigManager;
import jw.landon.pixelhunt.api.hunts.rewards.Reward;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.FormattingCodeTextSerializer;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Used to create a hunt board that contains a list of active hunts.
 * The text for these boards are configurable in Messages.conf, and there are several placeholders available.
 *
 * @author landonjw
 * @since  2.0.0
 */
public class HuntBoardPage {

    /** The configuration node that correlates to the messages for a hunt board. */
    private static CommentedConfigurationNode boardNode;
    /** The formatter for all text with colour codes. */
    private static FormattingCodeTextSerializer formatter;

    /**
     * Gets a page with a list of active hunt descriptions.
     *
     * @param hunts map of hunts and the times they were started
     * @return a page with a list of active hunt descriptions
     * @throws NullPointerException if hunt map is null
     */
    public static PaginationList of(Map<Hunt, Instant> hunts){
        Objects.requireNonNull(hunts, "hunt map must not be null");

        boardNode = ConfigManager.getMessagesNode("Messages", "Hunt-Board");
        formatter = TextSerializers.FORMATTING_CODE;

        PaginationList.Builder pageBuilder = PaginationList.builder();

        Text header = formatter.deserialize(boardNode.getNode("Board-Header").getString("Pixelmon Hunts"));
        pageBuilder.header(header);

        Text padding = formatter.deserialize(boardNode.getNode("Board-Padding").getString("-"));
        pageBuilder.padding(padding);

        List<Text> contents = new ArrayList<>();

        int numHunts = 0;
        for(Hunt hunt : hunts.keySet()){
            numHunts++;
            Text huntNumber = Text.of(TextColors.WHITE, TextStyles.BOLD, numHunts + " ");
            contents.add(huntNumber.concat(getHuntDescription(hunt, hunts.get(hunt))));
        }

        pageBuilder.contents(contents);

        Text info = formatter.deserialize(boardNode.getNode("Board-Info-Label").getString("?"));
        Text infoHover = formatter.deserialize(boardNode.getNode("Board-Info-Hover").getString(""));
        if(!infoHover.toPlain().equalsIgnoreCase("")){
            info.toBuilder().onHover(TextActions.showText(infoHover));
        }
        pageBuilder.footer(info);

        return pageBuilder.build();
    }

    /**
     * Gets the description for a hunt. This is displayed in the body of the hunt board.
     *
     * @param hunt        the hunt to get a description of
     * @param timeStarted the time the hunt was started
     * @return text that describes a hunt
     * @throws NullPointerException if the hunt is null
     * @throws NullPointerException if time started instant is null
     */
    private static Text getHuntDescription(Hunt hunt, Instant timeStarted){
        Objects.requireNonNull(hunt, "hunt must not be null");
        Objects.requireNonNull(timeStarted, "time started instant must not be null");

        Text pokemon = getPokemonText(hunt);
        Text natures = getNatureText(hunt);
        Text rewards = getRewardText(hunt);
        Text expiry = getExpiryText(hunt, timeStarted);

        Text space = Text.of(" ");

        Text description = Text.builder()
                .append(pokemon, space, natures, space, rewards, space, expiry)
                .build();

        return description;
    }

    /**
     * Gets the pokemon text for a hunt board body.
     * This should describe which species the hunt is corresponding to.
     *
     * Placeholders available for text:
     * <ul>
     *     <li>{species} : The hunted species</li>
     * </ul>
     *
     * @param hunt the hunt to get pokemon for
     * @return text that describes which species the hunt is corresponding to
     */
    private static Text getPokemonText(Hunt hunt){
        String pokemon = boardNode.getNode("Pokemon-Label").getString("&b{species}");
        pokemon = pokemon.replace("{species}", hunt.getHuntedSpecies().getLocalizedName());
        return formatter.deserialize(pokemon);
    }

    /**
     * Gets the nature text for a hunt board body.
     * Includes a hover to see which natures are required to complete a hunt.
     *
     * Placeholders available for text:
     * <ul>
     *     <li>{nature} : The hunted nature</li>
     * </ul>
     *
     * @param hunt the hunt to get natures for
     * @return text that can be hovered to view natures to complete a hunt
     */
    private static Text getNatureText(Hunt hunt){
        String nature = boardNode.getNode("Nature-Label").getString("&8&l[&aNatures&8&l]");
        Text natureLabel = formatter.deserialize(nature);
        Text natureHover = Text.EMPTY;
        for(EnumNature huntedNature : hunt.getHuntedNatures()){
            String natureHoverBody = boardNode.getNode("Nature-Hover").getString("&bNature: &f{nature}");
            natureHoverBody = natureHoverBody.replace("{nature}", huntedNature.getLocalizedName());
            natureHover = natureHover.concat(formatter.deserialize(natureHoverBody));
        }
        natureLabel = natureLabel.toBuilder()
                .onHover(TextActions.showText(natureHover))
                .build();

        return natureLabel;
    }

    /**
     * Gets the reward text for a hunt board body.
     * Includes a hover to see the rewards given from completing the hunt.
     *
     * No placeholders are available. Descriptions for rewards are done through the {@link Reward} interface.
     *
     * @param hunt the hunt to get reward text for
     * @return text that can be hovered to view hunt's rewards
     */
    private static Text getRewardText(Hunt hunt){
        String reward = boardNode.getNode("Reward-Label").getString("&8&l[&eRewards&8&l]");
        Text rewardLabel = formatter.deserialize(reward);
        Text rewardHover = Text.EMPTY;
        for(Reward huntReward : hunt.getRewards()){
            rewardHover = rewardHover.concat(formatter.deserialize(huntReward.getDescription()));
        }
        rewardLabel = rewardLabel.toBuilder()
                .onHover(TextActions.showText(rewardHover))
                .build();

        return rewardLabel;
    }

    /**
     * Gets the expiration text for a hunt board body.
     * Includes a hover to see when the hunt will expire.
     *
     * Placeholders available for text:
     * <ul>
     *     <li>{seconds} : Seconds until hunt expires</li>
     *     <li>{minutes} : Minutes until hunt expires</li>
     *     <li>{hours}   : Hours until hunt expires</li>
     *     <li>{days}    : Days until hunt expires</li>
     * </ul>
     *
     * @param hunt        the hunt to get expiration text for
     * @param timeStarted the time the hunt was started
     * @return text that can be hovered to view expiration time
     */
    private static Text getExpiryText(Hunt hunt, Instant timeStarted){
        String expiry = boardNode.getNode("Expiry-Label").getString("&8&l[&cExpiry&8&l]");
        Text expiryLabel = formatter.deserialize(expiry);
        Duration timeElapsed = Duration.between(timeStarted, Instant.now());
        String plainExpiryHover = boardNode.getNode("Expiry-Hover").getString("&f{days}&bD &f{hours}&bH " +
                "&f{minutes}&bM &f{seconds}&bS");
        long timeRemainingSeconds = hunt.getHuntDuration(TimeUnit.SECONDS) - timeElapsed.getSeconds();
        plainExpiryHover = plainExpiryHover.replace("{seconds}",
                "" + timeRemainingSeconds);
        plainExpiryHover = plainExpiryHover.replace("{minutes}",
                "" + TimeUnit.MINUTES.convert(timeRemainingSeconds, TimeUnit.SECONDS));
        plainExpiryHover = plainExpiryHover.replace("{hours}",
                "" + TimeUnit.HOURS.convert(timeRemainingSeconds, TimeUnit.SECONDS));
        plainExpiryHover = plainExpiryHover.replace("{days}",
                "" + TimeUnit.DAYS.convert(timeRemainingSeconds, TimeUnit.SECONDS));
        Text expiryHover = formatter.deserialize(plainExpiryHover);
        expiryLabel = expiryLabel.toBuilder()
                .onHover(TextActions.showText(expiryHover))
                .build();

        return expiryLabel;
    }

}
