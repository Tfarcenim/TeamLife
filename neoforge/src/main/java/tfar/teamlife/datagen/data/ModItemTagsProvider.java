package tfar.teamlife.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModItems;
import tfar.teamlife.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, TeamLife.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        tag(ModTags.CHESTPLATES_WITH_ELYTRA).add(ModItems.LEATHER_CHESTPLATE_WITH_ELYTRA,ModItems.CHAINMAIL_CHESTPLATE_WITH_ELYTRA,ModItems.GOLDEN_CHESTPLATE_WITH_ELYTRA,
                ModItems.IRON_CHESTPLATE_WITH_ELYTRA,ModItems.DIAMOND_CHESTPLATE_WITH_ELYTRA,ModItems.NETHERITE_CHESTPLATE_WITH_ELYTRA);
        tag(ItemTags.DYEABLE).add(ModItems.LEATHER_CHESTPLATE_WITH_ELYTRA);
    }
}
