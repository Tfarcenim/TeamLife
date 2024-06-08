package tfar.teamlife.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tfar.teamlife.TeamLife;
import tfar.teamlife.datagen.data.ModBlockTagsProvider;
import tfar.teamlife.datagen.data.ModItemTagsProvider;
import tfar.teamlife.datagen.data.ModLootTableProvider;
import tfar.teamlife.datagen.data.ModRecipeProvider;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class Datagen {

    public static void gather(GatherDataEvent event) {
        boolean client = event.includeClient();
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        dataGenerator.addProvider(client,new ModModelProvider(packOutput));
        dataGenerator.addProvider(client,new ModLangProvider(packOutput));
        dataGenerator.addProvider(true, ModLootTableProvider.create(packOutput,lookupProvider));
        BlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(packOutput,lookupProvider,existingFileHelper);
        dataGenerator.addProvider(true,blockTagsProvider);
        dataGenerator.addProvider(true,new ModItemTagsProvider(packOutput,lookupProvider,blockTagsProvider.contentsGetter(),existingFileHelper));
        dataGenerator.addProvider(true,new ModRecipeProvider(packOutput,lookupProvider));

    }

    public static Stream<Block> getKnownBlocks() {
        return getKnown(BuiltInRegistries.BLOCK);
    }
    public static Stream<Item> getKnownItems() {
        return getKnown(BuiltInRegistries.ITEM);
    }

    public static <V> Stream<V> getKnown(Registry<V> registry) {
        return registry.stream().filter(o -> registry.getKey(o).getNamespace().equals(TeamLife.MOD_ID));
    }
}
