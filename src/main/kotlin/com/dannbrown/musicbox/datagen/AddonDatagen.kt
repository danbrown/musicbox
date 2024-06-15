package com.dannbrown.musicbox.datagen

import com.dannbrown.deltaboxlib.registry.datagen.DeltaboxRecipeProvider
import com.dannbrown.deltaboxlib.registry.datagen.DatagenRootInterface
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.datagen.lang.AddonLangGen
import com.dannbrown.musicbox.datagen.recipe.AddonRecipeGen
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

class AddonDatagen(output: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : DatapackBuiltinEntriesProvider(output, future, BUILDER, modIds){
  companion object: DatagenRootInterface{
    override val modIds: MutableSet<String> = mutableSetOf(
      MusicBoxModule.MOD_ID
    )
    override val BUILDER: RegistrySetBuilder = RegistrySetBuilder()

    override fun gatherData(event: GatherDataEvent) {
      val generator = event.generator
      val packOutput = generator.packOutput
      val lookupProvider = event.lookupProvider

      // Builder generators above
      generator.addProvider(event.includeServer(), AddonDatagen(packOutput, lookupProvider))
      // Langs
      AddonLangGen.addStaticLangs(event.includeClient())
      // Recipes
      DeltaboxRecipeProvider.registerGenerators(event.includeServer(), generator, AddonRecipeGen::class)
    }
  }
}