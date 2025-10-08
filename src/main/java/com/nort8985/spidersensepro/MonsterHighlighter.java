package com.nort8985.spidersensepro;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;

public class MonsterHighlighter {
    public static int duration = -1;
    public static List<Entity> entities = new ArrayList<>();
    public static boolean shiftMode = false;
    public static boolean ctrlMode = false;

    // For HUD overlay
    public static Entity closestMob = null;
    public static double closestDistance = 0;

    public static void updateClosestMob(PlayerEntity player) {
        closestMob = null;
        closestDistance = 0;
        double minDistance = Double.MAX_VALUE;

        for (Entity entity : entities) {
            if (entity instanceof MobEntity && entity.isAlive()) {
                double distance = Math.sqrt(Math.pow(player.getX() - entity.getX(), 2) + Math.pow(player.getZ() - entity.getZ(), 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestMob = entity;
                    closestDistance = distance;
                }
            }
        }

        // Clear if no valid mobs
        if (closestMob == null || minDistance > 32) {
            closestMob = null;
        }
    }
}
