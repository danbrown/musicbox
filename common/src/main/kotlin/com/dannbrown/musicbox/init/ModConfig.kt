package com.dannbrown.musicbox.init

import com.dannbrown.musicbox.init.ModContent.REGISTRATE

object ModConfig {
  val MAX_DISC_RADIUS =
    REGISTRATE.configInt("max_disc_radius", 999, "The maximum radius of a custom music disc in blocks. Default is 999.")

  fun register() {
    REGISTRATE.freezeConfig()
  }
}