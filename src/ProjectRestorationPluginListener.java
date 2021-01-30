public class ProjectRestorationPluginListener extends PluginListener {
	boolean _DEBUG = false;

	private ProjectRestorationPlugin parent = null;

	public ProjectRestorationPluginListener(ProjectRestorationPlugin parent) {
		this.parent = parent;
	}

	/*=======================================*
	 * Internal minecraft block interactions *
	 *=======================================*/

	@Override
	public boolean onExplode(Block block) {
	    //TODO: Check if inside zone
	    log("Block "+block.toString()+" exploded.");
	    return false;
    }

	@Override
	public boolean onFlow(Block blockFrom, Block blockTo) {
	    //TODO: Check if inside zone
	    log("Block "+blockFrom.toString()+" flew to "+blockTo.toString()+".");
        return false;
    }

	@Override
	public boolean onBlockPhysics(Block block, boolean placed) {
	    //TODO: Check if inside zone
	    log("Block "+block.toString()+", placed:"+placed+" physics updated.");
        return false;
    }

	@Override
	public PluginLoader.HookResult onLiquidDestroy( PluginLoader.HookResult currentState, int liquidBlockId, Block targetBlock )  {
	    //TODO: Check if inside zone
	    log("Liquid with id:"+liquidBlockId+" destroyed block "+targetBlock.toString()+".");
        return PluginLoader.HookResult.DEFAULT_ACTION;
    }

	/*=============================*
     * Player - block interactions *
     *=============================*/

	@Override
	public boolean onBlockPlace(Player player, Block blockPlaced, Block blockClicked, Item itemInHand) {
	    //TODO: Check if inside zone
	    log("Player "+player.toString()+" placed block "+blockPlaced.toString()+" on block "+blockClicked.toString()+" with item "+itemInHand.toString()+" in hand.");
        return false;
    }

	@Override
	public boolean onBlockDestroy(Player player, Block block) {
	    //TODO: Check if inside zone
	    log("Player "+player.toString()+" destoryed block "+block.toString()+".");
	    return false;
	}

	@Override
	public boolean onBlockBreak(Player player, Block block) {
	    //TODO: Check if inside zone
	    log("Player "+player.toString()+" broke block "+block.toString()+".");
        return false;
    }

	@Override
	public void onBlockRightClicked(Player player, Block blockClicked, Item item) {
	    //TODO: Check if inside zone
	    log("Player "+player.toString()+" right-clicked block "+blockClicked.toString()+" with item "+item.toString()+" in hand.");
	    super.onBlockRightClicked(player, blockClicked, item);
    }

	@Override
	public boolean onIgnite(Block block, Player player) {
	    //TODO: Check if inside zone
	    log("Player "+player.toString()+" ignited block "+block.toString()+".");
        return false;
    }

	@Override
	public boolean onComplexBlockChange(Player player, ComplexBlock block) {
	    //TODO: Check if inside zone
	    log("Player "+player.toString()+" interacted with complex block "+block.toString()+".");
        return false;
    }

	@Override
	public boolean onSendComplexBlock(Player player, ComplexBlock block) {
	    //TODO: Check if inside zone
	    log("Player "+player.toString()+" received complex block "+block.toString()+" data.");
        return false;
    }

	@Override
	public void onLogin(Player player) {
	    //TODO: Check if inside zone
	    log("Player "+player.toString()+" logged in.");
	    super.onLogin(player);
    }

	@Override
	public void onPlayerMove(Player player, Location from, Location to) {
	    //TODO: Check if inside zone
//		if (result) {
//			player.teleportTo(from);
//		}
	    log("Player "+player.toString()+" moved from "+from.toString()+" to "+to.toString()+".");
	    super.onPlayerMove(player, from, to);
	}

	@Override
    public boolean onTeleport(Player player, Location from, Location to) {
	    //TODO: Check if destination inside zone
	    log("Player "+player.toString()+" teleported from "+from.toString()+" to "+toString()+" .");
        return false;
    }

	/**
     * Log a message
     *
     * @param str the string to log
     */
    public void log(String str) {
        System.out.println("[" + parent.getName() + "] " + parent.getVersion() + " " + str);
    }
	
}
