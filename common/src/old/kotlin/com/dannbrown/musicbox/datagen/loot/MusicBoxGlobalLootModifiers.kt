package com.dannbrown.musicbox.datagen.loot
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.init.MusicBoxDiscs
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.minecraftforge.common.data.GlobalLootModifierProvider
import net.minecraftforge.common.loot.LootTableIdCondition


class MusicBoxGlobalLootModifiers(output: PackOutput) : GlobalLootModifierProvider(output, MusicBoxModule.MOD_ID) {
  override fun start() {
    add("sweden_remix_disc_from_grass",
      AddDiscModifier(arrayOf(
        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
        LootItemRandomChanceCondition.randomChance(0.0001f).build() // 0.01% chance
      ), MusicBoxDiscs.SWEDEN_REMIX)
    )

    add("revenge_disc_from_creeper",
      AddDiscModifier(arrayOf(
        LootTableIdCondition.Builder(ResourceLocation("entities/creeper")).build(),
        LootItemRandomChanceCondition.randomChance(0.01f).build() // 1% chance
      ), MusicBoxDiscs.REVENGE)
    )

    add("dont_mine_at_night_disc_from_mineshaft",
      AddDiscModifier(arrayOf(
        LootTableIdCondition.Builder(ResourceLocation("chests/abandoned_mineshaft")).build(),
        LootItemRandomChanceCondition.randomChance(0.5f).build() // 50% chance
      ), MusicBoxDiscs.DONTMINEATNIGHT)
    )

    add("golden_wind_disc_from_bastion_treasure",
      AddDiscModifier(arrayOf(
        LootTableIdCondition.Builder(ResourceLocation("chests/bastion_treasure")).build(),
        LootItemRandomChanceCondition.randomChance(0.5f).build() // 50% chance
      ), MusicBoxDiscs.GOLDEN_WIND)
    )

    add("portal_disc_from_end_city_treasure",
      AddDiscModifier(arrayOf(
        LootTableIdCondition.Builder(ResourceLocation("chests/end_city_treasure")).build(),
        LootItemRandomChanceCondition.randomChance(0.5f).build() // 50% chance
      ), MusicBoxDiscs.PORTAL)
    )

    add("rick_roll_disc_from_village_weaponsmith",
      AddDiscModifier(arrayOf(
        LootTableIdCondition.Builder(ResourceLocation("chests/village/village_weaponsmith")).build(),
        LootItemRandomChanceCondition.randomChance(0.3f).build() // 30% chance
      ), MusicBoxDiscs.RICK_ROLL)
    )

    add("rick_roll_disc_from_village_tannery",
      AddDiscModifier(arrayOf(
        LootTableIdCondition.Builder(ResourceLocation("chests/village/village_tannery")).build(),
        LootItemRandomChanceCondition.randomChance(0.3f).build() // 30% chance
      ), MusicBoxDiscs.RICK_ROLL)
    )

    add("rick_roll_disc_from_village_armorer",
      AddDiscModifier(arrayOf(
        LootTableIdCondition.Builder(ResourceLocation("chests/village/village_armorer")).build(),
        LootItemRandomChanceCondition.randomChance(0.3f).build() // 30% chance
      ), MusicBoxDiscs.RICK_ROLL)
    )

    add("darude_sandstorm_disc_from_desert_pyramid",
      AddDiscModifier(arrayOf(
        LootTableIdCondition.Builder(ResourceLocation("chests/desert_pyramid")).build(),
        LootItemRandomChanceCondition.randomChance(0.5f).build() // 50% chance
      ), MusicBoxDiscs.DARUDE_SANDSTORM)
    )
  }
}