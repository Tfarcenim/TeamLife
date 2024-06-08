package tfar.teamlife.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import tfar.teamlife.block.PedestalBlock;

public class ModBlocks {
    public static final Block PEDESTAL = new PedestalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.STONE).pushReaction(PushReaction.BLOCK));
}
