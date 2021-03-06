package random.wings.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import random.wings.entity.TameableDragonEntity;
import random.wings.entity.WingsEntities;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class MimangoEntity extends TameableDragonEntity {
    private static final DataParameter<Boolean> HIDDEN = EntityDataManager.createKey(MimangoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(MimangoEntity.class, DataSerializers.VARINT);
    private static final Ingredient TEMPT_ITEM = Ingredient.fromItems(Items.COCOA_BEANS);

    public MimangoEntity(EntityType<? extends MimangoEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new TemptGoal(this, 0.2, TEMPT_ITEM, true));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(HIDDEN, true);
        this.dataManager.register(VARIANT, 0);
    }

    public boolean isHidden() {
        return this.dataManager.get(HIDDEN);
    }

    public void setHidden(boolean hidden) {
        this.dataManager.set(HIDDEN, hidden);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (isHidden()) setHidden(false);
        return super.attackEntityAsMob(entityIn);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.COCOA_BEANS;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        setVariant(rand.nextInt(5));
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!isHidden() && !isTamed() && stack.getItem() == Items.MELON_SEEDS) {
            if (rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                this.setTamedBy(player);
                this.navigator.clearPath();
                this.setAttackTarget(null);
                this.setHealth(20.0F);
                this.playTameEffect(true);
                this.world.setEntityState(this, (byte) 7);
            } else {
                this.playTameEffect(false);
                this.world.setEntityState(this, (byte) 6);
            }
        }
        return super.processInteract(player, hand);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        MimangoEntity mimango = Objects.requireNonNull(WingsEntities.MIMANGO.create(this.world));
        UUID uuid = this.getOwnerId();
        if (uuid != null) {
            mimango.setOwnerId(uuid);
            mimango.setTamed(true);
        }
        return mimango;
    }

    public static boolean canSpawn(EntityType<MimangoEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
        Block block = world.getBlockState(pos.down()).getBlock();
        return block.isIn(BlockTags.LEAVES) && world.getLightSubtracted(pos, 0) > 8;
    }
}
