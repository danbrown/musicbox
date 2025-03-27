package com.dannbrown.musicbox.fabric.init

import com.dannbrown.deltaboxlib.fabric.registrate.RegistrateDatagenFabric
import com.dannbrown.musicbox.datagen.DiscLootProvider
import com.dannbrown.musicbox.datagen.ModLootDiscs
import com.dannbrown.musicbox.init.ModContent
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder

class ModContentDatagenFabric : DataGeneratorEntrypoint {
  override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
    val pack = fabricDataGenerator.createPack()
    RegistrateDatagenFabric.buildDatagenResources(pack, ModContent.REGISTRATE)
    pack.addProvider { packOutput -> DiscLootProvider(ModLootDiscs.discs, packOutput) }
  }

  override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
    RegistrateDatagenFabric.buildRegistry(registryBuilder, ModContent.REGISTRATE)
  }
}