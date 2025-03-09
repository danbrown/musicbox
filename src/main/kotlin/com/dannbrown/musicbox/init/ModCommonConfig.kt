package com.dannbrown.musicbox.init

import net.minecraftforge.common.ForgeConfigSpec

object ModCommonConfig {
  val BUILDER: ForgeConfigSpec.Builder = ForgeConfigSpec.Builder()
  var SPEC: ForgeConfigSpec? = null

  var MAX_DISC_RADIUS: ForgeConfigSpec.ConfigValue<Int>? = null

  init {
    MAX_DISC_RADIUS = BUILDER.comment("The maximum radius of a custom music disc in blocks. Default is 999.")
      .define("MaxDiscRadius", 999)

    SPEC = BUILDER.build();
  }
}