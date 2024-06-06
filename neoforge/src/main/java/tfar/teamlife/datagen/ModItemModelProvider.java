package tfar.teamlife.datagen;

import com.google.gson.JsonElement;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModItems;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelGenerators {

    public ModItemModelProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> pOutput) {
        super(pOutput);
    }

    private static ModelTemplate create(String p_125731_, TextureSlot... p_125732_) {
        return new ModelTemplate(Optional.of(new ResourceLocation(TeamLife.MOD_ID, "block/" + p_125731_)), Optional.empty(), p_125732_);
    }

    ModelTemplate P_TEMPLATE = create("pedestal");


    @Override
    public void run() {

        Datagen.getKnownItems().forEach(item -> {
            if (!(item instanceof BlockItem)) {
                generateFlatItem(item,ModelTemplates.FLAT_ITEM);
            }
        });
        generateFlatItem(ModItems.PEDESTAL,P_TEMPLATE);
    }
}
