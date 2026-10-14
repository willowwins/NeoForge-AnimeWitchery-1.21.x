import io

bak = r'src\main\java\net\willowins\animewitchery\block\ModBlocks.java.bak'
out = r'src\main\java\net\willowins\animewitchery\block\ModBlocks.java'

with io.open(bak, 'r', encoding='utf-8') as f:
    s = f.read()

start = s.find('BLUEM')
if start >= 0:
    ls = s.rfind('\n', 0, start) + 1
    semi = s.find(';', start)
    nl = s.find('\n', semi)
    le = nl + 1 if nl >= 0 else len(s)
    new = (
        '    public static final DeferredBlock<Block> BLUME = registerBlock("blume",\n'
        '            () -> new net.willowins.animewitchery.block.custom.BlumeBlock(\n'
        '                    BlockBehaviour.Properties.of()\n'
        '                            .strength(0.0F).sound(SoundType.GRASS).noOcclusion().noCollission())));\n'
    )
    s2 = s[:ls] + new + s[le:]
else:
    s2 = s

with io.open(out, 'w', encoding='utf-8', newline='\n') as f:
    f.write(s2)

print('ok')
