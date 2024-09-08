package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.registry.generators.CreativeTabGen
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.MusicBoxItems
import com.dannbrown.musicbox.content.items.URLDiscItem
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object MusicBoxCreativeTabs {
  val TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MusicBoxModule.MOD_ID)

  fun register(modBus: IEventBus) {
    TABS.register(modBus)
  }

  val CREATIVE_TAB_KEY = "musicbox_tab"

  val MUSICBOX_TAB: RegistryObject<CreativeModeTab> =
    CreativeTabGen(TABS, MusicBoxModule.MOD_ID).createTab(
      CREATIVE_TAB_KEY,
      { ItemStack(MusicBoxItems.CUSTOM_RECORD.get()) },
      CreativeModeTabs.SPAWN_EGGS,
      { parameters, output ->
        fun addSongRecord(url: String, duration: Int, name: String, variant: URLDiscItem.DiscVariant = URLDiscItem.DiscVariant.RED) {
          val item = ItemStack(MusicBoxItems.CUSTOM_RECORD.get())
          item.orCreateTag.putString(URLDiscItem.URL_TAG_KEY, url)
          item.orCreateTag.putInt(URLDiscItem.DURATION_TAG_KEY, duration)
          item.orCreateTag.putString(URLDiscItem.NAME_TAG_KEY, name)
          item.orCreateTag.putInt(URLDiscItem.RADIUS_TAG_KEY, 30)
          item.orCreateTag.putBoolean(URLDiscItem.LOCKED_TAG_KEY, false)
          item.orCreateTag.putInt(URLDiscItem.TEXTURE_TAG_KEY, variant.toInt())
          item.hoverName = Component.literal(name)
          output.accept(item)
        }
        output.accept(ItemStack(Blocks.JUKEBOX))
        output.accept(ItemStack(MusicBoxItems.CUSTOM_RECORD.get()))
        addSongRecord("https://www.youtube.com/watch?v=cvh0nX08nRw", 601, "Rick Astley - Never Gonna Give You Up", URLDiscItem.DiscVariant.DIAMOND)
        addSongRecord("https://www.youtube.com/watch?v=y6120QOlsfU", 232, "Darude - Sandstorm", URLDiscItem.DiscVariant.NAUTILUS)
        addSongRecord("https://www.youtube.com/watch?v=9bZkp7q19f0", 252, "PSY - GANGNAM STYLE", URLDiscItem.DiscVariant.EMERALD)
        addSongRecord("https://www.youtube.com/watch?v=l8S1NzEmfcQ", 247, "DJ BoBo - Love Is All Around", URLDiscItem.DiscVariant.AMETHYST)
        addSongRecord("https://www.youtube.com/watch?v=ag5Dxuc-OHw", 174, "creamy - Rocket Man", URLDiscItem.DiscVariant.MAGENTA)
        addSongRecord("https://www.youtube.com/watch?v=U0TXIXTzJEY", 295, "Yugo Kanno - Golden Wind", URLDiscItem.DiscVariant.GOLD)
        addSongRecord("https://www.youtube.com/watch?v=0XFudmaObLI", 256, "Aaron Smith - Dancin (KRONO Remix)", URLDiscItem.DiscVariant.BLUE)
        addSongRecord("https://www.youtube.com/watch?v=bL7nUkpFZHg", 216, "Aaron Smith - Dancin (CG5 Cover)", URLDiscItem.DiscVariant.CYAN)
        addSongRecord("https://www.youtube.com/watch?v=6Dh-RL__uN4", 134, "PewDiePie - B**** Lasagna", URLDiscItem.DiscVariant.RED)
        addSongRecord("https://www.youtube.com/watch?v=4XKGfziuw5c", 272, "Grupo Y-no - Querido Meu Amor", URLDiscItem.DiscVariant.YELLOW)
        addSongRecord("https://www.youtube.com/watch?v=2gLt3vst4l8", 228, "Vuc Vuc - Infinity (Maffalda Remix)", URLDiscItem.DiscVariant.GREEN)
        addSongRecord("https://www.youtube.com/watch?v=989-7xsRLR4", 249, "Vitas - The 7th Element", URLDiscItem.DiscVariant.LIME)
        addSongRecord("https://www.youtube.com/watch?v=3pbvmR8n27w", 316, "C418 - Sweden (Caution & Crisis Remix)", URLDiscItem.DiscVariant.BLUE)
        addSongRecord("https://www.youtube.com/watch?v=cPJUBQd-PNM", 264, "CaptainSparklez - Revenge", URLDiscItem.DiscVariant.GREEN)
        addSongRecord("https://www.youtube.com/watch?v=X_XGxzMrq04", 228, "BebopVox - Don't Mine At Night", URLDiscItem.DiscVariant.PETRIFIED)
        addSongRecord("https://www.youtube.com/watch?v=eQlsmfirpbA", 248, "Ant Venom - Through The Night", URLDiscItem.DiscVariant.MAGENTA)
        addSongRecord("https://www.youtube.com/watch?v=Qtf8YFw8iZg", 237, "Lena Raine - Creator", URLDiscItem.DiscVariant.ORANGE)
        addSongRecord("https://www.youtube.com/watch?v=V6QrZe7ag9k", 234, "Lena Raine - Creator (8-bit cover)", URLDiscItem.DiscVariant.GREEN)
        addSongRecord("https://www.youtube.com/watch?v=15YA3_dJMek", 274, "Lena Raine - Firebugs (Buttonmashur Remix)", URLDiscItem.DiscVariant.LIME)
        addSongRecord("https://www.youtube.com/watch?v=dEgjOyBwIaE", 299, "Aaron Cherof - Precipice", URLDiscItem.DiscVariant.PURPLE)
        addSongRecord("https://www.youtube.com/watch?v=WR7fwMM5ccc", 289, "Starbound - I Was The Sun (Before It Was Cool)", URLDiscItem.DiscVariant.HONEY)
        addSongRecord("https://www.youtube.com/watch?v=jcxeENnM0yI", 22, "Portal - Radio Loop", URLDiscItem.DiscVariant.COOKIE)
      }
    )

}