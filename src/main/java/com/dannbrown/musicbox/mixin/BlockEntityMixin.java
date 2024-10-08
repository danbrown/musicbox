package com.dannbrown.musicbox.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = {BlockEntity.class})
public class BlockEntityMixin {
  @Shadow
  protected Level level;
  @Final
  @Shadow
  protected BlockPos worldPosition;
  @Shadow
  public boolean hasLevel() {
    return this.level != null;
  }
}