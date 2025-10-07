package com.nort8985.spidersensepro.mixin;

import com.nort8985.spidersensepro.MonsterHighlighter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "hasOutline", at = @At("RETURN"), cancellable = true)
    void outline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (MonsterHighlighter.entities.contains(entity)) cir.setReturnValue(true);
    }
}
