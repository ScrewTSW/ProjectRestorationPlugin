import java.util.MissingFormatArgumentException;

public class ProjectRestorationPluginListener extends PluginListener {

	/*=======================================*
	 * Internal minecraft block interactions *
	 *=======================================*/

	@Override
	public boolean onExplode(Block block) {
	    Logger.trace("Block "+block.toString()+" exploded.");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ());
	    if (inBounds) Logger.trace("Block x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ()+" tried to explode.");
	    return inBounds;
    }

	@Override
	public boolean onFlow(Block blockFrom, Block blockTo) {
	    Logger.trace("Block "+blockFrom.toString()+" flew to "+blockTo.toString()+".");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(blockTo.getX(), blockTo.getY(), blockTo.getZ());
	    if (inBounds) Logger.trace("Block x:"+blockFrom.getX()+" y:"+blockFrom.getY()+" z:"+blockFrom.getZ()+" tried to flow into x:"+blockTo.getX()+" y:"+blockTo.getY()+" z:"+blockTo.getZ()+".");
        return inBounds;
    }

	@Override
	public boolean onBlockPhysics(Block block, boolean placed) {
	    Logger.trace("Block "+block.toString()+", placed:"+placed+" physics updated.");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ());
	    if (inBounds) Logger.trace("Block x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ()+" tried to update physics.");
        return inBounds;
    }

	@Override
	public PluginLoader.HookResult onLiquidDestroy( PluginLoader.HookResult currentState, int liquidBlockId, Block targetBlock )  {
	    Logger.trace("Liquid with id:"+liquidBlockId+" destroyed block "+targetBlock.toString()+".");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
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
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ());
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
	    Logger.trace("Player "+player.toString()+" destoryed block "+block.toString()+". [inBounds:"+ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ())+"]");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ());
	    if (inBounds) {
	        Logger.debug("Player "+player.toString()+" tried to destory block at x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ());
	        player.sendMessage("You cannot destroy blocks inside Sanctuary chunks.");
	    }
	    return inBounds;
	}

	@Override
	public boolean onBlockBreak(Player player, Block block) {
	    Logger.debug("Player "+player.toString()+" broke block "+block.toString()+".");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ());
        if (inBounds) {
            Logger.warn("Player "+player.toString()+" tried to break block at x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ());
            player.sendMessage("You cannot break blocks inside Sanctuary chunks.");
        }
        return false;
    }

	@Override
	public void onBlockRightClicked(Player player, Block blockClicked, Item item) {
	    Logger.debug("Player "+player.toString()+" right-clicked block "+blockClicked.toString()+" with item "+item.toString()+" in hand.");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
        if (inBounds) {
            Logger.warn("Player "+player.toString()+" tried to right-click block at x:"+blockClicked.getX()+" y:"+blockClicked.getY()+" z:"+blockClicked.getZ());
            player.sendMessage("You cannot interact with blocks inside Sanctuary chunks.");
        } else {
            super.onBlockRightClicked(player, blockClicked, item);
        }
    }

	@Override
	public boolean onIgnite(Block block, Player player) {
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ());
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

	@Override
	public boolean onComplexBlockChange(Player player, ComplexBlock block) {
	    Logger.debug("Player "+player.toString()+" interacted with complex block "+block.toString()+".");
	    boolean inBounds = ProjectRestorationPlugin.isInBounds(block.getX(), block.getY(), block.getZ());
        if (inBounds) {
            Logger.warn("Player "+player.toString()+" tried to interact with complex block at x:"+block.getX()+" y:"+block.getY()+" z:"+block.getZ());
            player.sendMessage("You cannot interact with blocks inside Sanctuary chunks.");
        }
        return inBounds;
    }

	@Override
	public boolean onSendComplexBlock(Player player, ComplexBlock block) {
	    //TODO: Check if inside zone
	    try {
	        Logger.debug("Player "+player.toString()+" received complex block "+block.toString()+" data.");
	    } catch (MissingFormatArgumentException e) {
	        Logger.debug("Player "+player.toString()+" received complex block which failed to format to text.");
	    }
        return false;
    }

	@Override
	public void onLogin(Player player) {
	    //TODO: Check if inside zone
	    Logger.debug("Player "+player.toString()+" logged in.");
	    super.onLogin(player);
    }

	@Override
	public void onPlayerMove(Player player, Location from, Location to) {
	    //TODO: Check if inside zone
//		if (result) {
//			player.teleportTo(from);
//		}
	    Logger.debug("Player "+player.toString()+" moved from "+from.toString()+" to "+to.toString()+".");
	    super.onPlayerMove(player, from, to);
	}

	@Override
    public boolean onTeleport(Player player, Location from, Location to) {
	    //TODO: Check if destination inside zone
	    Logger.debug("Player "+player.toString()+" teleported from "+from.toString()+" to "+toString()+" .");
        return false;
    }
}
