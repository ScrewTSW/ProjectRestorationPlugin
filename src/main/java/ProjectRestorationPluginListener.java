import java.util.MissingFormatArgumentException;

public class ProjectRestorationPluginListener extends PluginListener {

	/*=======================================*
	 * Internal minecraft block interactions *
	 *=======================================*/

	@Override
	public boolean onExplode(Block block) {
	    Logger.trace("Block "+block.toString()+" exploded.");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
	    if (inBounds) Logger.trace("Block x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ()+" tried to explode.");
	    return inBounds;
    }

	@Override
	public boolean onFlow(Block blockFrom, Block blockTo) {
	    Logger.trace("Block "+blockFrom.toString()+" flew to "+blockTo.toString()+".");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(blockTo.getX(), blockTo.getY(), blockTo.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
	    if (inBounds) Logger.trace("Block x:"+blockFrom.getX()+" y:"+blockFrom.getY()+" z:"+blockFrom.getZ()+" tried to flow into x:"+blockTo.getX()+" y:"+blockTo.getY()+" z:"+blockTo.getZ()+".");
        return inBounds;
    }

	@Override
	public boolean onBlockPhysics(Block block, boolean placed) {
	    Logger.trace("Block "+block.toString()+", placed:"+placed+" physics updated.");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
	    if (inBounds) Logger.trace("Block x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ()+" tried to update physics.");
        return inBounds;
    }

	@Override
	public PluginLoader.HookResult onLiquidDestroy( PluginLoader.HookResult currentState, int liquidBlockId, Block targetBlock )  {
	    Logger.trace("Liquid with id:"+liquidBlockId+" destroyed block "+targetBlock.toString()+".");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
	    if (inBounds) {
	        Logger.trace("Block x:"+targetBlock.getX()+" y:"+targetBlock.getY()+" z:"+targetBlock.getZ()+" was about to be destroyed by liquid.");
	        return PluginLoader.HookResult.PREVENT_ACTION;
	    }
        return PluginLoader.HookResult.DEFAULT_ACTION;
    }

	/*=============================*
     * Player - block interactions *
     *=============================*/

	@Override
	public boolean onBlockPlace(Player player, Block blockPlaced, Block blockClicked, Item itemInHand) {
	    Logger.debug("Player "+player.toString()+" placed block "+blockPlaced.toString()+" on block "+blockClicked.toString()+" with item "+itemInHand.toString()+" in hand.");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
        if (inBounds) {
            Logger.warn("Player "+player.toString()+" tried to place block at x:"+blockPlaced.getX()+" y:"+blockPlaced.getY()+" z:"+blockPlaced.getZ());
            player.sendMessage("You cannot place blocks inside Sanctuary chunks.");
        }
	    return inBounds;
    }

	@Override
	public boolean onBlockDestroy(Player player, Block block) {
	    // Weird glitch where after you let go of left mouse, the game tries to break block at x:0,y:0,z:0
	    if (block.getX() == 0 && block.getY() == 0 && block.getZ() == 0) return false;
	    Logger.trace("Player "+player.toString()+" destoryed block "+block.toString()+". [inBounds:"+ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount())+"]");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
	    if (inBounds) {
	        Logger.debug("Player "+player.toString()+" tried to destory block at x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ());
	        player.sendMessage("You cannot destroy blocks inside Sanctuary chunks.");
	    }
	    return inBounds;
	}

	@Override
	public boolean onBlockBreak(Player player, Block block) {
	    Logger.debug("Player "+player.toString()+" broke block "+block.toString()+".");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
        if (inBounds) {
            Logger.warn("Player "+player.toString()+" tried to break block at x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ());
            player.sendMessage("You cannot break blocks inside Sanctuary chunks.");
        }
        return false;
    }

	@Override
	public void onBlockRightClicked(Player player, Block blockClicked, Item item) {
	    Logger.debug("Player "+player.toString()+" right-clicked block "+blockClicked.toString()+" with item "+item.toString()+" in hand.");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
        if (inBounds) {
            Logger.warn("Player "+player.toString()+" tried to right-click block at x:"+blockClicked.getX()+" y:"+blockClicked.getY()+" z:"+blockClicked.getZ());
            player.sendMessage("You cannot interact with blocks inside Sanctuary chunks.");
        } else {
            super.onBlockRightClicked(player, blockClicked, item);
        }
    }

	@Override
	public boolean onIgnite(Block block, Player player) {
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ(), ProjectRestorationPlugin.getSanctuaryChunkCount());
	    if (player != null) {
	        Logger.debug("Player "+player.toString()+" ignited block "+block.toString()+".");
	        if (inBounds) {
	            Logger.warn("Player "+player.toString()+" tried to ignite block at x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ());
	            player.sendMessage("You cannot ignite blocks inside Sanctuary chunks.");
	        }
	        return inBounds;
	    } else {
	        Logger.trace("Block "+block.toString()+" was ignited.");
	        return inBounds;
	    }
    }

	/*==============================*
	 * Player movement interactions *
	 *==============================*/

	@Override
	public void onLogin(Player player) {
		Location playerLocation = player.getLocation();
		Logger.debug("Player "+player.toString()+" logged in at x:"+playerLocation.x+" y:"+playerLocation.y+" z:"+playerLocation.z+".");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(playerLocation, ProjectRestorationPlugin.getWorldBorderChunkCount());
	    if (!inBounds) {
	    	Location inBoundsLocation = ProjectRestorationPlugin.getNearestLocationInBounds(playerLocation, ProjectRestorationPlugin.getWorldBorderChunkCount());
	    	Logger.warn("Player "+player.toString()+" logged in outside world border. Teleporting to x:"+inBoundsLocation.x+" y:"+inBoundsLocation.y+" z:"+inBoundsLocation.z);
	    	player.sendMessage("You have logged in out of the world boundaries. Teleporting you to the nearest border.");
	    	player.teleportTo(inBoundsLocation);
		}
		super.onLogin(player);
    }

	@Override
	public void onPlayerMove(Player player, Location from, Location to) {
		Logger.debug("Player "+player.toString()+" moved from x:"+from.x+" y:"+from.y+" z:"+from.z+" to x:"+to.x+" y:"+to.y+" z:"+to.z+".");
		boolean inBounds = ProjectRestorationPlugin.isInBounds(to, ProjectRestorationPlugin.getWorldBorderChunkCount());
		if (!inBounds) {
			Location inBoundsLocation = ProjectRestorationPlugin.getNearestLocationInBounds(player.getLocation(), ProjectRestorationPlugin.getWorldBorderChunkCount());
			Logger.warn("Player "+player.toString()+" tried to move out of the world border at x:"+to.x+" y:"+to.y+" z:"+to.z);
			player.sendMessage("You are trying to leave the world boundary.");
			player.teleportTo(inBoundsLocation);
		} else {
			super.onPlayerMove(player, from, to);
		}
	}

	@Override
    public boolean onTeleport(Player player, Location from, Location to) {
	    Logger.debug("Player "+player.toString()+" teleported from x:"+from.x+" y:"+from.y+" z:"+from.z+" to x:"+to.x+" y:"+to.y+" z:"+to.z+".");
		boolean inBounds = ProjectRestorationPlugin.isInBounds(to, ProjectRestorationPlugin.getWorldBorderChunkCount());
		if (!inBounds) {
			Location inBoundsLocation = ProjectRestorationPlugin.getNearestLocationInBounds(player.getLocation(), ProjectRestorationPlugin.getWorldBorderChunkCount());
			Logger.warn("Player "+player.toString()+" tried to move out of the world border at x:"+to.x+" y:"+to.y+" z:"+to.z);
			player.sendMessage("You are trying to leave the world boundary.");
		}
	    return !inBounds;
    }
}
