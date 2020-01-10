package jw.landon.pixelhunt.api.hunts.rewards;

import jw.landon.pixelhunt.PixelHunt;
import jw.landon.pixelhunt.api.hunts.Hunt;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * An amount of money given to a player that completed a {@link Hunt}.
 *
 * @author landonjw
 * @since  2.0.0
 */
public class CurrencyReward extends RewardBase {

    /** The amount of money to reward. */
    private double amount;
    /** The type of currency to reward. */
    private Currency currency;

    /**
     * Constructor for the currency reward.
     *
     * @param amount      the amount of money to reward
     * @param currency    the type of currency to reward, null treated as default currency
     * @param description the description for the reward, shown on the hunt board,
     *                    accepts colour codes
     * @throws IllegalStateException if there isn't an economy service loaded
     * @throws NullPointerException if the description is null
     */
    public CurrencyReward(double amount, Currency currency, String description){
        super(description);
        if(!PixelHunt.getEconomyService().isPresent()){
            throw new IllegalStateException("no economy service is loaded");
        }

        this.amount = amount;
        if(currency == null){
            this.currency = PixelHunt.getEconomyService().get().getDefaultCurrency();
        }
        else{
            this.currency = currency;
        }
    }

    /**
     * Constructor for the currency reward, using default currency.
     *
     * @param amount the amount of money to reward
     * @param description the description for the reward, shown on the hunt board,
     *                    accepts colour codes
     * @throws IllegalStateException if there isn't an economy service loaded
     * @throws NullPointerException if the description is null
     */
    public CurrencyReward(double amount, String description){
        super(description);
        if(!PixelHunt.getEconomyService().isPresent()){
            throw new IllegalStateException("no economy service is loaded");
        }

        this.amount = amount;
        this.currency = PixelHunt.getEconomyService().get().getDefaultCurrency();
    }

    /** {@inheritDoc} */
    @Override
    public void distributeReward(Player player) {
        if(PixelHunt.getEconomyService().isPresent()){
            EconomyService economyService = PixelHunt.getEconomyService().get();
            Optional<UniqueAccount> maybeAccount = economyService.getOrCreateAccount(player.getUniqueId());

            if(maybeAccount.isPresent()){
                UniqueAccount account = maybeAccount.get();

                EventContext eventContext = EventContext.builder()
                        .add(EventContextKeys.PLUGIN, PixelHunt.getContainer()).build();

                Cause cause = Cause.of(eventContext, PixelHunt.getContainer());

                account.deposit(currency, BigDecimal.valueOf(amount), cause);
            }
        }
    }

}
