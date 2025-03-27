package com.dannbrown.musicbox.init

import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.init.ModContent.REGISTRATE
import net.minecraft.tags.ItemTags

object ModItems {

  val CUSTOM_RECORD =
    REGISTRATE.item<URLDiscItem>("custom_record")
      .factory { p -> URLDiscItem(17, ModSounds.PLACEHOLDER_SOUND.get(), p.stacksTo(1)) }
      .itemTags(ItemTags.MUSIC_DISCS)
//      .model { c, p ->
//        val model =
//          p.withExistingParent(c.name, p.mcLoc("item/generated")).texture("layer0", p.modLoc("item/custom_record"))
//        for (i in 0 until DiscVariant.maxVariants() + 1) {
//          model.override()
//            .model(
//              p.withExistingParent("custom_record_${i}", p.mcLoc("item/generated"))
//                .texture("layer0", p.modLoc("item/custom_record${if (i == 0) "" else "_$i"}"))
//            )
//            .predicate(p.modLoc("disc_variant"), 1.0f + (i.toFloat() / 100))
//            .end()
//        }
//      }
      .register()

  fun register() {
    // init
  }
}