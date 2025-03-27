package com.dannbrown.musicbox.datagen

import com.dannbrown.musicbox.content.items.DiscVariant
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.google.common.base.Suppliers
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

class AddDiscModifier(val name: String, val lootTableId: ResourceLocation, val chance: Float, val item: ItemStack) {
  companion object {
    val CODEC: Supplier<Codec<AddDiscModifier>> = Suppliers.memoize {
      RecordCodecBuilder.create { inst: RecordCodecBuilder.Instance<AddDiscModifier> ->
        inst.group(
          ResourceLocation.CODEC.fieldOf("loot_table_id").forGetter { m: AddDiscModifier -> m.lootTableId },
          Codec.FLOAT.fieldOf("chance").forGetter { m: AddDiscModifier -> m.chance },
          Codec.STRING.fieldOf("url")
            .forGetter { m: AddDiscModifier -> m.item.orCreateTag.getString(URLDiscItem.URL_TAG_KEY) },
          Codec.STRING.fieldOf("name")
            .forGetter { m: AddDiscModifier -> m.item.orCreateTag.getString(URLDiscItem.NAME_TAG_KEY) },
          Codec.INT.fieldOf("variant")
            .forGetter { m: AddDiscModifier -> m.item.orCreateTag.getInt(URLDiscItem.TEXTURE_TAG_KEY) },
          Codec.INT.fieldOf("radius")
            .forGetter { m: AddDiscModifier -> m.item.orCreateTag.getInt(URLDiscItem.RADIUS_TAG_KEY) },
          Codec.INT.fieldOf("duration")
            .forGetter { m: AddDiscModifier -> m.item.orCreateTag.getInt(URLDiscItem.DURATION_TAG_KEY) },
        )
          .apply(
            inst
          ) { lootTableId, chance, url, name, variant, radius, duration ->
            AddDiscModifier(
              "",
              lootTableId, chance,
              URLDiscItem.createDiscItem(url, duration, name, DiscVariant.fromInt(variant), radius, true)
            )
          }
      }
    }
  }
}