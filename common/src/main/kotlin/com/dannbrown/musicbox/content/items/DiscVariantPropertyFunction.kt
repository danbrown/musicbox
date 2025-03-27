package com.dannbrown.musicbox.content.items

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class DiscVariantPropertyFunction : ClampedItemPropertyFunction {
  override fun unclampedCall(
    itemStack: ItemStack,
    clientLevel: ClientLevel?,
    livingEntity: LivingEntity?,
    i: Int
  ): Float {
    if (itemStack.item is URLDiscItem) {
      val itemTexture = itemStack.orCreateTag.getInt(URLDiscItem.TEXTURE_TAG_KEY)
      val variant = String.format("%.2f", 1.0 + (itemTexture.toDouble() / 100)).toFloat()
      return variant
    }
    return 0f
  }
}