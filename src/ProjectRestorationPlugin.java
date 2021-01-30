import java.lang.StringBuilder;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProjectRestorationPlugin extends Plugin {

    private static String NAME = "ProjectRestorationPlugin";
    private static int MAJOR = 1;
    private static int MINOR = 0;
    private static int REVISION = 0;

    private PropertiesFile properties;
    private ProjectRestorationPluginListener listener = null;

    public void initialize() {
        log("Registering listeners");

        listener = new ProjectRestorationPluginListener(this);

        register(PluginLoader.Hook.BLOCK_DESTROYED, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.BLOCK_BROKEN, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.BLOCK_PLACE, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.BLOCK_PHYSICS, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.BLOCK_RIGHTCLICKED, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.COMPLEX_BLOCK_CHANGE);
        register(PluginLoader.Hook.COMPLEX_BLOCK_SEND);
        register(PluginLoader.Hook.EXPLODE, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.FLOW, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.IGNITE, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.LIQUID_DESTROY, PluginListener.Priority.CRITICAL);
        register(PluginLoader.Hook.LOGIN);
        register(PluginLoader.Hook.PLAYER_MOVE);
        register(PluginLoader.Hook.TELEPORT);
    }

    /**
     * Register a hook
     *
     * @param hook the hook to register
     * @priority the priority to use
     */
    private void register(PluginLoader.Hook hook, PluginListener.Priority priority) {
        etc.getLoader().addListener(hook, listener, this, priority);
    }

    /**
     * Register a hook with default priority
     *
     * @param hook the hook to register
     */
    private void register(PluginLoader.Hook hook) {
        register(hook, PluginListener.Priority.HIGH);
    }

    /**
     * When the plugin is disabled the plugin will cease to function
     */
    @Override
    public void disable() {
        log(ProjectRestorationPlugin.NAME + " - Server-side map protection disabled!");
    }

    /**
     * When the plugin is enabled (including when the server is just started)
     */
    @Override
    public void enable() {
        setName(ProjectRestorationPlugin.NAME);
        log(ProjectRestorationPlugin.NAME + " - Server-side map protection enabled.");

        properties = new PropertiesFile("server.properties");
    }

    /**
     * Log a message
     *
     * @param str the string to log
     */
    public void log(String str) {
        System.out.println("[" + getName() + "] " + getVersion() + " " + str);
    }
    
    public String getVersion() {
        return new StringBuilder("[v").append(MAJOR).append(".").append(MINOR).append(".").append(REVISION).append("]").toString();
    }

}
