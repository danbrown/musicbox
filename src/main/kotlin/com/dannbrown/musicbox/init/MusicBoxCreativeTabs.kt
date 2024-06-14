package com.dannbrown.musicbox.init

import com.dannbrown.databoxlib.registry.generators.CreativeTabGen
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.MusicBoxItems
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object MusicBoxCreativeTabs {
  val TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MusicBoxModule.MOD_ID)

  fun register(modBus: IEventBus) {
    TABS.register(modBus)
  }

  val CREATIVE_TAB_KEY = "creative_tab"
  val CREATIVE_TAB: RegistryObject<CreativeModeTab> =
    CreativeTabGen(TABS, MusicBoxModule.MOD_ID).createTab(
      CREATIVE_TAB_KEY,
      { ItemStack(MusicBoxItems.CUSTOM_RECORD.get()) },
      CreativeModeTabs.SPAWN_EGGS,
      { parameters, output ->
        CreativeTabGen.displayAllRegistrate(MusicBoxModule.REGISTRATE, parameters, output)
      }
    )
}