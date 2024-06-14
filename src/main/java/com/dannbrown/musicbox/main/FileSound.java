package com.dannbrown.musicbox.main;


import com.dannbrown.musicbox.MusicBoxModule;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSound implements SoundInstance {
  private final String fileName;
  private final BlockPos position;
  private final int radius;

  public FileSound(String fileName, BlockPos position, int radius) {
    this.fileName = fileName;
    this.position = position;
    this.radius = radius;
  }

  @Override
  public @NotNull ResourceLocation getLocation() {
    return new ResourceLocation(MusicBoxModule.MOD_ID, "customsound/" + fileName);
  }

  @Nullable
  @Override
  public WeighedSoundEvents resolve(SoundManager soundManager) {
    return new WeighedSoundEvents(this.getLocation(), null);
  }

  @Override
  public @NotNull Sound getSound() {
    return new Sound(this.getLocation().toString(), ConstantFloat.of(this.getVolume()), ConstantFloat.of(this.getPitch()), 1, Sound.Type.SOUND_EVENT, true, false, radius);
  }

  @Override
  public @NotNull SoundSource getSource() {
    return SoundSource.BLOCKS;
  }

  @Override
  public boolean isLooping() {
    return false;
  }

  @Override
  public boolean isRelative() {
    return false;
  }

  @Override
  public int getDelay() {
    return 0;
  }

  @Override
  public float getVolume() {
    return 1;
  }

  @Override
  public float getPitch() {
    return 1;
  }

  @Override
  public double getX() {
    return position.getX();
  }

  @Override
  public double getY() {
    return position.getY();
  }

  @Override
  public double getZ() {
    return position.getZ();
  }

  @Override
  public @NotNull Attenuation getAttenuation() {
    return Attenuation.LINEAR;
  }
}