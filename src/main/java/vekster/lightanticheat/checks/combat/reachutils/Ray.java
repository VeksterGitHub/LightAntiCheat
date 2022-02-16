package vekster.lightanticheat.checks.combat.reachutils;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

//The code for ray tracing by 567legodude

// Just a ray class I made with useful methods I needed.

public record Ray(Vector origin, Vector direction) {

    // Create a ray at the origin pointing in a direction.

    // Create a ray based on where the player is looking.
    // Origin: Player Eye Location
    // Direction: Player-looking direction
    public static Ray from(Player player) {
        return new Ray(player.getEyeLocation().toVector(), player.getLocation().getDirection());
    }

    public double origin(int i) {
        return switch (i) {
            case 0 -> origin.getX();
            case 1 -> origin.getY();
            case 2 -> origin.getZ();
            default -> 0;
        };
    }

    public double direction(int i) {
        return switch (i) {
            case 0 -> direction.getX();
            case 1 -> direction.getY();
            case 2 -> direction.getZ();
            default -> 0;
        };
    }

}