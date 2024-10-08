package com.dannbrown.musicbox.datagen.recipe

import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.deltaboxlib.registry.datagen.recipe.DeltaboxRecipeSlice
import com.dannbrown.musicbox.MusicBoxItems
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import java.util.function.Consumer

class MusicBoxRecipeGen : DeltaboxRecipeSlice(MusicBoxModule.MOD_ID) {
  override fun name(): String {
    return "Music Box Addon Recipes"
  }
  override fun addRecipes(recipeConsumer: Consumer<FinishedRecipe>) {
    val CUSTOM_DISC = crafting(recipeConsumer, { MusicBoxItems.CUSTOM_RECORD.get() }) { b ->
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
}