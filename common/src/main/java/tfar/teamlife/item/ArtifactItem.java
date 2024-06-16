package tfar.teamlife.item;

import net.minecraft.world.item.Item;

import java.util.Set;
import java.util.function.Supplier;

import static tfar.teamlife.init.ModItems.TEAM_REGENERATION;
import static tfar.teamlife.init.ModItems.TEAM_REGENERATION_ARTIFACT;

public class ArtifactItem extends Item implements Artifact {
    private final Supplier<Set<Item>> usable;

    public ArtifactItem(Properties $$0, Supplier<Set<Item>> usable) {
        super($$0);
        this.usable = usable;
    }

    public static ArtifactItem enchantedTome(Properties properties) {
        ArtifactItem artifactItem = new ArtifactItem(properties, () -> Set.of(TEAM_REGENERATION, TEAM_REGENERATION_ARTIFACT));
        return artifactItem;
    }

    @Override
    public Set<Item> usable() {
        return usable.get();
    }
}
