package vekster.lightanticheat.checks.combat.utils;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

//The code for ray tracing by 567legodude

// Just an AABB class I made with some useful methods I needed.
// Mainly for fast Ray-AABB collision detection.

public class AABB {

    private final Vector min;
    private final Vector max; // min/max locations

    private AABB(Entity entity) {
        this.min = getMin(entity);
        this.max = getMax(entity);
    }

    private Vector getMin(Entity entity) {
        double halfOfWidth = entity.getWidth() / 2.0D;
        return entity.getLocation().toVector().add(new Vector(-halfOfWidth, 0, -halfOfWidth));
    }

    private Vector getMax(Entity entity) {
        double height = entity.getHeight();
        double halfOfWidth = entity.getWidth() / 2.0D;
        return entity.getLocation().toVector().add(new Vector(halfOfWidth, height, halfOfWidth));
    }

    // Create an AABB based on a player's hitbox
    public static AABB from(Entity entity) {
        return new AABB(entity);
    }

    // Returns minimum x, y, or z point from inputs 0, 1, or 2.
    public double min(int i) {
        return switch (i) {
            case 0 -> min.getX();
            case 1 -> min.getY();
            case 2 -> min.getZ();
            default -> 0;
        };
    }

    // Returns maximum x, y, or z point from inputs 0, 1, or 2.
    public double max(int i) {
        return switch (i) {
            case 0 -> max.getX();
            case 1 -> max.getY();
            case 2 -> max.getZ();
            default -> 0;
        };
    }

    // Same as other collides method, but returns the distance of the nearest
    // point of collision of the ray and box, or -1 if no collision.
    public double collidesD(Ray ray, double tmin, double tmax) {
        for (int i = 0; i < 3; i++) {
            double d = 1 / ray.direction(i);
            double t0 = (min(i) - ray.origin(i)) * d;
            double t1 = (max(i) - ray.origin(i)) * d;
            if (d < 0) {
                double t = t0;
                t0 = t1;
                t1 = t;
            }
            tmin = Math.max(t0, tmin);
            tmax = Math.min(t1, tmax);
            if (tmax <= tmin) return -1;
        }
        return tmin;
    }

}