package com.dannbrown.musicbox.lib.main

import com.dannbrown.musicbox.MusicBoxModule
import net.minecraft.client.Minecraft
import java.nio.file.Path

object Manager {
  var PATH: Path? = if (Minecraft.getInstance() != null) Minecraft.getInstance().gameDirectory.toPath().resolve(MusicBoxModule.MOD_ID)
  else null
  var AUDIO_BIT_RATE: Choices = Choices.MEDIUM // TODO: add this to a mod config file
  var MAX_AUDIO_MINUTES: Int = 60 // TODO: add this to a mod config file
  var DO_CHECK_UPDATES_ON_START: Boolean = true // TODO: add this to a mod config file

  enum class Choices(val value: String) {
    LOW("48K"),
    MEDIUM("96K"),
    HIGH("128K")
  }
}
