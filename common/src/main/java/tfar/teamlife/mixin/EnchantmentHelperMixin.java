package tfar.teamlife.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.teamlife.TeamLife;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getEnchantmentLevel",at = @At("RETURN"),cancellable = true)
    private static void modifyLevel(Enchantment $$0, LivingEntity $$1, CallbackInfoReturnable<Integer> cir) {
        int level = cir.getReturnValue();
        if (TeamLife.boostEnchants($$0,$$1,level)) {
            cir.setReturnValue(level + 2);
        }
    }

    @Inject(method = "getItemEnchantmentLevel",at = @At("RETURN"),cancellable = true)
    private static void modifyLevel2(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int level = cir.getReturnValue();
        if (TeamLife.boostItemEnchants(enchantment,level)) {
            cir.setReturnValue(level + 2);
        }
    }


}
