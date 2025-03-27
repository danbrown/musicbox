package com.dannbrown.musicbox.datagen

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.init.ModContent
import com.mojang.serialization.JsonOps
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class DiscLootProvider(
  private val discs: List<AddDiscModifier>,
  private val packOutput: PackOutput
) : DataProvider {
  private val pathProvider =
    packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "loot_modifiers")

  override fun getName(): String = "Disc Loot Datagen for: ${ModContent.MOD_ID}"

  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val futures: MutableList<CompletableFuture<*>> = ArrayList()
    for (disc in discs) {
      val discName =
        "add_${DeltaboxUtil.asId(disc.name)}_to_${DeltaboxUtil.asId(disc.lootTableId.path)}"
      val discPath = pathProvider.json(DeltaboxUtil.resourceLocation(ModContent.MOD_ID, discName))
      futures.add(saveDiscData(cachedOutput, discPath, disc))
    }
    return CompletableFuture.allOf(*futures.toTypedArray())
  }

  private fun saveDiscData(cachedOutput: CachedOutput, path: Path, disc: AddDiscModifier): CompletableFuture<*> {
    return try {
      val jsonObject = AddDiscModifier.CODEC.get()
        .encodeStart(JsonOps.INSTANCE, disc)
        .getOrThrow(false) {}
        .asJsonObject
      DataProvider.saveStable(cachedOutput, jsonObject, path)
    } catch (ioException: IOException) {
      DeltaboxUtil.LOGGER.error("Couldn't save disc loot at {}", path, ioException)
      throw ioException
    }
  }
}
