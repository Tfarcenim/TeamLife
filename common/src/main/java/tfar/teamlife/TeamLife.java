package tfar.teamlife;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.teamlife.client.TeamLifeClient;
import tfar.teamlife.init.*;
import tfar.teamlife.item.PersonalHeartItem;
import tfar.teamlife.platform.Services;
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class TeamLife {

    public static final String MOD_ID = "teamlife";
    public static final String MOD_NAME = "TeamLife";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        Services.PLATFORM.registerAll(ModBlocks.class, BuiltInRegistries.BLOCK, Block.class);
        Services.PLATFORM.registerAll(ModBlockEntities.class, BuiltInRegistries.BLOCK_ENTITY_TYPE, BlockEntityType.class);
        Services.PLATFORM.registerAll(ModItems.class, BuiltInRegistries.ITEM, Item.class);
        Services.PLATFORM.registerAll(ModMenus.class, BuiltInRegistries.MENU, MenuType.class);
        Services.PLATFORM.registerAll(ModDataComponents.class, BuiltInRegistries.DATA_COMPONENT_TYPE, DataComponentType.class);

    }

    //copy personal hearts attribute
    public static void playerClone(Player oldPlayer, Player newPlayer, boolean wasDeath) {
        AttributeInstance attributeInstanceOld = oldPlayer.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance attributeInstanceNew = newPlayer.getAttribute(Attributes.MAX_HEALTH);
        if (attributeInstanceOld != null && attributeInstanceNew != null) {
            AttributeModifier attributeModifierOld = attributeInstanceOld.getModifier(PersonalHeartItem.uuid);
            if (attributeModifierOld != null) {
                attributeInstanceNew.addPermanentModifier(attributeModifierOld);
            }
        }
    }

    //team hearts take damage first
    //return damage NOT blocked by team hearts
    public static float onDamageEvent(LivingEntity living, DamageSource damageSource,float amount) {
        if (living instanceof ServerPlayer serverPlayer) {
            ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(serverPlayer.server);
            if (modTeamsServer != null) {
                ModTeam modTeam = modTeamsServer.findTeam(serverPlayer);
                if (modTeam != null) {
                    //health is higher than damage
                    if (modTeam.health >= amount) {
                        modTeamsServer.adjustHealth(modTeam,-amount);
                        return 0;
                        //damage is higher than health
                    } else {
                        float teamHealth = modTeam.health;
                        modTeamsServer.adjustHealth(modTeam,-teamHealth);
                        return amount - teamHealth;
                    }
                }
            }
        }
        return amount;
    }

    public static boolean onAttackEvent(LivingEntity living, DamageSource damageSource,float amount) {
        if (living instanceof ServerPlayer serverPlayer) {
            if ((damageSource.is(DamageTypes.FALL) || damageSource.is(DamageTypes.FLY_INTO_WALL)) && serverPlayer.getInventory().countItem(ModItems.INFINITE_FIREWORK_ROCKET) > 0) {
                return true;
            }
        }
        return false;
    }

    //heal team hearts first
    //return healing unused
    public static float onHealEvent(LivingEntity living, float amount) {
        if (living instanceof ServerPlayer serverPlayer) {
            ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(serverPlayer.server);
            if (modTeamsServer != null) {
                ModTeam modTeam = modTeamsServer.findTeam(serverPlayer);
                if (modTeam != null) {
                    //healing will not go above max team health
                    if (modTeam.maxHealth >= (amount + modTeam.health)) {
                        modTeamsServer.adjustHealth(modTeam,amount);
                        return 0;
                        //healing will go above max team health
                    } else {
                        float teamHealth = modTeam.health;
                        modTeamsServer.adjustHealth(modTeam,modTeam.maxHealth);
                        return amount - (modTeam.maxHealth - teamHealth);
                    }
                }
            }
        }
        return amount;
    }

    public static void onPlayerTick(Player player) {
    }

    public static void login(ServerPlayer player) {
        ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(player.server);
        if (modTeamsServer != null) {
            modTeamsServer.updateClient(player);
        }
    }

    @Nullable
    public static ModTeam getTeamSideSafe(@Nullable Player player) {
        if (player == null) return null;
        if (player.level().isClientSide) {
            return TeamLifeClient.getTeam();
        } else {
            ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(player.getServer());
            if (modTeamsServer != null) {
                return modTeamsServer.findTeam((ServerPlayer) player);
            }
            return null;
        }
    }

    public static boolean boostEnchants(Enchantment enchantment,LivingEntity living,int originalLevel) {
        if (originalLevel <= 0) return false;
        if (enchantment.getMaxLevel() == 1) return false;
        if (enchantment ==  Enchantments.LURE) return false;
        if (living instanceof Player player) {
            if (!canUseArtifact(getTeamSideSafe(player),ModItems.ENCHANTMENT_TOME_ARTIFACT)) return false;
            if (!canUseArtifact(getTeamSideSafe(player),ModItems.ENCHANTMENT_TOME)) return false;

            if (player.getInventory().countItem(ModItems.ENCHANTMENT_TOME) > 0) {
                return true;
            }

            if (player.getInventory().countItem(ModItems.ENCHANTMENT_TOME_ARTIFACT) > 0) {
                return true;
            }

        }
        return false;
    }

    public static boolean canUseArtifact(@Nullable ModTeam modTeam,Item artifactItem) {
        return modTeam != null && modTeam.usableArtifacts.contains(artifactItem);
    }

    public static boolean canPlayerUseArtifact(Player player,Item item) {
        return canUseArtifact(getTeamSideSafe(player),item);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }


}
//1 new feature
//11 new items
//1 new block
//
//I am looking for a mod to make teams and a central health bar. It should lets me have four teams with one health bar each.
// Each teams health bar replaces the normal health bar each player has so if one person takes damage all people on the team take damage.
//
//New items/blocks: personal hearts, team hearts, chest plate with elytra, portable beacon, team inventory, enchant multiplier 1.5,
// infant rockets with no fall damage, Nether core, Overworld core, End core, Center team peace, and artifact pedestal