package jw.landon.pixelhunt.implementation.config;

import jw.landon.pixelhunt.PixelHunt;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads and stores all the configuration settings.
 * It loads from file on server start up. or when a player reloads the plugin.
 *
 * @uathor landonjw
 * @since  2.0.0
 */
public class ConfigManager {

    /** Name of the file to grab configuration settings from. */
    private static final String[] FILE_NAMES = {"Configuration.conf", "Messages.conf"};

    /** Paths needed to locate the configuration file.s */
    private static Path dir, config, messages;
    /** Loaders for the configuration files. */
    private static ConfigurationLoader<CommentedConfigurationNode> configLoad, messagesLoad;
    /** Storage for all the configuration settings. */
    private static CommentedConfigurationNode configNode, messagesNode;

    /**
     * Locates the configuration files and loads them.
     *
     * @param folder Folder where the configuration files are located.
     */
    public static void setup(Path folder){
        dir = folder;
        config = dir.resolve(FILE_NAMES[0]);
        messages = dir.resolve(FILE_NAMES[1]);
        load();
    }

    /**
     * Loads the configuration settings into storage.
     */
    public static void load(){
        //Create directory if it doesn't exist.
        try{
            if(!Files.exists(dir)){
                Files.createDirectory(dir);
            }

            //Create or locate file and load configuration file into storage.
            PixelHunt.getContainer().getAsset(FILE_NAMES[0]).get().copyToFile(config, false, true);
            PixelHunt.getContainer().getAsset(FILE_NAMES[1]).get().copyToFile(messages, false, true);

            configLoad = HoconConfigurationLoader.builder().setPath(config).build();
            messagesLoad = HoconConfigurationLoader.builder().setPath(messages).build();

            configNode = configLoad.load();
            messagesNode = messagesLoad.load();
        }
        catch (IOException e){
            PixelHunt.getLogger().error("PixelHunt configuration could not load.");
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration settings to configuration files.
     */
    public static void save(){
        Task save = Task.builder().execute(() -> {

            try{
                configLoad.save(configNode);
                messagesLoad.save(messagesNode);
            }
            catch(IOException e){
                PixelHunt.getLogger().error("PixelHunt could not save configuration.");
                e.printStackTrace();
            }

        }).async().submit(PixelHunt.getInstance());
    }

    /**
     * Gets a node from the configuration, where all configuration settings are stored.
     *
     * @param node A node within the configuration.
     * @return A node within the configuration node.
     */
    public static CommentedConfigurationNode getConfigNode(Object... node){
        return configNode.getNode(node);
    }

    /**
     * Gets a node from the messages configuration, where all message settings are stored.
     *
     * @param node A node within the messages configuration.
     * @return A node within the messages node.
     */
    public static CommentedConfigurationNode getMessagesNode(Object... node){
        return messagesNode.getNode(node);
    }
}
