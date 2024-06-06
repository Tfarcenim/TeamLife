package tfar.teamlife.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState $$1, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult $$6) {

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
            if (!level.isClientSide) {
                UUID teamUUID = null;
                ModTeamsServer modTeamsServer = ModTeamsServer.getInstance((ServerLevel) level);
                if (modTeamsServer != null) {
                    ModTeam modTeam = modTeamsServer.findTeam((ServerPlayer) player);
                    if (modTeam != null) {
                        teamUUID = modTeam.uuid;
                    }
                }
                if (pedestalBlockEntity.item.isEmpty()) {
                    pedestalBlockEntity.setItem(teamUUID,itemStack.copy());
                    player.setItemInHand(hand,ItemStack.EMPTY);
                } else {
                    if (player.getItemInHand(hand).isEmpty()) {
                        player.setItemInHand(hand,pedestalBlockEntity.item.copy());
                        pedestalBlockEntity.setItem(teamUUID,ItemStack.EMPTY);
                    }
                }
                return ItemInteractionResult.CONSUME;
            }
        }

        return ItemInteractionResult.SUCCESS;
    }
}
