import java.lang.StringBuilder;

public class ProjectRestorationPlugin extends Plugin {

    public static final String NAME = "ProjectRestorationPlugin";
    public static final int MAJOR = 1;
    public static final int MINOR = 0;
    public static final int REVISION = 0;

    private PropertiesFile properties;
    private ProjectRestorationPluginListener listener = null;
    private static int sanctuaryChunkCount = 1;
    private static int worldBorderChunkCount = 2;
    private static Location spawnLocation = null;

    public void initialize() {
        Logger.info("Initializing listener");
        listener = new ProjectRestorationPluginListener();
        sanctuaryChunkCount = properties.getInt("sanctuary-exclusion-chunk-count", 1);
        worldBorderChunkCount = properties.getInt("sanctuary-world-border-chunk-count", 2);
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

    public static int getSanctuaryChunkCount() {
        return sanctuaryChunkCount;
    }

    public static int getWorldBorderChunkCount() {
        return worldBorderChunkCount;
    }

    public static boolean isInBounds(double x, double y, double z, int chunkCount) {
        int distanceFromSpawn = Math.max(Math.abs((int)x - (int)Math.floor(spawnLocation.x)), Math.abs((int)z - (int)Math.floor(spawnLocation.z)));
        Logger.trace("Event location: x:"+x+" z:"+z+ " spawn: x:"+spawnLocation.x+" z:"+spawnLocation.z+" sanctuarySize:"+chunkCount*16+" distance:"+distanceFromSpawn);
        return distanceFromSpawn <= chunkCount*16;
    }

    public static boolean isInBounds(Location location, int chunkCount) {
        return isInBounds(location.x, location.y, location.z, chunkCount);
    }

    public static Location getNearestLocationInBounds(Location location, int chunkCount) {
        Location inBoundsLocation = new Location(location.x, 0d, location.z, location.rotX, location.rotY);
        double minX = Math.floor(getSpawnLocation().x) - chunkCount*16;
        double maxX = Math.floor(getSpawnLocation().x) + chunkCount*16;
        double minZ = Math.floor(getSpawnLocation().z) - chunkCount*16;
        double maxZ = Math.floor(getSpawnLocation().z) + chunkCount*16;
        if (location.x > maxX) {
            Logger.debug("X:"+inBoundsLocation.x+" is larger than maximal X:"+maxX);
            inBoundsLocation.x = maxX+.5;
        }
        if (location.x < minX) {
            Logger.debug("X:"+inBoundsLocation.x+" is smaller than minimal X:"+minX);
            inBoundsLocation.x = minX+.5;
        }
        if (location.z > maxZ) {
            Logger.debug("Z:"+inBoundsLocation.z+" is larger than maximal Z:"+maxZ);
            inBoundsLocation.z = maxZ+.5;
        }
        if (location.z < minZ) {
            Logger.debug("Z:"+inBoundsLocation.z+" is smaller than minimal Z:"+minZ);
            inBoundsLocation.z = minZ+.5;
        }
        etc.getServer().loadChunk((int)inBoundsLocation.x, (int)inBoundsLocation.z);
        int teleportY = etc.getServer().getHighestBlockY((int)inBoundsLocation.x, (int)inBoundsLocation.z);
        Block highestBlock = etc.getServer().getBlockAt((int)inBoundsLocation.x, teleportY, (int)inBoundsLocation.z);
        while (highestBlock.getType() == 0 && teleportY > 0) {
            teleportY-=1;
            highestBlock = etc.getServer().getBlockAt((int)inBoundsLocation.x, teleportY, (int)inBoundsLocation.z);
        }
        inBoundsLocation.y = teleportY+1;
        return inBoundsLocation;
    }
}
