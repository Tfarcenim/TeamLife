package tfar.teamlife.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tfar.teamlife.blockentity.PedestalBlockEntity;
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

import java.util.UUID;

public class PedestalBlock extends Block implements EntityBlock {
    public PedestalBlock(Properties $$0) {
        super($$0);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PedestalBlockEntity(blockPos,blockState);
    }

    public static final VoxelShape SHAPE = Block.box(2,0,2,14,16,14);

    @Override
    protected VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity && placer instanceof ServerPlayer serverPlayer) {
            ModTeamsServer modTeamsServer = ModTeamsServer.getInstance((ServerLevel) level);
            if (modTeamsServer != null) {
                ModTeam modTeam = modTeamsServer.findTeam(serverPlayer);
                if (modTeam != null) {
                    pedestalBlockEntity.setOwners(modTeam.uuid);
                }
            }
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState $$3, boolean $$4) {

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {

            if (!level.isClientSide) {
                Containers.dropItemStack(level,pos.getX(),pos.getY(),pos.getZ(),pedestalBlockEntity.item);
                pedestalBlockEntity.onItemChange(pedestalBlockEntity.item.getItem(), ItemStack.EMPTY.getItem());

            }

        }

        super.onRemove(state, level, pos, $$3, $$4);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState $$1, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult $$6) {

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
            if (!level.isClientSide) {
                ModTeamsServer modTeamsServer = ModTeamsServer.getInstance((ServerLevel) level);
                if (modTeamsServer != null) {
                    ModTeam modTeam = modTeamsServer.findTeam((ServerPlayer) player);
                    if (modTeam != null) {
                    }
                }
                if (pedestalBlockEntity.item.isEmpty()) {
                    pedestalBlockEntity.setItem(itemStack.copy());
                    player.setItemInHand(hand,ItemStack.EMPTY);
                } else {
                    if (player.getItemInHand(hand).isEmpty()) {
                        player.setItemInHand(hand,pedestalBlockEntity.item.copy());
                        pedestalBlockEntity.setItem(ItemStack.EMPTY);
                    }
                }
                return ItemInteractionResult.CONSUME;
            }
        }

        return ItemInteractionResult.SUCCESS;
    }
}
