package com.nort8985.spidersensepro.mixin;

import com.nort8985.spidersensepro.MonsterHighlighter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow public MinecraftClient client;

    @Inject(method = "tick", at = @At("HEAD"))
    void highlight(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (MonsterHighlighter.duration <= 0) {
            MonsterHighlighter.list = new ArrayList<>();
            MonsterHighlighter.duration = -1;
        } else MonsterHighlighter.duration--;

        World world = client.world;
        if (world != null && client.interactionManager.getCurrentGameMode() == GameMode.SURVIVAL) {
            var pos = new Vec3d(player.getX(), player.getY(), player.getZ());
            var box = new Box(pos.getX() - 16, pos.getY() - 8, pos.getZ() - 16, pos.getX() + 16, pos.getY() + 8, pos.getZ() + 16);
            List<MobEntity> entities;

            boolean shiftPressed = GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS ||
                                   GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

            MonsterHighlighter.shiftMode = shiftPressed;

            if (shiftPressed) {
                entities = world.getEntitiesByClass(MobEntity.class, box, e -> e instanceof HostileEntity || e instanceof SlimeEntity);
            } else {
                entities = world.getEntitiesByClass(MobEntity.class, box, e -> (e instanceof HostileEntity && ((HostileEntity) e).canSee(player)) || (e instanceof SlimeEntity && e.canSee(player)));
            }

            if (!entities.isEmpty()) {
                MonsterHighlighter.duration = 20;
                MonsterHighlighter.list = entities;
            }
        }
    }
}
