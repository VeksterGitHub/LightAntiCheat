package vekster.lightanticheat.checks.interaction;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.PlayerInventory;
import vekster.lightanticheat.extra.CheckTypes;
import vekster.lightanticheat.players.LACPlayer;
import vekster.lightanticheat.players.Violations;
import vekster.lightanticheat.usage.Config;

public class BlockPlacementCheck implements Listener {

    private static final BlockFace[] BLOCK_FACES = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST};

    private static boolean isAirPlace(Block block) {
        for (BlockFace blockFace : BLOCK_FACES) {
            if (!block.getRelative(blockFace).isEmpty() && !block.getRelative(blockFace).isLiquid())
                return false;
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void placementCheck(BlockPlaceEvent event) {

        if (!Config.irregularPlacement)
            return;

        Player player = event.getPlayer();
        LACPlayer lacPlayer = LACPlayer.get(player);
        long time = System.currentTimeMillis();

        if (lacPlayer == null || lacPlayer.isBypass || time - lacPlayer.joinTime <= Config.disablerTimeOnJoin ||
                Config.punishmentCommand == null || lacPlayer.isApiBypass || time - lacPlayer.lastQuickBreakTime < 1000)
            return;

        //IrregularPlacementB (the placement is not possible)
        if (Config.irregularPlacementB) {
            if (player.isBlocking() || player.isDead() || player.isSleeping())
                Violations.interactViolation(player, CheckTypes.IRREGULAR_PLACEMENT_B_0, lacPlayer);
        }

        //IrregularPlacementA  (block placement in the air/liquid)
        Block block = event.getBlock();
        Material material = block.getType();
        PlayerInventory playerInventory = player.getInventory();

        if (Config.irregularPlacementA)
            return;
        if (material.name().contains("LILY_PAD") || playerInventory.getItemInMainHand().getType().name().contains("AXE") ||
                playerInventory.getItemInOffHand().getType().name().contains("AXE"))
            return;
        if (!isAirPlace(block))
            return;

        if (material != Material.SAND && material != Material.RED_SAND &&
                material != Material.GRAVEL && !material.name().contains("CONCRETE_POWDER"))
            Violations.accurateViolation(player, CheckTypes.IRREGULAR_PLACEMENT_A_0, lacPlayer);
        else
            Violations.interactViolation(player, CheckTypes.IRREGULAR_PLACEMENT_A_0, lacPlayer);
    }

}
