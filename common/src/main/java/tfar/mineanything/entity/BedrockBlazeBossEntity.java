package tfar.mineanything.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class BedrockBlazeBossEntity extends Blaze {
    public BedrockBlazeBossEntity(EntityType<? extends Blaze> $$0, Level $$1) {
        super($$0, $$1);
    }


    public static AttributeSupplier.Builder createBossAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.MAX_HEALTH,100);
    }



    protected void registerGoals() {
        this.goalSelector.addGoal(4, new TripleBlazeAttackGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }


    public static class TripleBlazeAttackGoal extends Goal {
        private final Blaze blaze;
        private int attackStep;
        private int attackTime;
        private int lastSeen;

        public TripleBlazeAttackGoal(Blaze $$0) {
            this.blaze = $$0;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity $$0 = this.blaze.getTarget();
            return $$0 != null && $$0.isAlive() && this.blaze.canAttack($$0);
        }

        public void start() {
            this.attackStep = 0;
        }

        public void stop() {
            this.blaze.setCharged(false);
            this.lastSeen = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            --this.attackTime;
            LivingEntity target = this.blaze.getTarget();
            if (target != null) {
                boolean canSee = this.blaze.getSensing().hasLineOfSight(target);
                if (canSee) {
                    this.lastSeen = 0;
                } else {
                    ++this.lastSeen;
                }

                double distSq = this.blaze.distanceToSqr(target);
                if (distSq < 4.0) {
                    if (!canSee) {
                        return;
                    }

                    if (this.attackTime <= 0) {
                        this.attackTime = 20;
                        this.blaze.doHurtTarget(target);
                    }

                    this.blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0);
                } else if (distSq < this.getFollowDistance() * this.getFollowDistance() && canSee) {
                    double fireballX = target.getX() - this.blaze.getX();
                    double fireballY = target.getY(0.5) - this.blaze.getY(0.5);
                    double fireballZ = target.getZ() - this.blaze.getZ();
                    if (this.attackTime <= 0) {
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = 60;
                            this.blaze.setCharged(true);
                        } else if (this.attackStep <= 4) {
                            this.attackTime = 6;
                        } else {
                            this.attackTime = 100;
                            this.attackStep = 0;
                            this.blaze.setCharged(false);
                        }

                        if (this.attackStep > 1) {
                            double $$6 = Math.sqrt(Math.sqrt(distSq)) * 0.5;
                            if (!this.blaze.isSilent()) {
                                this.blaze.level().levelEvent(null, 1018, this.blaze.blockPosition(), 0);
                            }

                            for(int $$7 = 0; $$7 < 1; ++$$7) {
                                SmallFireball fireball = new SmallFireball(this.blaze.level(), this.blaze, this.blaze.getRandom().triangle(fireballX, 2.297 * $$6), fireballY, this.blaze.getRandom().triangle(fireballZ, 2.297 * $$6));
                                fireball.setPos(fireball.getX(), this.blaze.getY(0.5) + 0.5, fireball.getZ());
                                this.blaze.level().addFreshEntity(fireball);
                            }

                            for (int i = 0 ; i < 2;i++) {

                                double randX = (blaze.getRandom().nextDouble() -.5) *.25;
                                double randZ = (blaze.getRandom().nextDouble() -.5) *.25;

                                SmallFireball fireball = new SmallFireball(this.blaze.level(), this.blaze, randX, fireballY,randZ);
                                fireball.setPos(fireball.getX(), this.blaze.getY(0.5) + 0.5, fireball.getZ());
                                this.blaze.level().addFreshEntity(fireball);
                            }
                        }
                    }

                    this.blaze.getLookControl().setLookAt(target, 10.0F, 10.0F);
                } else if (this.lastSeen < 5) {
                    this.blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0);
                }

                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }

}
