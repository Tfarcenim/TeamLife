package tfar.teamlife.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import tfar.teamlife.blockentity.PedestalBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL = BlockEntityType.Builder.of(PedestalBlockEntity::new,ModBlocks.PEDESTAL).build(null);

}
