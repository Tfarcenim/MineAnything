package tfar.mineanything.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.Utils;
import tfar.mineanything.world.InputHandler;

public class DragonElytraItem extends Item implements Equipable {
    public DragonElytraItem(Properties $$0) {
        super($$0);
    }

    /*
     * Jetpack logic is very much like Simply Jetpacks, since I used it to learn how to make this work
     * Credit to Tonius & Tomson124
     * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/ItemJetpack.java
     */
    //@Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        var item = stack.getItem();

        if (Utils.isFlightOn(stack)) {
            boolean hover = Utils.isHovering(stack);
            boolean up = InputHandler.isHoldingUp(player);

            if (up || hover && !player.onGround()) {
                var jetpack = Utils.getJetpack(stack);


                double motionY = player.getDeltaMovement().y();
                double hoverSpeed = InputHandler.isHoldingDown(player) ? jetpack.speedHoverDescend : jetpack.speedHoverSlow;
                double currentAccel = jetpack.accelVert * (motionY < 0.3D ? 2.5D : 1.0D);
                double currentSpeedVertical = jetpack.speedVert * (player.isInWater() ? 0.4D : 1.0D);

                double usage = player.isSprinting() || InputHandler.isHoldingSprint(player) ? jetpack.usage * jetpack.sprintFuel : jetpack.usage;

                var creative = jetpack.creative;
            //    var energy = JetpackUtils.getEnergyStorage(stack);

             //   if (!player.isCreative() && !creative) {
              //      energy.extractEnergy((int) usage, false);
              //  }

                if (hover && player.isFallFlying()) {
                    player.stopFallFlying();
                }

                if (stack.getDamageValue() < stack.getMaxDamage()-1 || player.isCreative() || creative) {
                    double throttle = 1;//JetpackUtils.getThrottle(stack);
                    double verticalSprintMulti = motionY >= 0 && InputHandler.isHoldingSprint(player) ? jetpack.sprintSpeedVert : 1.0D;

                    if (up) {
                        if (!hover) {
                            fly(player, Math.min(motionY + currentAccel, currentSpeedVertical) * throttle * verticalSprintMulti);
                        } else {
                            if (InputHandler.isHoldingDown(player)) {
                                fly(player, Math.min(motionY + currentAccel, -jetpack.speedHoverSlow));
                            } else {
                                fly(player, Math.min(motionY + currentAccel, jetpack.speedHoverAscend) * throttle * verticalSprintMulti);
                            }
                        }
                    } else {
                        fly(player, Math.min(motionY + currentAccel, -hoverSpeed));
                    }

                    double speedSideways = (player.isCrouching() ? jetpack.speedSide * 0.5F : jetpack.speedSide) * throttle;
                    double speedForward = (player.isSprinting() ? speedSideways * jetpack.sprintSpeed : speedSideways) * throttle;

                    if (!player.isFallFlying()) {
                        if (InputHandler.isHoldingForwards(player)) {
                            player.moveRelative(1, new Vec3(0, 0, speedForward));
                        }

                        if (InputHandler.isHoldingBackwards(player)) {
                            player.moveRelative(1, new Vec3(0, 0, -speedSideways * 0.8F));
                        }

                        if (InputHandler.isHoldingLeft(player)) {
                            player.moveRelative(1, new Vec3(speedSideways, 0, 0));
                        }

                        if (InputHandler.isHoldingRight(player)) {
                            player.moveRelative(1, new Vec3(-speedSideways, 0, 0));
                        }
                    }

                    if (!level.isClientSide()) {
                        player.fallDistance = 0.0F;

                        if (player instanceof ServerPlayer) {
                            ((ServerPlayer) player).connection.aboveGroundTickCount = 0;
                        }
                    }
                }
            }
        }
    }

    private static void fly(Player player, double y) {
        var motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x(), y, motion.z());
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}
