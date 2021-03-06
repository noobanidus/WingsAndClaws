package random.wings.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import random.wings.entity.TameableDragonEntity;
import random.wings.tileentity.NestTileEntity;

import javax.annotation.Nullable;

public class NestBlock<T extends NestTileEntity> extends ContainerBlock {
    private static final VoxelShape AABB = VoxelShapes.create(0.05, 0, 0.05, 0.95, 0.3, 0.95);
    private final Class<? extends TameableDragonEntity> entity;
    private final Class<T> tile;
    private TileEntityType<T> type;
    private Item item;

    public NestBlock(String name, Class<? extends TameableDragonEntity> entity, Class<T> tile) {
        super(Block.Properties.create(Material.SAND).sound(SoundType.SAND).hardnessAndResistance(1, 0));
        this.entity = entity;
        this.tile = tile;
        setRegistryName(name);
        WingsBlocks.LIST.add(this);
    }

    public void setItem(TileEntityType<T> value) {
        this.type = value;
    }

    public void setItem(Item value) {
        this.item = value;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return type.create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack stack = player.getHeldItem(handIn);
        TileEntity te = worldIn.getTileEntity(pos);
        if (tile.isInstance(te)) {
            if (stack.isEmpty()) {
                boolean removed = ((NestTileEntity) te).removeEgg();
                if (removed) player.setHeldItem(handIn, new ItemStack(item));
                return removed;
            } else if (stack.getItem() == item) {
                boolean added = ((NestTileEntity) te).addEgg();
                if (added) {
                    if (!player.abilities.isCreativeMode) stack.shrink(1);
                } else {
                    boolean removed = stack.getCount() < stack.getMaxStackSize() && ((NestTileEntity) te).removeEgg();
                    if (removed) stack.grow(1);
                    return removed;
                }
                return true;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (!player.abilities.isCreativeMode)
            worldIn.getEntitiesWithinAABB(entity, player.getBoundingBox().grow(32)).stream().filter(entity -> !entity.isChild() && entity.getGender() && !entity.isOwner(player) && !entity.isSleeping()).forEach(e -> e.setAttackTarget(player));
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
