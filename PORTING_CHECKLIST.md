# NeoForge port inventory

Comparison source: `fabric-AnimeWitchery-1.20.x`. Item/block counts come from IDs declared in Fabric and NeoForge `ModItems`/`ModBlocks` registration sources. Java class comparison is a basename scan, so renamed ports need manual review. The recipe reference section lists active JSON IDs absent from literal NeoForge registrations.

## Port order

1. [ ] Resolve every remaining recipe dependency listed below, preserving the original item/block behavior.
2. [x] Port the alchemy table: block, block entity, inventory, menu/screen, synchronization, and recipe execution.
3. [ ] Port other block and machine systems, including block entities and screens.
4. [ ] Port remaining item/equipment behavior.
5. [ ] Port world generation, dimensions, entities, effects, enchantments, and integrations.
6. [ ] Port client renderers, models, particles, shaders, and screens.

## Work completed in this pass

- [x] Haloic armor and sword registration; armor uses existing Haloic material/full-set bonus, and the sword applies the Fabric undead damage/fire effect.
- [x] Eight spell scroll registrations and their spell name, tooltip, rarity, and glint behavior.
- [x] Rosewillow planks, stairs, slab, fence, and fence gate, plus Scheelite block.
- [x] Alchemy table block entity, 10 ingredient slots, output slot, recipe processing, XP/catalyst activation, menu/screen, GeckoLib idle model, and floating result/orbiting ingredient renderer.
- [x] Asset audit against Fabric: every Fabric asset path is already present in NeoForge, including `alchemy_book_ui.png`, `alchemy_table_gui.png`, `extractor_gui.png`, `spellbook_gui.png`, and shader resources. Replaced the two spell-trigger placeholder markers with 16x16 glyph textures and repaired broken texture references for spell scrolls, marble, Scheelite, catalyst blocks, and related items using shipped assets or vanilla block textures.
- [x] Verified the reported dark log/plank/sapling and Luna crystal texture files and their model references are present. Sixteen unresolved model references remain for features whose block models are still unported; those are tracked with the outstanding block work.
- [x] Ported the missing marble construction blocks: marble bricks and pillar, plus polished marble and its stair, slab, and wall variants. Registered their block items, added them to the creative tab, and supplied blockstates, models, item models, loot tables, and English names. The variants reuse vanilla calcite and polished diorite textures, matching the Fabric model provider.
- [x] Registered 100 previously missing Fabric class armor and weapon items in NeoForge and added them to Combat, including the class armor sets, first-wave Assassin/Butcher/Brute/Monk equipment, eight class weapons, and eight scythes. Reused the armor materials and item models/textures already in the NeoForge assets. Class-specific passive effects, weapon procs, and scythe area-harvest behavior still need dedicated ports.
- [x] Ported empty flask filling, four-charge potion flask drinking/empty-flask return with a charge bar, mortar-and-pestle crafting durability, fire-resistance and speed crystal effects, the gliding-only mana rocket boost using NeoForge mana attachments, the passive durability-repair charm, repair essence, and low-health emergency recall to the saved spawn. Added them to the Basic creative tab. The potion flask's offhand thrown-potion bonus remains to be ported.
- [x] Ported the divining rod scan for nearby vanilla ores and its feedback/cooldown/consumption behavior, and registered it in the Basic creative tab.
- [x] Ported Torch Flower Essence's Nether Wart Block to Shroomlight conversion and consumption behavior, and registered it in the Basic creative tab.
- [x] Ported the stateful Weather Changer, including its custom-data mode selection, item-model predicate, mana costs (including stored catalyst mana), weather changes, sounds, feedback, and cooldown.
- [x] Ported the charged Respawn Beacon teleport, including the XP cost, saved-spawn fallback, and sneaking companion teleport behavior.
- [x] Ported the durability-based Metal Detector search, including its detectable-block tag, which covers mod silver/scheelite ores and vanilla ore tags.
- [x] Ported Soul Jar's 27-slot persistent inventory, menu synchronization, GUI, and quick-move behavior. Entity capture, recall, and summoning through the Summoner Staff remain to be ported.

## Registry comparison

Counts are the original inventory snapshot and have not been regenerated after the ports in this pass, so treat them as an approximate baseline.

- Fabric item IDs scanned: 249; NeoForge IDs: 93; unmatched: 171.
- Fabric block IDs scanned: 125; NeoForge IDs: 105; unmatched: 58.
- Active recipes reference 205 distinct AnimeWitchery IDs; unresolved by NeoForge literal registration scan: 70.

### Unmatched Fabric item registration IDs

`air_spell_scroll`, `alchemist_boots`, `alchemist_hood`, `alchemist_leggings`, `ascendant_skill_orb`, `assassin_boots`, `assassin_chestplate`, `assassin_dagger`, `assassin_hood`, `assassin_leggings`, `bloodletter`, `butcher_apron`, `butcher_boots`, `butcher_helmet`, `butcher_knife`, `butcher_leggings`, `cat_ears`, `chalk`, `chaps`, `chipped_dice`, `chisel`, `copper_scythe`, `copper_warhammer`, `cosmetic_bag`, `cotton_gown`, `cowboy_hat`, `creeping_prevailance`, `crown`, `crystalline_spatial_fold`, `darkness_spell_scroll`, `death_spell_scroll`, `deathbringer_boots`, `deathbringer_hood`, `deathbringer_leggings`, `deathbringer_robes`, `deathbringer_scythe`, `deck_of_cards`, `defiant_rift`, `diamond_scythe`, `dimension`, `divining_rod`, `domain_spark`, `druid_boots`, `druid_hood`, `druid_leggings`, `earth_spell_scroll`, `emergency_recall`, `empty_flask`, `farmer_boots`, `farmer_leggings`, `farmer_overalls`, `farming_hoe`, `femboy_hoodie`, `fire_res_crystal`, `fire_spell_scroll`, `gambler_pants`, `gambler_shoes`, `gambler_visor`, `grand_skill_orb`, `great_hammer`, `gunslinger_boots`, `gunslinger_vest`, `healing_staff`, `heavy_mace`, `heavy_plate_boots`, `heavy_plate_chestplate`, `heavy_plate_helmet`, `heavy_plate_leggings`, `hoarder_boots`, `hoarder_chestplate`, `hoarder_helmet`, `hoarder_leggings`, `hooded_cape`, `iron_scythe`, `iron_shackle`, `job_application`, `kamikaze_ritual_scroll`, `keep_inventory_charm`, `key`, `kinetic_breaker`, `lemon_seeds`, `life_spell_scroll`, `light_spell_scroll`, `longsword`, `mage_boots`, `mage_hat`, `mage_leggings`, `mage_robes`, `mage_staff`, `magic_chalk`, `major_skill_orb`, `mana_rocket`, `metal_detector`, `minor_skill_orb`, `mod_totem`, `monk_boots`, `monk_head`, `monk_leggings`, `mortar_and_pestle`, `nature_staff`, `nbt_preserving_tool`, `needle`, `netherite_scythe`, `oathbreaker`, `obelisk_boots`, `obelisk_chestplate`, `obelisk_helmet`, `obelisk_leggings`, `obelisk_sword`, `paladin_boots`, `paladin_chestplate`, `paladin_helmet`, `paladin_leggings`, `plate_boots`, `plate_chestplate`, `plate_leggings`, `platform_boots`, `potion_flask`, `prospector_boots`, `prospector_hat`, `prospector_leggings`, `prospector_shovel`, `prospector_tunic`, `railgun`, `railgunner_boots`, `railgunner_chestplate`, `railgunner_helmet`, `railgunner_leggings`, `rawsilver`, `redstone_transmitter`, `redstone_transmitter_button`, `repair_essence`, `resonant_focus`, `resonant_greatsword`, `resonant_shield`, `resonant_shield_3d`, `resonant_spark`, `respawn_beacon`, `revolver`, `rituals_book`, `rosewillow_vines`, `sanguine_boots`, `sanguine_hood`, `sanguine_leggings`, `silveringot`, `soiled_jacket`, `soul`, `soul_jar`, `soul_scythe`, `sparse_tunic`, `speed_crystal`, `spellbook`, `spellbook_page`, `stained_robes`, `star_tear`, `stone_scythe`, `straw_hat`, `strawberry_seeds`, `stuffed_animal`, `summoner_boots`, `summoner_hood`, `summoner_leggings`, `summoner_robes`, `summoner_staff`, `sword_of_midas`, `thigh_highs`, `torch_flower_essence`, `void_wisp_spawn_egg`, `water_spell_scroll`, `weatheritem`, `wooden_scythe`

### Unmatched Fabric block registration IDs

`active_binding_spell`, `active_effigy_fountain`, `active_obelisk`, `alchemical_catalyst_block`, `alchemical_enchanter`, `alchemy_table`, `auto_crafter`, `barrier_circle`, `barrier_distance_glyph`, `binding_spell`, `block_miner`, `block_placer`, `boss_obelisk`, `decorative_fountain`, `deepslate_threshold`, `effigy_fountain`, `enemy_landing_platform`, `float_block`, `gacha_altar`, `growth_accelerator`, `guardian_statue`, `interactor`, `landing_platform`, `large_rosewillow_blossom`, `looping_jukebox`, `marble_brick_slab`, `marble_brick_stairs`, `marble_brick_wall`, `marble_bricks`, `marble_pillar`, `monster_statue`, `obelisk`, `particle_block`, `particle_sink_block`, `pillar`, `plate`, `polished_marble`, `polished_marble_slab`, `polished_marble_stairs`, `polished_marble_wall`, `redstone_receiver`, `redstone_toggle_receiver`, `redstone_transmitter_button`, `resonant_catalyst_block`, `rosewillow_bulb`, `rosewillow_fence`, `rosewillow_fence_gate`, `rosewillow_planks`, `rosewillow_roots`, `rosewillow_sapling`, `rosewillow_slab`, `rosewillow_stairs`, `scheelite_block`, `server_button`, `shimmering_farmland`, `spell_trigger_block`, `transmutation_pyre_block`, `world_button`

### Active recipe IDs missing from NeoForge registration literals

`air_spell_scroll`, `ascendant_skill_orb`, `auto_crafter`, `binding_spell`, `block_miner`, `block_placer`, `capturing`, `chalk`, `chisel`, `cosmetic_bag`, `creeping_prevailance`, `crystalline_spatial_fold`, `darkness_spell_scroll`, `death_spell_scroll`, `decorative_fountain`, `defiant_rift`, `dimension`, `domain_spark`, `earth_spell_scroll`, `empty_flask`, `fire_spell_scroll`, `grand_shulker_box`, `grand_skill_orb`, `growth_accelerator`, `healing_staff`, `interactor`, `job_application`, `kamikaze_ritual_scroll`, `keep_inventory_charm`, `kinetic_breaker`, `life_spell_scroll`, `light_spell_scroll`, `magic_chalk`, `major_skill_orb`, `mana_rocket`, `minor_skill_orb`, `mod_totem`, `mortar_and_pestle`, `nbt_preserving_tool`, `needle`, `netherite_door`, `obelisk_boots`, `obelisk_chestplate`, `obelisk_helmet`, `obelisk_leggings`, `obelisk_sword`, `particle_block`, `particle_sink_block`, `protected_chest`, `railgun`, `railgunner_boots`, `railgunner_chestplate`, `railgunner_helmet`, `railgunner_leggings`, `repair_essence`, `resonant_greatsword`, `respawn_beacon`, `rosewillow_fence`, `rosewillow_fence_gate`, `rosewillow_planks`, `rosewillow_slab`, `rosewillow_stairs`, `scheelite_block`, `spell_trigger_block`, `spellbook`, `torch_flower_essence`, `transmutation_pyre_block`, `void_wisp_spawn_egg`, `water_spell_scroll`, `weatheritem`

## Fabric Java files without a same-named NeoForge source

508 files grouped by Fabric package; review for real platform ports versus Fabric-only glue or renamed implementations.

### `.` (3)

`AnimeWitcheryDataGenerator`, `ModScreenHandlers`, `TestReflection`

### `block/custom` (54)

`ActiveBindingSpellBlock`, `ActiveEffigyFountainBlock`, `ActiveObeliskBlock`, `AlchemicalEnchanterBlock`, `AlchemyTableBlock`, `AutoCrafterBlock`, `BarrierCircleBlock`, `BarrierDistanceGlyphBlock`, `BindingSpellBlock`, `BlockMinerBlock`, `BlockPlacerBlock`, `BossObeliskBlock`, `DecorativeFountainBlock`, `DeepslateThresholdBlock`, `EffigyFountainBlock`, `EnemyLandingPlatformBlock`, `FloatBlock`, `GachaAltarBlock`, `GrandShulkerBoxBlock`, `GrowthAcceleratorBlock`, `GuardianStatueBlock`, `ItemActionBlock`, `LandingPlatformBlock`, `LargeRosewillowBlossomBlock`, `LemonCropBlock`, `LockboxBlock`, `LoopingJukeboxBlock`, `MonsterStatueBlock`, `NetheriteDoorBlock`, `ObeliskBlock`, `ParticleBeamBlock`, `ParticleSinkBlock`, `PillarBlock`, `PlateBlock`, `RedstoneReceiverBlock`, `RedstoneToggleReceiverBlock`, `RedstoneTransmitterButtonBlock`, `RosewillowBulbBlock`, `RosewillowFenceBlock`, `RosewillowFenceGateBlock`, `RosewillowRootsBlock`, `RosewillowSaplingBlock`, `RosewillowSlabBlock`, `RosewillowStairsBlock`, `RoseWillowVineBodyBlock`, `RoseWillowVineHeadBlock`, `ServerButtonBlock`, `ShimmeringFarmlandBlock`, `SpellTriggerBlock`, `StarlightFluidBlock`, `StrawberryCropBlock`, `StrongboxBlock`, `TransmutationPyreBlock`, `WorldButtonBlock`

### `block/entity` (33)

`ActiveBindingSpellBlockEntity`, `ActiveEffigyFountainBlockEntity`, `ActiveObeliskBlockEntity`, `AlchemicalEnchanterBlockEntity`, `AlchemyTableBlockEntity`, `AutoCrafterBlockEntity`, `BarrierCircleBlockEntity`, `BarrierDistanceGlyphBlockEntity`, `BindingSpellBlockEntity`, `BlockMinerBlockEntity`, `BlockPlacerBlockEntity`, `BossObeliskBlockEntity`, `DecorativeFountainBlockEntity`, `DeepslateThresholdBlockEntity`, `EffigyFountainBlockEntity`, `GachaAltarBlockEntity`, `GrandShulkerBoxBlockEntity`, `GrowthAcceleratorBlockEntity`, `GuardianStatueBlockEntity`, `ItemActionBlockEntity`, `LockboxBlockEntity`, `LoopingJukeboxBlockEntity`, `NetheriteDoorBlockEntity`, `ObeliskBlockEntity`, `ParticleBeamBlockEntity`, `PlateBlockEntity`, `RedstoneTransmitterButtonBlockEntity`, `RosewillowRootsBlockEntity`, `ServerButtonBlockEntity`, `SpellTriggerBlockEntity`, `StrongboxBlockEntity`, `TransmutationPyreBlockEntity`, `WorldButtonBlockEntity`

### `block/entity/model` (14)

`ActiveBindingSpellModel`, `ActiveEffigyFountainModel`, `ActiveObeliskModel`, `AlchemyTableModel`, `BindingSpellModel`, `BossObeliskModel`, `DecorativeFountainModel`, `DeepslateThresholdModel`, `EffigyFountainModel`, `GrandShulkerBoxModel`, `GuardianStatueModel`, `ObeliskModel`, `PlateBlockModel`, `StrongboxModel`

### `block/entity/renderer` (19)

`ActiveBindingSpellRenderer`, `ActiveEffigyFountainRenderer`, `ActiveObeliskRenderer`, `AlchemicalEnchanterRenderer`, `AlchemyTableRenderer`, `BarrierCircleRenderer`, `BarrierDistanceGlyphRenderer`, `BindingSpellRenderer`, `BossObeliskRenderer`, `DecorativeFountainRenderer`, `DeepslateThresholdRendererFixed`, `EffigyFountainRenderer`, `GrandShulkerBoxRenderer`, `GuardianStatueRenderer`, `ObeliskRenderer`, `ParticleBeamRenderer`, `PlateRenderer`, `StrongboxRenderer`, `TransmutationPyreRenderer`

### `client` (8)

`AttackerTracker`, `ClientBlockUtil`, `CosmeticArmorState`, `DebugScreenInterceptor`, `DiviningRodRenderer`, `IntermediaryScreen`, `ManaHudOverlay`, `PocketGridRenderer`

### `client/input` (1)

`PlatformKeybindHandler`

### `client/render` (2)

`ParadiseLostDimensionEffects`, `ParadiseLostSkyRenderer`

### `client/render/feature` (1)

`ResonantEyeFeatureRenderer`

### `client/shader` (1)

`VoidBoundPostProcessor`

### `client/sky` (1)

`SkyRitualRenderer`

### `command` (1)

`PocketCommands`

### `component` (6)

`ClassComponent`, `ClassRegistry`, `IClassComponent`, `IVanityComponent`, `SkillRegistry`, `VanityComponent`

### `config` (1)

`MenuWorldConfig`

### `datagen` (8)

`ModBlockTagProvider`, `ModEntityLootTableProvider`, `ModItemTagProvider`, `ModLootTableProvider`, `ModModelProvider`, `ModPoiTagProvider`, `ModRecipeProvider`, `ModWorldGenerator`

### `effect` (4)

`BleedingStatusEffect`, `ModEffect`, `PacifismStatusEffect`, `RevealedStatusEffect`

### `entity` (5)

`ISummonedEntity`, `KamikazeRitualEntity`, `KineticBladeHitboxEntity`, `ModEntities`, `VoidWispEntity`

### `entity/ai` (3)

`FollowCrownGoal`, `FollowSummonerGoal`, `SummonerTargetGoal`

### `entity/client` (3)

`KamikazeRitualRenderer`, `ModEntityRenderers`, `ResonantShieldEntityRenderer`

### `entity/client/model` (4)

`KamikazeRitualModel`, `MonsterStatueModel`, `NeedleProjectileGeoModel`, `VoidWispModel`

### `entity/client/render` (4)

`MonsterStatueRenderer`, `NeedleModel`, `NeedleProjectileRenderer`, `VoidWispRenderer`

### `entity/custom` (3)

`MonsterStatueBlockEntity`, `ResonantShieldEntity`, `ResonantSparkEntity`

### `entity/projectile` (1)

`NeedleProjectileEntity`

### `events` (20)

`AlchemyEnchantmentHandler`, `AlchemySpellbookHandler`, `BarrierBlockProtectionHandler`, `BarrierExplosionHandler`, `BarrierInteractionHandler`, `BarrierProtectionHandler`, `BlastingBreakHandler`, `CapturingHandler`, `ChainsawBreakHandler`, `ClassCombatHandler`, `ExcavationBreakHandler`, `ItemFrameInvisibilityHandler`, `MobEquipmentHandler`, `ModEvents`, `PlatformCreator`, `SoulJarInteractionHandler`, `SoulRecoveryHandler`, `SpawnerNbtHandler`, `SpellbookEnchantingHandler`, `SpellbookInteractionHandler`

### `fluid` (1)

`StarlightFluid`

### `item` (2)

`ModFoodComponents`, `ModItemGroups`

### `item/armor` (2)

`ObeliskArmorItem`, `RailGunnerArmorItem`

### `item/armor/client` (2)

`ObeliskArmorRenderer`, `RailGunnerArmorRenderer`

### `item/client/model` (4)

`HealingStaffModel`, `ObeliskSwordModel`, `RailgunModel`, `RailGunnerArmorModel`

### `item/custom` (96)

`AssassinDaggerItem`, `AssassinHoodItem`, `BanHammerItem`, `BloodletterItem`, `ButcherApronItem`, `ButcherKnifeItem`, `ChapsItem`, `ChippedDiceItem`, `ChiselItem`, `CopperWarhammerItem`, `CosmeticBagHelper`, `CosmeticBagItem`, `CottonGownItem`, `CowboyHatItem`, `CrownItem`, `CrystallineSpatialFoldItem`, `DeathbringerRobesItem`, `DeckOfCardsItem`, `DimensionTeleportItem`, `DiviningRodItem`, `DomainSparkItem`, `EmergencyRecallItem`, `EmptyFlaskItem`, `FarmerArmorItem`, `FarmingHoeItem`, `FireResCrystalItem`, `GrandShulkerBoxItem`, `GreatHammerItem`, `HealingStaff`, `HeavyMaceItem`, `HeavyPlateArmorItem`, `HoarderArmorItem`, `HoodedCapeItem`, `IronShackleItem`, `KamikazeRitualScroll`, `KeepInventoryCharmItem`, `KeyItem`, `KineticBladeItem`, `LongswordItem`, `MageRobesItem`, `MageStaffItem`, `MagicChalkItem`, `ManaRocketItem`, `MetalDetectorItem`, `ModTotemItem`, `MonsterStatueBlockItem`, `MortarAndPestleItem`, `NatureStaffItem`, `NBTPreservingToolItem`, `NeedleItem`, `NormalChalkItem`, `OathbreakerItem`, `ObeliskSwordItem`, `PaladinArmorItem`, `PatchouliBookItem`, `PlateArmorItem`, `PotionFlaskItem`, `ProspectorShovelItem`, `QuiverItem`, `RailgunItem`, `RedstoneTransmitterButtonItem`, `RedstoneTransmitterItem`, `RepairCharmItem`, `RepairEssenceItem`, `ResonantBeamItem`, `ResonantGreatSwordItem`, `ResonantlCatalystItem`, `ResonantShieldItem`, `ResonantSparkItem`, `RespawnTeleportItem`, `RevolverItem`, `RosewillowBlossomItem`, `ScytheItem`, `SilverPendant`, `SkillOrbItem`, `SoiledJacketItem`, `SoulItem`, `SoulJarItem`, `SoulScytheItem`, `SparseTunicItem`, `SpeedCrystalItem`, `SpellbookItem`, `SpellbookPageItem`, `StainedRobesItem`, `StarlightBucketItem`, `StarTearItem`, `StuffedAnimalItem`, `SummonerRobesItem`, `SummonerStaffItem`, `SwordOfMidasItem`, `ThighHighsItem`, `TorchFlowerEssenceItem`, `UnbanWandItem`, `VoidWispSpawnEggItem`, `WeatherClearItem`, `WeatherItem`

### `item/renderer` (4)

`HealingStaffRenderer`, `ObeliskBuiltinItemRenderer`, `ObeliskSwordRenderer`, `RailgunRenderer`

### `mana` (7)

`IManaComponent`, `ManaComponent`, `ManaHelper`, `ManaStorageRegistry`, `ManaTicker`, `ManaUtils`, `ModComponents`

### `mixin` (69)

`AmetystClusterMixin`, `AnvilScreenHandlerMixin`, `AssassinArmorRenderMixin`, `BeaconMixin`, `BrewingStandBlockEntityMixin`, `BrewingStandFuelMixin`, `BrewingStandFuelSlotMixin`, `ButcherLootingEnchantmentMixin`, `ButcherLootingLivingMixin`, `CapturingLootingMixin`, `ChiseldStoneBricksMixin`, `CreativeDefiantRiftMixin`, `CreeperBlockDamageMixin`, `CropBlockMixin`, `CropBlockOnlyMixin`, `CrossbowItemMixin`, `DeepslateDiamondOreMixinMixin`, `DiamondOreMixin`, `EntityDeathMixin`, `EntityMixin`, `ExampleMixin`, `ExplosionMixin`, `FarmlandBlockMixin`, `FluidBlockMixin`, `GlassBlockMixin`, `GlobalTotemMixin`, `GoldOreMixin`, `GreatswordRenderMixin`, `GreatswordSweepingMixin`, `GreatswordWardenBeamMixin`, `GuildedBlackstoneMixin`, `HideOffhandMixin`, `IronGolemFollowMixin`, `IronOreMixin`, `ItemRendererAccessor`, `ItemRendererMixin`, `KeepInventoryCharmMixin`, `LakeFeatureMixin`, `LightningRodBlockMixin`, `LivingEntityMixin`, `LivingEntityMixin2`, `LivingEntityRendererAccessor`, `LodestoneBlockMixin`, `MinecraftServerMixin`, `MobEntityMixin`, `MouseMixin`, `NetheriteBlockMixin`, `ParadiseLostMixin`, `PlayerDeathMixin`, `PlayerEntityMixin`, `PlayerEntityRendererMixin`, `PotionSlotMixin`, `ReinforcedDeepslateMixin`, `ResonantEyeGlowMixin`, `ResonantShieldDisableMixin`, `ResonantShieldWardenMixin`, `ScytheSweepingMixin`, `ServerPlayerEntityMixin`, `SettingsAccessor`, `ShroomlightMixin`, `SkulkCatalystMixin`, `StairsBlockAccessor`, `StonePressurePlateMixin`, `TorchflowerLightMixin`, `UndeadMixin`, `UtilMixin`, `VillagerMobGriefingMixin`, `WolfFollowMixin`, `WorldRendererMixin`

### `mixin/client` (7)

`ForceRiptideAnimationMixin`, `GeckoLibVanityMixin`, `LivingEntityRiptideAccessor`, `ParadiseLostFogMixin`, `ParadiseLostSkyMixin`, `VanityArmorMixin`, `VanityLivingEntityMixin`

### `mixin/common` (1)

`PlayerScreenHandlerMixin`

### `mixin/entity` (1)

`StarlightItemEntityMixin`

### `networking` (3)

`ModPackets`, `SpellbookPackets`, `WorldButtonSyncPacket`

### `networking/lodestone` (7)

`KamikazeFxPacket`, `LaserBeamPacket`, `LaserChargePacket`, `LaserHitPacket`, `ObeliskShake`, `RailgunBeamPacket`, `ResonantBeamParticlePacket`

### `networking/payload` (19)

`BlockPosPayload`, `CreatePlatformPayload`, `CycleNextPayload`, `CyclePrevPayload`, `KamikazeFxPayload`, `LaserBeamPayload`, `LaserChargePayload`, `LaserHitPayload`, `ObeliskShakePayload`, `RailgunBeamPayload`, `RemoveSoulPayload`, `ResonantBeamParticlePayload`, `SelectClassPayload`, `SetServerPayload`, `SetWorldPayload`, `SwapSoulsPayload`, `SwingMissPayload`, `SyncEnchantmentListsPayload`, `UnlockSkillPayload`

### `particle` (1)

`ShockwaveParticle`

### `recipe` (3)

`DecompileCatalystBlockRecipe`, `EffigyFountainRecipe`, `EmptyCatalystBlockRecipe`

### `ritual` (2)

`RitualConfiguration`, `RitualConfigurationBuilder`

### `screen` (27)

`AdvancedSpellbookScreen`, `AdvancedSpellbookScreenHandler`, `AlchemicalEnchanterScreen`, `AlchemicalEnchanterScreenHandler`, `AlchemyTableScreen`, `AlchemyTableScreenHandler`, `AutoCrafterScreen`, `AutoCrafterScreenHandler`, `BlockMinerScreen`, `BlockMinerScreenHandler`, `BlockPlacerScreen`, `BlockPlacerScreenHandler`, `CosmeticBagScreen`, `CosmeticBagScreenHandler`, `GrandShulkerBoxScreen`, `GrandShulkerBoxScreenHandler`, `GrandShulkerBoxSlot`, `GrowthAcceleratorScreen`, `GrowthAcceleratorScreenHandler`, `ItemActionScreen`, `ItemActionScreenHandler`, `LoopingJukeboxScreen`, `LoopingJukeboxScreenHandler`, `PlayerUseDispenserScreen`, `PlayerUseDispenserScreenHandler`, `SoulJarScreen`, `SoulJarScreenHandler`

### `screen/class_system` (2)

`ClassSelectionScreen`, `SkillTreeScreen`

### `screen/slot` (1)

`DispenserSlot`

### `sound` (1)

`ModSounds`

### `state/property` (1)

`ModProperties`

### `util` (21)

`AdvancedSpellCaster`, `AdvancedSpellConfiguration`, `BookInteractionHandler`, `ButcherContext`, `ChunkLoadingManager`, `FakeServerPlayer`, `FakeServerPlayerInteractionManager`, `IKeepInventoryCharmUser`, `ImplementedInventory`, `ItemStackHelper`, `MidnightWandState`, `MinecraftServerDuck`, `ModCustomTrades`, `ModGameRules`, `ModLootTableModifiers`, `ModTags`, `ServerScheduler`, `SpellCaster`, `SpellConfiguration`, `SpellEncoder`, `TrinketsHelper`

### `villager` (1)

`ModVillagers`

### `world` (7)

`ModConfiguredFeatures`, `ModOrePlacement`, `ModPlacedFeatures`, `ObeliskBreakState`, `ObeliskRegistry`, `ObeliskWorldListener`, `SoulRecoveryState`

### `world/dimension` (5)

`DimensionLoader`, `DynamicDimensionManager`, `ModDimensions`, `PocketCollisionHandler`, `PocketManager`

### `world/gen` (6)

`ModFeatures`, `ModFoliagePlacerTypes`, `ModOreGeneration`, `ModTreeDecoratorTypes`, `ModWorldGeneration`, `RosewillowSaplingGenerator`

### `world/gen/feature` (2)

`FloatingIslandFeature`, `RosewillowMegaTreeFeature`

### `world/gen/foliage` (1)

`RosewillowFoliagePlacer`

### `world/gen/treedecorator` (1)

`RosewillowVineDecorator`

### `world/spawn` (1)

`VoidWispSpawnHandler`
