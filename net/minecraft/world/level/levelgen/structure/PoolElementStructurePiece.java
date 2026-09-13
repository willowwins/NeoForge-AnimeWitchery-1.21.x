package net.minecraft.world.level.levelgen.structure;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.slf4j.Logger;

public class PoolElementStructurePiece extends StructurePiece {
   private static final Logger LOGGER = LogUtils.getLogger();
   protected final StructurePoolElement element;
   protected BlockPos position;
   private final int groundLevelDelta;
   protected final Rotation rotation;
   private final List<JigsawJunction> junctions = Lists.newArrayList();
   private final StructureTemplateManager structureTemplateManager;

   public PoolElementStructurePiece(StructureTemplateManager structureTemplateManager, StructurePoolElement element, BlockPos position, int groundLevelDelta, Rotation rotation, BoundingBox box) {
      super(StructurePieceType.JIGSAW, 0, box);
      this.structureTemplateManager = structureTemplateManager;
      this.element = element;
      this.position = position;
      this.groundLevelDelta = groundLevelDelta;
      this.rotation = rotation;
   }

   public PoolElementStructurePiece(StructurePieceSerializationContext context, CompoundTag tag) {
      super(StructurePieceType.JIGSAW, tag);
      this.structureTemplateManager = context.structureTemplateManager();
      this.position = new BlockPos(tag.getInt("PosX"), tag.getInt("PosY"), tag.getInt("PosZ"));
      this.groundLevelDelta = tag.getInt("ground_level_delta");
      DynamicOps<Tag> dynamicops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());
      this.element = StructurePoolElement.CODEC.parse(dynamicops, tag.getCompound("pool_element")).resultOrPartial(LOGGER::error).orElseThrow(() -> {
         return new IllegalStateException("Invalid pool element found");
      });
      this.rotation = Rotation.valueOf(tag.getString("rotation"));
      this.boundingBox = this.element.getBoundingBox(this.structureTemplateManager, this.position, this.rotation);
      ListTag listtag = tag.getList("junctions", 10);
      this.junctions.clear();
      listtag.forEach((p_204943_) -> {
         this.junctions.add(JigsawJunction.deserialize(new Dynamic<>(dynamicops, p_204943_)));
      });
   }

   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
      tag.putInt("PosX", this.position.getX());
      tag.putInt("PosY", this.position.getY());
      tag.putInt("PosZ", this.position.getZ());
      tag.putInt("ground_level_delta", this.groundLevelDelta);
      DynamicOps<Tag> dynamicops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());
      StructurePoolElement.CODEC.encodeStart(dynamicops, this.element).resultOrPartial(LOGGER::error).ifPresent((p_163125_) -> {
         tag.put("pool_element", p_163125_);
      });
      tag.putString("rotation", this.rotation.name());
      ListTag listtag = new ListTag();

      for(JigsawJunction jigsawjunction : this.junctions) {
         listtag.add(jigsawjunction.serialize(dynamicops).getValue());
      }

      tag.put("junctions", listtag);
   }

   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
      this.place(level, structureManager, generator, random, box, pos, false);
   }

   public void place(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, BlockPos pos, boolean keepJigsaws) {
      this.element.place(this.structureTemplateManager, level, structureManager, generator, this.position, pos, this.rotation, box, random, keepJigsaws);
   }

   public void move(int x, int y, int z) {
      super.move(x, y, z);
      this.position = this.position.offset(x, y, z);
   }

   public Rotation getRotation() {
      return this.rotation;
   }

   public String toString() {
      return String.format(Locale.ROOT, "<%s | %s | %s | %s>", this.getClass().getSimpleName(), this.position, this.rotation, this.element);
   }

   public StructurePoolElement getElement() {
      return this.element;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public int getGroundLevelDelta() {
      return this.groundLevelDelta;
   }

   public void addJunction(JigsawJunction junction) {
      this.junctions.add(junction);
   }

   public List<JigsawJunction> getJunctions() {
      return this.junctions;
   }
}
