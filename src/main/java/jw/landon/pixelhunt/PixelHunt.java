package jw.landon.pixelhunt;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import jw.landon.pixelhunt.api.hunts.HuntAPI;
import jw.landon.pixelhunt.implementation.commands.AddCommand;
import jw.landon.pixelhunt.implementation.commands.BaseCommand;
import jw.landon.pixelhunt.implementation.commands.ReloadCommand;
import jw.landon.pixelhunt.implementation.commands.RemoveCommand;
import jw.landon.pixelhunt.implementation.config.ConfigManager;
import jw.landon.pixelhunt.implementation.hunts.BaseHuntAPI;
import jw.landon.pixelhunt.implementation.listeners.PixelmonMoveListener;
import jw.landon.pixelhunt.implementation.listeners.PokemonCaptureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;
import java.nio.file.Path;
import java.util.*;

@Plugin(id = PixelHunt.PLUGIN_ID,
        name = PixelHunt.PLUGIN_NAME,
        version = PixelHunt.PLUGIN_VERSION,
        dependencies = {
                @Dependency(id = Pixelmon.MODID)
        })
public class PixelHunt {

    /** The ID of the plugin. */
    public static final String PLUGIN_ID = "pixelhunt";
    /** The name of the plugin. */
    public static final String PLUGIN_NAME = "PixelHunt";
    /** The version of the plugin. */
    public static final String PLUGIN_VERSION = "2.0.0";

    /** The instance of the plugin. */
    private static PixelHunt instance;
    /** The container containing information regarding the plugin. */
    private static PluginContainer container;
    /** The logger for logging messages to console. */
    private static Logger logger = LoggerFactory.getLogger(PLUGIN_NAME);

    /** The active Hunt API. */
    private static HuntAPI huntAPI;

    /** The plugin directory for configurations to be created. */
    @Inject
    @ConfigDir(sharedRoot=false)
    private Path dir;

    /** The economy service loaded on the server. Null if no economy service is present. */
    private static EconomyService economyService;

    /**
     * Initializes the plugin and loads the configuration.
     *
     * @param event the event called when the server is in the preinitialization phase
     */
    @Listener
    public void preInit(GamePreInitializationEvent event){
        instance = this;
        container = Sponge.getPluginManager().getPlugin(PLUGIN_ID).get();

        ConfigManager.setup(dir);
        huntAPI = new BaseHuntAPI();
        Sponge.getServiceManager().setProvider(this, HuntAPI.class, huntAPI);
    }

    /**
     * Creates and registers all commands and events.
     *
     * @param event the event called when the server is in the initialization phase
     */
    @Listener
    public void init(GameInitializationEvent event){
        CommandSpec remove = CommandSpec.builder()
                .description(Text.of("Removes an active hunt"))
                .permission("pixelhunt.admin.commands.remove")
                .arguments(
                        GenericArguments.optional(GenericArguments.integer(Text.of("slot")))
                )
                .executor(new RemoveCommand())
                .build();

        CommandSpec add = CommandSpec.builder()
                .description(Text.of("Adds an active hunt"))
                .permission("pixelmon.admin.commands.add")
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("species"))),
                        GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("natures")))
                )
                .executor(new AddCommand())
                .build();

        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reload the config!"))
                .permission("pixelhunt.commands.admin.reload")
                .executor(new ReloadCommand())
                .build();

        CommandSpec base = CommandSpec.builder()
                .description(Text.of("List hunted pokemon!"))
                .permission("pixelhunt.commands.base")
                .executor(new BaseCommand())
                .child(remove, "remove")
                .child(add, "add")
                .child(reload, "reload")
                .build();

        Sponge.getCommandManager().register(this, base, "pixelhunt", "hunts", "hunt");

        Sponge.getEventManager().registerListeners(this, new PixelmonMoveListener());
        Pixelmon.EVENT_BUS.register(new PokemonCaptureListener());
    }

    /**
     * Loads the Sponge economy service is one is found.
     *
     * @param event the event called when the server is in the post initialization phase
     */
    @Listener
    public void postInit(final GamePostInitializationEvent event){
        logger.info("Checking for economy service...");
        Optional<EconomyService> maybeEconomy = Sponge.getServiceManager().provide(EconomyService.class);
        if (maybeEconomy.isPresent()) {
            economyService = maybeEconomy.get();
            logger.info("Economy loaded.");
        }
        else {
            logger.info("Economy not loaded. Proceeding without...");
        }
    }

    /**
     * Changes the economy service for the plugin if the server's economy service is to change.
     *
     * @param event the event fired when a service provider changes
     */
    @Listener
    public void onChangeEconomy(ChangeServiceProviderEvent event){
        if (event.getService().equals(EconomyService.class)) {
            economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
        }
    }

    /**
     * Gets the instance of the plugin.
     *
     * @return the instance of the plugin
     */
    public static PixelHunt getInstance(){
        return instance;
    }

    /**
     * Gets the container containing information regarding the plugin.
     *
     * @return the container containing information regarding the plugin.
     */
    public static PluginContainer getContainer(){
        return container;
    }

    /**
     * Gets the logger for logging messages to console.
     *
     * @return the logger for logging messages to console.
     */
    public static Logger getLogger(){
        return logger;
    }

    /**
     * Gets the economy service loaded if one is present.
     *
     * @return the economy service loaded, or empty if one isn't present
     */
    public static Optional<EconomyService> getEconomyService(){
        return Optional.ofNullable(economyService);
    }

    /**
     * Gets the hunt API.
     *
     * @return the hunt API
     */
    public static HuntAPI getHuntAPI(){
        return huntAPI;
    }

}
