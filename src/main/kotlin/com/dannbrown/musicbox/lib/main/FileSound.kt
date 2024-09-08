package com.dannbrown.musicbox.lib.main

import com.dannbrown.musicbox.MusicBoxModule
import net.minecraft.client.resources.sounds.Sound
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.client.sounds.SoundManager
import net.minecraft.client.sounds.WeighedSoundEvents
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
import net.minecraft.util.valueproviders.ConstantFloat

class FileSound(private val fileName: String, private val position: BlockPos, private val radius: Int) : SoundInstance {
  override fun getLocation(): ResourceLocation {
    return ResourceLocation(MusicBoxModule.MOD_ID, "customsound/$fileName")
  }

  override fun resolve(soundManager: SoundManager): WeighedSoundEvents? {
    return WeighedSoundEvents(this.location, null)
  }

  override fun getSound(): Sound {
    return Sound(this.location.toString(), ConstantFloat.of(this.volume), ConstantFloat.of(this.pitch), 1, Sound.Type.SOUND_EVENT, true, false, radius)
  }

  override fun getSource(): SoundSource {
    return SoundSource.BLOCKS
  }

  override fun isLooping(): Boolean {
    return false
  }

  override fun isRelative(): Boolean {
    return false
  }

  override fun getDelay(): Int {
    return 0
  }

  override fun getVolume(): Float {
    return 1.0f
  }

  override fun getPitch(): Float {
    return 1.0f
  }

  override fun getX(): Double {
    return position.x
      .toDouble()
  }

  override fun getY(): Double {
    return position.y
      .toDouble()
  }

  override fun getZ(): Double {
    return position.z
      .toDouble()
  }

  override fun getAttenuation(): SoundInstance.Attenuation {
    return SoundInstance.Attenuation.LINEAR
  }
}