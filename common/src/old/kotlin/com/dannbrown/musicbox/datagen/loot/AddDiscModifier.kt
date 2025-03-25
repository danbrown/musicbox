package com.dannbrown.musicbox.datagen.loot

import com.dannbrown.musicbox.content.items.DiscVariant
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.google.common.base.Suppliers
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.common.loot.LootModifier
import java.util.function.Supplier

class AddDiscModifier(conditionsIn: Array<LootItemCondition>, private val item: ItemStack) : LootModifier(conditionsIn) {
  override fun doApply(generatedLoot: ObjectArrayList<ItemStack>, context: LootContext): ObjectArrayList<ItemStack> {
    for (condition in this.conditions) {
      if (!condition.test(context)) {
        return generatedLoot
      }
    }

    generatedLoot.add(this.item)

    return generatedLoot
  }

  override fun codec(): Codec<out IGlobalLootModifier> {
    return CODEC.get()
  }

  companion object {
    val CODEC: Supplier<Codec<AddDiscModifier>> = Suppliers.memoize {
      RecordCodecBuilder.create { inst: RecordCodecBuilder.Instance<AddDiscModifier> ->
        codecStart(inst)
          .and(Codec.STRING.fieldOf("url").forGetter { m: AddDiscModifier -> m.item.orCreateTag.getString(URLDiscItem.URL_TAG_KEY) })
          .and(Codec.STRING.fieldOf("name").forGetter { m: AddDiscModifier -> m.item.orCreateTag.getString(URLDiscItem.NAME_TAG_KEY) })
          .and(Codec.INT.fieldOf("variant").forGetter { m: AddDiscModifier -> m.item.orCreateTag.getInt(URLDiscItem.TEXTURE_TAG_KEY) })
          .and(Codec.INT.fieldOf("radius").forGetter { m: AddDiscModifier -> m.item.orCreateTag.getInt(URLDiscItem.RADIUS_TAG_KEY) })
          .and(Codec.INT.fieldOf("duration").forGetter { m: AddDiscModifier -> m.item.orCreateTag.getInt(URLDiscItem.DURATION_TAG_KEY) })
          .apply(inst
          ) { conditionsIn: Array<LootItemCondition>, url: String, name: String, variant: Int, radius: Int, duration: Int ->
            AddDiscModifier(conditionsIn, URLDiscItem.createDiscItem(url, duration, name, DiscVariant.fromInt(variant), radius, true))
          }
      }
    }
  }
}