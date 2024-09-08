package com.dannbrown.musicbox.init

import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.datagen.loot.AddDiscModifier
import com.mojang.serialization.Codec
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object MusicBoxLootModifiers {
  val LOOT_MODIFIER_SERIALIZERS: DeferredRegister<Codec<out IGlobalLootModifier?>> = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MusicBoxModule.MOD_ID)

  val ADD_ITEM: RegistryObject<Codec<out IGlobalLootModifier?>> = LOOT_MODIFIER_SERIALIZERS.register("add_disc_loot", AddDiscModifier.CODEC)

  fun register(eventBus: IEventBus) {
    LOOT_MODIFIER_SERIALIZERS.register(eventBus)
  }
}