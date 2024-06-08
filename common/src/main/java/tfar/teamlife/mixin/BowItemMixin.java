package tfar.teamlife.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.teamlife.TeamLife;

@Mixin(ProjectileWeaponItem.class)
public class BowItemMixin {

    @Inject(method = "createProjectile",at = @At("HEAD"))
    private void hookPlayer(Level $$0, LivingEntity living, ItemStack $$2, ItemStack $$3, boolean $$4, CallbackInfoReturnable<Projectile> cir) {
        if (living instanceof Player player) {
            TeamLife.playerThreadLocal.set(player);
        } else {
            TeamLife.playerThreadLocal.remove();
        }
    }
}
