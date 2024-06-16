package tfar.teamlife.item;

import net.minecraft.world.item.Item;
import tfar.teamlife.init.ModItems;

import java.util.Set;
import java.util.function.Supplier;

public class ArtifactItem extends Item implements Artifact {
    private final Supplier<Set<Item>> usable;

    public ArtifactItem(Properties $$0, Supplier<Set<Item>> usable) {
        super($$0);
        this.usable = usable;
    }

    public static ArtifactItem enchantedTome(Properties properties) {
        ArtifactItem artifactItem = new ArtifactItem(properties, () -> Set.of(ModItems.TEAM_REGENERATION, ModItems.TEAM_REGENERATION_ARTIFACT));
        return artifactItem;
    }

    public Item getCraftingRemainingItem() {
        return this == ModItems.TEAM_REGENERATION_ARTIFACT || this == ModItems.CHESTPLATE_WITH_ELYTRA_ARTIFACT ? this : super.getCraftingRemainingItem();
    }

    @Override
    public Set<Item> usable() {
        return usable.get();
    }
}
