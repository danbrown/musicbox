package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.init.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.CreativeTabsUtil
import com.dannbrown.musicbox.lib.executable.FFmpeg
import com.dannbrown.musicbox.lib.executable.YoutubeDL
import com.dannbrown.musicbox.lib.main.FileSound
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import java.io.IOException
import java.net.URISyntaxException

object ModContent {
  const val MOD_ID = "musicbox"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  var playingSounds = mutableMapOf<BlockPos, FileSound>()
  var ongoingDownloads = mutableMapOf<BlockPos, Boolean>()

  val TAB = REGISTRATE.creativeTab(MOD_ID, "Music Box", { ItemStack(ModItems.CUSTOM_RECORD.get()) }, { p, o ->
    CreativeTabsUtil.displayAll(
      REGISTRATE, p, o
    )
  })

  fun init() {
    ModConfig.register()
    ModSounds.register()
    ModTags.register()
    ModBlocks.register()
    ModItems.register()
    ModParticles.register()
    ModBlockEntities.register()
    ModModelLayers.register()
    ModLang.register()
    ModScreens.register()
    ModNetwork.register()
    REGISTRATE.buildRegistries()
  }

  fun initClient() {
    // Download FFmpeg and YoutubeDL if they are not already downloaded and checks for updates.
    try {
      FFmpeg.checkForExecutable()
      YoutubeDL.checkForExecutable()
    } catch (e: IOException) {
      throw RuntimeException(e)
    } catch (e: URISyntaxException) {
      throw RuntimeException(e)
    }
  }
}