package com.dannbrown.musicbox.datagen.recipe

import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.deltaboxlib.registry.datagen.DeltaboxRecipeProvider
import com.dannbrown.musicbox.MusicBoxItems
import net.minecraft.data.DataGenerator
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks

class AddonRecipeGen(generator: DataGenerator) : DeltaboxRecipeProvider(generator.packOutput, MusicBoxModule.MOD_ID) {
  val CUSTOM_DISC = crafting({ MusicBoxItems.CUSTOM_RECORD.get() }) { b ->
    b
      .shaped(1) { c ->
        c
          .pattern(" G ")
          .pattern("GDG")
          .pattern(" GF")
          .define('G', Blocks.REDSTONE_BLOCK)
          .define('D', ItemTags.MUSIC_DISCS)
          .define('F', Items.FEATHER)
      }
  }
}