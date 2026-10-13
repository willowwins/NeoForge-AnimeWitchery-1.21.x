import os, json, re

workspace = r'c:\Users\willow\IdeaProjects\NeoForge-AnimeWitchery-1.21.x'
models_item_dir = os.path.join(workspace, 'src', 'main', 'resources', 'assets', 'animewitchery', 'models', 'item')
models_block_dir = os.path.join(workspace, 'src', 'main', 'resources', 'assets', 'animewitchery', 'models', 'block')
textures_dir = os.path.join(workspace, 'src', 'main', 'resources', 'assets', 'animewitchery', 'textures')

# Read ModItems.java
items = set()
with open(os.path.join(workspace, 'src', 'main', 'java', 'net', 'willowins', 'animewitchery', 'item', 'ModItems.java'), 'r') as f:
    content = f.read()
    for m in re.finditer(r'ITEMS\.register(?:SimpleItem)?\s*\(\s*"([^"]+)"', content):
        items.add(m.group(1))

# Read ModBlocks.java
blocks = set()
with open(os.path.join(workspace, 'src', 'main', 'java', 'net', 'willowins', 'animewitchery', 'block', 'ModBlocks.java'), 'r') as f:
    content = f.read()
    for m in re.finditer(r'registerBlock\s*\(\s*"([^"]+)"', content):
        blocks.add(m.group(1))

# Read ModFluids.java
fluids = set()
with open(os.path.join(workspace, 'src', 'main', 'java', 'net', 'willowins', 'animewitchery', 'fluid', 'ModFluids.java'), 'r') as f:
    content = f.read()
    for m in re.finditer(r'ITEMS\.register\s*\(\s*"([^"]+)"', content):
        fluids.add(m.group(1))

print(f"Registered items: {len(items)}")
print(f"Registered block items: {len(blocks)}")
print(f"Registered fluid bucket items: {len(fluids)}")

all_registered = items.union(blocks).union(fluids)
print(f"Total registered item IDs: {len(all_registered)}")

missing_item_models = []
broken_item_models = []

for item in sorted(all_registered):
    model_path = os.path.join(models_item_dir, f"{item}.json")
    if not os.path.exists(model_path):
        missing_item_models.append(item)
    else:
        try:
            with open(model_path, 'r') as mf:
                data = json.load(mf)
                parent = data.get('parent', '')
                if parent.startswith('animewitchery:block/'):
                    bname = parent.replace('animewitchery:block/', '')
                    if not os.path.exists(os.path.join(models_block_dir, f"{bname}.json")):
                        broken_item_models.append((item, f"parent block model missing: animewitchery:block/{bname}"))
                elif parent.startswith('animewitchery:item/'):
                    iname = parent.replace('animewitchery:item/', '')
                    if not os.path.exists(os.path.join(models_item_dir, f"{iname}.json")):
                        broken_item_models.append((item, f"parent item model missing: animewitchery:item/{iname}"))
                
                textures = data.get('textures', {})
                for k, v in textures.items():
                    if v.startswith('animewitchery:'):
                        tpath = v.replace('animewitchery:', '') + '.png'
                        full_tpath = os.path.join(textures_dir, tpath)
                        if not os.path.exists(full_tpath):
                            broken_item_models.append((item, f"texture missing: {v} (layer/key: {k})"))
        except Exception as e:
            broken_item_models.append((item, f"JSON error: {e}"))

print(f"\n--- MISSING ITEM MODELS ({len(missing_item_models)}) ---")
for m in missing_item_models:
    print(m)

print(f"\n--- BROKEN ITEM MODELS ({len(broken_item_models)}) ---")
for b, reason in broken_item_models:
    print(f"{b}: {reason}")
