package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateModelTemplates
import com.dannbrown.deltaboxlib.registrate.util.DataIngredient
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.content.items.DiscVariant
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.init.ModContent.REGISTRATE
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import java.util.function.Supplier

object ModItems {

  val CUSTOM_RECORD =
    REGISTRATE.item<URLDiscItem>("custom_record")
      .factory { p -> URLDiscItem(17, ModSounds.PLACEHOLDER_SOUND.get(), p.stacksTo(1)) }
      .itemTags(ItemTags.MUSIC_DISCS)
      .recipe { c, p ->
        c.simpleShapedRecipe(
          { p.get() }, arrayOf(" G ", "GDG", " GF"), mapOf(
            'G' to Supplier { DataIngredient(Blocks.REDSTONE_BLOCK) },
            'D' to Supplier { DataIngredient(ItemTags.MUSIC_DISCS) },
            'F' to Supplier { DataIngredient(Items.FEATHER) }
          ), 1)
      }
      .model { c, p ->

        fun generateDiscVariants(
          resourceLocation: ResourceLocation,
          map: Map<TextureSlot, ResourceLocation>
        ): JsonObject {
          val jsonObject = RegistrateModelTemplates.FLAT_ITEM.createBaseTemplate(resourceLocation, map)
          val jsonArray = JsonArray()

          for (i in 0 until DiscVariant.maxVariants() + 1) {
            val variantNumber = String.format("%.2f", (i.toDouble() / 100)).toDouble()
            val jsonObject2 = JsonObject()
            val jsonObject3 = JsonObject()
            jsonObject3.addProperty(
              DeltaboxUtil.resourceLocation(ModContent.MOD_ID, "disc_variant").toString(),
              variantNumber
            )
            jsonObject2.add("predicate", jsonObject3)
            jsonObject2.addProperty(
              "model",
              DeltaboxUtil.resourceLocation(ModContent.MOD_ID, "item/custom_record_$i")
                .toString()
            )
            jsonArray.add(jsonObject2)
          }

          jsonObject.add("overrides", jsonArray)
          return jsonObject
        }

// This generate the main disc with variants predicate, its not needed to run everytime
//        RegistrateModelTemplates.FLAT_ITEM.create(
//          BuiltInRegistries.ITEM.getKey(p.get()).withPrefix("item/"),
//          TextureMapping().put(TextureSlot.LAYER0, c.optionalTexture(p.get(), "", "", "item/")),
//          c.asOutput(), ::generateDiscVariants
//        )

        for (i in 0 until DiscVariant.maxVariants() + 1) {
          RegistrateModelTemplates.FLAT_ITEM.create(
            DeltaboxUtil.resourceLocation(ModContent.MOD_ID, "item/custom_record_$i"),
            TextureMapping().put(
              TextureSlot.LAYER0,
              c.optionalTexture(p.get(), "custom_record${if (i == 0) "" else "_$i"}", "", "item/")
            ),
            c.asOutput()
          )
        }
      }
      .register()

  fun register() {
    // init
  }
}