package com.dannbrown.musicbox.datagen

import com.dannbrown.deltaboxlib.registry.datagen.DatagenRootInterface
import com.dannbrown.deltaboxlib.registry.datagen.recipe.DeltaboxRecipeProvider
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.datagen.lang.MusicBoxLangGen
import com.dannbrown.musicbox.datagen.loot.MusicBoxGlobalLootModifiers
import com.dannbrown.musicbox.datagen.recipe.MusicBoxRecipeGen
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

class MusicBoxDatagen(output: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : DatapackBuiltinEntriesProvider(output, future, BUILDER, modIds){
  companion object: DatagenRootInterface{
    override val modIds: MutableSet<String> = mutableSetOf(
      MusicBoxModule.MOD_ID
    )
    override val BUILDER: RegistrySetBuilder = RegistrySetBuilder()

    override fun gatherData(event: GatherDataEvent) {
      val generator = event.generator
      val packOutput = generator.packOutput
      val lookupProvider = event.lookupProvider
      val existingFileHelper = event.existingFileHelper
      // Builder generators above
      generator.addProvider(event.includeServer(), MusicBoxDatagen(packOutput, lookupProvider))
      // Langs
      MusicBoxLangGen.addStaticLangs(event.includeClient())
      // Recipes
      generator.addProvider(event.includeServer(), DeltaboxRecipeProvider(packOutput, listOf(MusicBoxRecipeGen())))
      // Global loot modifiers
      generator.addProvider(event.includeServer(), MusicBoxGlobalLootModifiers(packOutput))
    }
  }
}