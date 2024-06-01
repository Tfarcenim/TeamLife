package tfar.teamlife.datagen.data;

import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import tfar.teamlife.datagen.Datagen;

public class ModBlockLoot extends VanillaBlockLoot {

    @Override
    protected void generate() {

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Datagen.getKnownBlocks().toList();
    }
}
