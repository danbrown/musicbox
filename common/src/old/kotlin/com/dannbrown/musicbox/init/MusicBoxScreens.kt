package com.dannbrown.musicbox.init

import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.content.gui.MusicDiscMenu
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object MusicBoxScreens {
  private val SCREENS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MusicBoxModule.MOD_ID)

  val MUSIC_DISC_MENU = registerMenu("music_disc_menu") { id, inv, buff -> MusicDiscMenu(id) }

  // Helper method to register a menu type
  private fun <T : AbstractContainerMenu> registerMenu(name: String, factory: IContainerFactory<T>): RegistryObject<MenuType<T>> {
    return SCREENS.register(name) { IForgeMenuType.create(factory) }
  }

  fun register(modBus: IEventBus) {
    SCREENS.register(modBus)
  }
}