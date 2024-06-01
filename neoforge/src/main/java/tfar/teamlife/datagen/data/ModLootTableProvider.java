package tfar.teamlife.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput pOutput, Set<ResourceKey<LootTable>> pRequiredTables, List<SubProviderEntry> pSubProviders, CompletableFuture<HolderLookup.Provider> lookup) {
        super(pOutput, pRequiredTables, pSubProviders,lookup);
    }

    public static LootTableProvider create(PackOutput pOutput,CompletableFuture<HolderLookup.Provider> lookup) {
        return new ModLootTableProvider(pOutput, BuiltInLootTables.all(), List.of(new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)),lookup);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
    }
}
