# Alchemy Table Recipe Images

To show the actual alchemy table GUI in the Patchouli book, create images of the alchemy table interface with recipe ingredients placed in the correct slots.

## Image Requirements:
- Size: 128x128 pixels (scaled down to fit Patchouli book pages)
- Format: PNG with transparency support
- Base: Use a simplified version of the alchemy table GUI

## Recommended Approach:
1. Create a simplified circular layout showing only the recipe slots
2. Scale down the GUI elements to fit in a 128x128 square
3. Focus on the circular arrangement of ingredients around the center output
4. Include visual indicators for XP cost and processing time

## Slot Layout (from AlchemyTableScreenHandler.java):
- Slot 0 (Output): Center position (79, 71)
- Slots 1-10 (Inputs): Arranged counter-clockwise around center:
  - Slot 1: (56, 10)
  - Slot 2: (29, 36) 
  - Slot 3: (21, 72)
  - Slot 4: (29, 107)
  - Slot 5: (56, 133)
  - Slot 6: (103, 133)
  - Slot 7: (130, 108)
  - Slot 8: (138, 72)
  - Slot 9: (130, 36)
  - Slot 10: (103, 10)

## Recipe Examples to Create:

### Blood Rune Stone (blood_rune_stone.png):
- Slot 1: rune_stone
- Slot 2: death_rune_stone  
- Slot 3: life_rune_stone
- Slot 4: nether_wart
- Slot 5: fermented_spider_eye
- Slot 6: ghast_tear
- Slot 7: resonant_catalyst (full)
- Output: blood_rune_stone

### Grand Shulker Box (grand_shulker_box.png):
- Slot 1: shulker_box
- Slot 2-9: shulker_shell (8x)
- Slot 10: resonant_catalyst (full)
- Output: grand_shulker_box

### Base Rune Stone (rune_stone.png):
- Slot 1: stone
- Slot 2: amethyst_dust
- Slot 3: bone_dust
- Slot 4: alchemical_catalyst (full)
- Output: rune_stone (4x)

### Fire Rune Stone (fire_rune_stone.png):
- Slot 1: rune_stone
- Slot 2: amethyst_dust
- Slot 3: magma_cream
- Slot 4: blaze_powder
- Slot 5: redstone
- Slot 6: coal
- Output: fire_rune_stone

### Obelisk Sword (obelisk_sword.png):
- Slot 1: netherite_sword
- Slot 2-3: obelisk_shard (2x)
- Slot 4: silver_template
- Slot 5: crying_obsidian
- Slot 6: resonant_catalyst (full)
- Output: obelisk_sword

### Obelisk Helmet (obelisk_helmet.png):
- Slot 1: netherite_helmet
- Slot 2-3: obelisk_shard (2x)
- Slot 4: silver_template
- Slot 5: crying_obsidian
- Slot 6: resonant_catalyst (full)
- Output: obelisk_helmet

## Instructions:
1. Copy alchemy_table_gui.png as the base
2. Overlay item textures at the specified slot positions
3. Place the result item in the center output slot
4. Save as PNG with the recipe name
5. Update Patchouli entries to use patchouli:image pages
