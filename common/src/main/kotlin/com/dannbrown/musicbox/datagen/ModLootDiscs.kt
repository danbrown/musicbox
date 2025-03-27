package com.dannbrown.musicbox.datagen

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.init.MusicBoxDiscs

object ModLootDiscs {
  val discs: MutableList<AddDiscModifier> = mutableListOf(
    AddDiscModifier(
      "darude_sandstorm",
      DeltaboxUtil.resourceLocation("minecraft", "chests/jungle_temple"),
      0.5f,
      MusicBoxDiscs.DARUDE_SANDSTORM
    )
  )
}