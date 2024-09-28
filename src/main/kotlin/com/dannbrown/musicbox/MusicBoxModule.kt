package com.dannbrown.musicbox

import com.dannbrown.deltaboxlib.registry.DeltaboxRegistrate
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import com.dannbrown.musicbox.datagen.MusicBoxDatagen
import com.dannbrown.musicbox.init.MusicBoxCommands
import com.dannbrown.musicbox.init.MusicBoxCreativeTabs
import com.dannbrown.musicbox.init.MusicBoxLootModifiers
import com.dannbrown.musicbox.init.MusicBoxScreens
import com.dannbrown.musicbox.lib.executable.FFmpeg
import com.dannbrown.musicbox.lib.executable.YoutubeDL
import com.dannbrown.musicbox.lib.main.FileSound
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.core.BlockPos
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.DIST
import java.io.IOException
import java.net.URISyntaxException

@Mod(MusicBoxModule.MOD_ID)
class MusicBoxModule {
  companion object {
    const val MOD_ID = "musicbox"
    const val NAME = "Music Box"
    val LOGGER = LogManager.getLogger()
    val REGISTRATE = DeltaboxRegistrate(MOD_ID)

    var playingSounds = mutableMapOf<BlockPos, FileSound>()
    var ongoingDownloads = mutableMapOf<BlockPos, Boolean>()

    // mod compatibility
    fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
      LOGGER.info("$MOD_ID has started!")
      MusicBoxCreativeTabs.register(modBus)
      MusicBoxItems.register(modBus)
      MusicBoxScreens.register(modBus)
      MusicBoxLootModifiers.register(modBus)

      REGISTRATE.registerEventListeners(modBus)
      forgeEventBus.addListener(EventPriority.HIGH) { event: RegisterCommandsEvent -> onRegisterCommands(event) }
      modBus.addListener(::commonSetup)
      modBus.addListener(EventPriority.LOWEST) { event: GatherDataEvent -> MusicBoxDatagen.gatherData(event) }
    }

    fun registerClient(modBus: IEventBus, forgeEventBus: IEventBus) {
      modBus.addListener(::clientSetup)

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

    // RUN SETUP
    private fun commonSetup(event: FMLCommonSetupEvent) {
      MusicBoxNetworking.register()
    }

    // Run Client Setup
    private fun clientSetup(event: FMLClientSetupEvent) {
      MenuScreens.register(MusicBoxScreens.MUSIC_DISC_MENU.get()) { menu, inv, c -> MusicDiscScreen(menu, inv) }
      MusicBoxItems.addDiscPredicate(event)
    }

    fun onRegisterCommands(event: RegisterCommandsEvent) {
      MusicBoxCommands.register(event.dispatcher)
    }
  }

  init {
    val modBus = FMLJavaModLoadingContext.get().modEventBus
    val forgeEventBus = MinecraftForge.EVENT_BUS
    register(modBus, forgeEventBus)
    // client
    if (DIST.isClient) {
      // register main mod client content
      registerClient(modBus, forgeEventBus)
    }
  }
}
