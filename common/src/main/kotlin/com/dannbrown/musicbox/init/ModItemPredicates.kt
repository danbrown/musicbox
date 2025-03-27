package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.content.items.URLDiscItem
import dev.architectury.registry.item.ItemPropertiesRegistry
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

object ModItemPredicates {
  fun init() {
    ItemPropertiesRegistry.register(
      ModItems.CUSTOM_RECORD.get(),
      DeltaboxUtil.resourceLocation(ModContent.MOD_ID, "disc_variant"),
      { itemStack: ItemStack, clientLevel: ClientLevel?, livingEntity: LivingEntity?, i: Int ->
        if (itemStack.item is URLDiscItem) {
          val itemTexture = itemStack.orCreateTag.getInt(URLDiscItem.TEXTURE_TAG_KEY)
          val variant = String.format("%.2f", (itemTexture.toDouble() / 100)).toFloat()
          return@register variant
        }
        return@register 0f
      }
    )
  }
}