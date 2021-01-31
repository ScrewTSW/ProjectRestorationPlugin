import java.lang.StringBuilder;

public class ProjectRestorationPlugin extends Plugin {

    public static final String NAME = "ProjectRestorationPlugin";
    public static final int MAJOR = 1;
    public static final int MINOR = 0;
    public static final int REVISION = 0;

    private PropertiesFile properties;
    private ProjectRestorationPluginListener listener = null;
    private static int sanctuaryChunkCount = 1;
    private static Location spawnLocation = null;

    public void initialize() {
        Logger.info("Initializing listener");
        listener = new ProjectRestorationPluginListener();
        sanctuaryChunkCount = properties.getInt("sanctuary-exclusion-chunk-count", 1);
        spawnLocation = etc.getServer().getSpawnLocation();
        Logger.info("Spawn location: x:"+spawnLocation.x+" y:"+spawnLocation.y+" z:"+spawnLocation.z);

        Logger.info("Registering events");
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
        try {
            etc.getLoader().addListener(hook, listener, this, priority);
        } catch (NoSuchFieldError e) {
            Logger.error("Could not register for "+hook.toString()+" event.");
        }
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
        Logger.warn(ProjectRestorationPlugin.NAME + " - Server-side map protection disabled!");
    }

    /**
     * When the plugin is enabled (including when the server is just started)
     */
    @Override
    public void enable() {
        setName(ProjectRestorationPlugin.NAME);
        properties = new PropertiesFile("server.properties");
        Logger.init(Logger.LogLevel.parse(properties.getString("log-level","INFO")), this);
        System.out.println("[" + getName() + "] " + ProjectRestorationPlugin.getVersion() + " log-level:"+Logger.getLogLevel().getValue() + " parsed from:"+properties.getString("log-level","INFO"));
        Logger.warn("Server-side map protection enabled.");
    }

    public static String getVersion() {
        return new StringBuilder("[v").append(MAJOR).append(".").append(MINOR).append(".").append(REVISION).append("]").toString();
    }

    public static Location getSpawnLocation() {
        return spawnLocation;
    }

    public static boolean isInBounds(double x, double y, double z) {
        int distanceFromSpawn = Math.max(Math.abs((int)x - (int)Math.floor(spawnLocation.x)), Math.abs((int)z - (int)Math.floor(spawnLocation.z)));
        Logger.trace("Event location: x:"+x+" z:"+z+ " spawn: x:"+spawnLocation.x+" z:"+spawnLocation.z+" sanctuarySize:"+sanctuaryChunkCount*16+" distance:"+distanceFromSpawn);
        return distanceFromSpawn <= sanctuaryChunkCount*16;
    }

    public static boolean isInBounds(Location location) {
        return isInBounds(location.x, location.y, location.z);
    }
}
