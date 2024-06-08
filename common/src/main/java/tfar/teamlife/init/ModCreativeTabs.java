package tfar.teamlife.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import tfar.teamlife.TeamLife;

public class ModCreativeTabs {

    public static final CreativeModeTab TAB = CreativeModeTab.builder(null,-1)
            .icon(() -> new ItemStack(ModItems.PEARL_OF_LIFE))
            .title(Component.literal(TeamLife.MOD_NAME))
            .displayItems(
                    (itemDisplayParameters, output) -> {
                        BuiltInRegistries.ITEM.stream().filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(TeamLife.MOD_ID)).forEach(item -> output.accept(item));
                    }
            ).build();

}
