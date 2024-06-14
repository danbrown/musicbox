package com.dannbrown.musicbox

import com.dannbrown.databoxlib.registry.generators.ItemGen
import com.dannbrown.musicbox.content.items.URLDiscItem
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.ItemTags
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object MusicBoxItems {
  val SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MusicBoxModule.MOD_ID)
  val PLACEHOLDER_SOUND = SOUNDS.register("placeholder_sound") { SoundEvent.createFixedRangeEvent(ResourceLocation(MusicBoxModule.MOD_ID, "placeholder_sound"), 0.5f) }
  val ITEMS = ItemGen(MusicBoxModule.REGISTRATE)

  val CUSTOM_RECORD = ITEMS.simpleItem("custom_record", { p -> URLDiscItem(17, PLACEHOLDER_SOUND.get(), p.stacksTo(1)) }, ItemTags.MUSIC_DISCS)

  fun register(bus: IEventBus){
    SOUNDS.register(bus)
  }
}