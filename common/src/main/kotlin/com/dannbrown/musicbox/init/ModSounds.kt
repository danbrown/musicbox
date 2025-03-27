package com.dannbrown.musicbox.init

import com.dannbrown.musicbox.init.ModContent.REGISTRATE

object ModSounds {
  val PLACEHOLDER_SOUND = REGISTRATE.soundEvent("placeholder_sound", 1, 0.5f)

  fun register() {
    REGISTRATE.buildSounds()
  }
}