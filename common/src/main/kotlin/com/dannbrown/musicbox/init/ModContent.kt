package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.init.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.CreativeTabsUtil
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.lib.executable.FFmpeg
import com.dannbrown.musicbox.lib.executable.YoutubeDL
import com.dannbrown.musicbox.lib.main.FileSound
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import java.io.IOException
import java.net.URISyntaxException

object ModContent {
  const val MOD_ID = "musicbox"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  var playingSounds = mutableMapOf<BlockPos, FileSound>()
  var ongoingDownloads = mutableMapOf<BlockPos, Boolean>()

  val TAB = REGISTRATE.creativeTab(MOD_ID, "Music Box", { ItemStack(ModItems.CUSTOM_RECORD.get()) }, { p, o ->
    o.accept(ItemStack(Blocks.JUKEBOX))
    o.accept(ItemStack(ModItems.CUSTOM_RECORD.get()))

    // DISCS
    o.accept(MusicBoxDiscs.RICK_ROLL)
    o.accept(MusicBoxDiscs.DARUDE_SANDSTORM)
    o.accept(MusicBoxDiscs.GANGNAM_STYLE)
    o.accept(MusicBoxDiscs.ALLSTAR)
    o.accept(MusicBoxDiscs.DJBOBO)
    o.accept(MusicBoxDiscs.ROCKETMAN)
    o.accept(MusicBoxDiscs.GOLDEN_WIND)
    o.accept(MusicBoxDiscs.DANCIN1)
    o.accept(MusicBoxDiscs.DANCIN2)
    o.accept(MusicBoxDiscs.PEWDIEPIE)
    o.accept(MusicBoxDiscs.PAGODE_JAPONES)
    o.accept(MusicBoxDiscs.VUCVUC)
    o.accept(MusicBoxDiscs.VITAS)
    o.accept(MusicBoxDiscs.SWEDEN_REMIX)
    o.accept(MusicBoxDiscs.REVENGE)
    o.accept(MusicBoxDiscs.THROUGHTHENIGHT)
    o.accept(MusicBoxDiscs.DONTMINEATNIGHT)
    o.accept(MusicBoxDiscs.CREATOR)
    o.accept(MusicBoxDiscs.CREATOR_8BIT)
    o.accept(MusicBoxDiscs.FIREBUGS)
    o.accept(MusicBoxDiscs.PRECIPICE)
    o.accept(MusicBoxDiscs.STARBOUND)
    o.accept(MusicBoxDiscs.PORTAL)
    o.accept(MusicBoxDiscs.DIREDIREDOCKS)
    o.accept(MusicBoxDiscs.ARBEET)
    o.accept(MusicBoxDiscs.SHADOMAL1)
    o.accept(MusicBoxDiscs.SHADOMAL2)
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