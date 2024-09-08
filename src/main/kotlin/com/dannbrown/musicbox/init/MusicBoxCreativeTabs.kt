package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.registry.generators.CreativeTabGen
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.MusicBoxItems
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object MusicBoxCreativeTabs {
  val TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MusicBoxModule.MOD_ID)

  fun register(modBus: IEventBus) {
    TABS.register(modBus)
  }

  val CREATIVE_TAB_KEY = "musicbox_tab"

  val MUSICBOX_TAB: RegistryObject<CreativeModeTab> =
    CreativeTabGen(TABS, MusicBoxModule.MOD_ID).createTab(
      CREATIVE_TAB_KEY,
      { ItemStack(MusicBoxItems.CUSTOM_RECORD.get()) },
      CreativeModeTabs.SPAWN_EGGS,
      { parameters, output ->

        output.accept(ItemStack(Blocks.JUKEBOX))
        output.accept(ItemStack(MusicBoxItems.CUSTOM_RECORD.get()))

        // DISCS
        output.accept(MusicBoxDiscs.RICK_ROLL)
        output.accept(MusicBoxDiscs.DARUDE_SANDSTORM)
        output.accept(MusicBoxDiscs.GANGNAM_STYLE)
        output.accept(MusicBoxDiscs.DJBOBO)
        output.accept(MusicBoxDiscs.ROCKETMAN)
        output.accept(MusicBoxDiscs.GOLDEN_WIND)
        output.accept(MusicBoxDiscs.DANCIN1)
        output.accept(MusicBoxDiscs.DANCIN2)
        output.accept(MusicBoxDiscs.PEWDIEPIE)
        output.accept(MusicBoxDiscs.PAGODE_JAPONES)
        output.accept(MusicBoxDiscs.VUCVUC)
        output.accept(MusicBoxDiscs.VITAS)
        output.accept(MusicBoxDiscs.SWEDEN_REMIX)
        output.accept(MusicBoxDiscs.REVENGE)
        output.accept(MusicBoxDiscs.THROUGHTHENIGHT)
        output.accept(MusicBoxDiscs.DONTMINEATNIGHT)
        output.accept(MusicBoxDiscs.CREATOR)
        output.accept(MusicBoxDiscs.CREATOR_8BIT)
        output.accept(MusicBoxDiscs.FIREBUGS)
        output.accept(MusicBoxDiscs.PRECIPICE)
        output.accept(MusicBoxDiscs.STARBOUND)
        output.accept(MusicBoxDiscs.PORTAL)
      }
    )
}