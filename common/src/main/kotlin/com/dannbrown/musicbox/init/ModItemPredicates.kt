package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.content.items.DiscVariantPropertyFunction
import net.minecraft.client.renderer.item.ItemProperties

object ModItemPredicates {
  init {
    ItemProperties.register(
      ModItems.CUSTOM_RECORD.get(),
      DeltaboxUtil.resourceLocation(ModContent.MOD_ID, "disc_variant"),
      DiscVariantPropertyFunction()
    )
  }

  fun register() {
    // init
  }
}