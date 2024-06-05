package tfar.teamlife.datagen.data;

import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import tfar.teamlife.datagen.Datagen;
import tfar.teamlife.init.ModBlocks;

public class ModBlockLoot extends VanillaBlockLoot {

    @Override
    protected void generate() {
        dropSelf(ModBlocks.PEDESTAL);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Datagen.getKnownBlocks().toList();
    }
}
