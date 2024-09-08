package com.dannbrown.musicbox

import com.dannbrown.deltaboxlib.registry.generators.ItemGen
import com.dannbrown.musicbox.content.items.URLDiscItem
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object MusicBoxItems {
  val SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MusicBoxModule.MOD_ID)
  val PLACEHOLDER_SOUND = SOUNDS.register("placeholder_sound") { SoundEvent.createFixedRangeEvent(ResourceLocation(MusicBoxModule.MOD_ID, "placeholder_sound"), 0.5f) }
  val ITEMS = ItemGen(MusicBoxModule.REGISTRATE)

 val MAX_VARIANTS = 27;

  val CUSTOM_RECORD = ITEMS.registrate.item<URLDiscItem>("custom_record") { p -> URLDiscItem(17, PLACEHOLDER_SOUND.get(), p.stacksTo(1)) }
    .tag(ItemTags.MUSIC_DISCS)
    .model { c, p ->
      val model = p.withExistingParent(c.name, p.mcLoc("item/generated"))
        .texture("layer0", p.modLoc("item/custom_record"))
      for(i in 0 until MAX_VARIANTS+1){
        model.override()
          .model(p.getExistingFile(p.modLoc("item/custom_record_${i}")))
          .predicate(p.modLoc("disc_variant"), 1.0f + (i.toFloat() / 100))
          .end()
      }
    }
    .register()

  fun register(bus: IEventBus){
    SOUNDS.register(bus)
  }

  fun addDiscPredicate(event: FMLClientSetupEvent) {
    ItemProperties.register(CUSTOM_RECORD.get(), ResourceLocation("${MusicBoxModule.MOD_ID}:disc_variant"))
    { stack: ItemStack, world: ClientLevel?, entity: LivingEntity?, i: Int ->

      val itemName = stack.orCreateTag.getString(URLDiscItem.NAME_TAG_KEY)
      val key = getVariantNumber(itemName).toString()
      val variant = 1.0f + (key.toFloat() / 100)
      return@register variant ?: 1.0f
    }
  }

  private fun getVariantNumber(name: String): Int {
    if(name.isEmpty()) return 0
    // transform the name into a number between 1 and max
    val maxVariant = MAX_VARIANTS - 1
    val hash = name.hashCode()
    return Math.floorMod(hash, maxVariant) + 1
  }
}