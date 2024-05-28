package tfar.mineanything.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import tfar.mineanything.entity.ClonePlayerEntity;

public class ClonePlayerEntityRenderer extends LivingEntityRenderer<ClonePlayerEntity, PlayerModel<ClonePlayerEntity>> {
    public ClonePlayerEntityRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidArmorModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new ArrowLayer<>(context, this));
        //this.addLayer(new Deadmau5EarsLayer(this));
       // this.addLayer(new CapeLayer(this));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
        //this.addLayer(new ParrotOnShoulderLayer<>(this, context.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer<>(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this));
    }

    @Override
    public void render(ClonePlayerEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        this.setModelProperties($$0);
        super.render($$0, $$1, $$2, $$3, $$4, $$5);
    }

    @Override
    protected boolean shouldShowName(ClonePlayerEntity entity) {
        return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public Vec3 getRenderOffset(ClonePlayerEntity $$0, float $$1) {
        return $$0.isCrouching() ? new Vec3(0.0, -0.125, 0.0) : super.getRenderOffset($$0, $$1);
    }

    private void setModelProperties(ClonePlayerEntity entity_303) {
        PlayerModel<ClonePlayerEntity> model1 = this.getModel();
        if (entity_303.isSpectator()) {
            model1.setAllVisible(false);
            model1.head.visible = true;
            model1.hat.visible = true;
        } else {
            model1.setAllVisible(true);
            model1.hat.visible = true;//entity_303.isModelPartShown(PlayerModelPart.HAT);
            model1.jacket.visible = true; //entity_303.isModelPartShown(PlayerModelPart.JACKET);
            model1.leftPants.visible = true; //entity_303.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            model1.rightPants.visible = true; //entity_303.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            model1.leftSleeve.visible = true; //entity_303.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            model1.rightSleeve.visible = true; //entity_303.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            model1.crouching = entity_303.isCrouching();
            HumanoidModel.ArmPose $$2 = getArmPose(entity_303, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose $$3 = getArmPose(entity_303, InteractionHand.OFF_HAND);
            if ($$2.isTwoHanded()) {
                $$3 = entity_303.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }

            if (entity_303.getMainArm() == HumanoidArm.RIGHT) {
                model1.rightArmPose = $$2;
                model1.leftArmPose = $$3;
            } else {
                model1.rightArmPose = $$3;
                model1.leftArmPose = $$2;
            }
        }

    }

    private static HumanoidModel.ArmPose getArmPose(LivingEntity entity_303, InteractionHand $$1) {
        ItemStack $$2 = entity_303.getItemInHand($$1);
        if ($$2.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (entity_303.getUsedItemHand() == $$1 && entity_303.getUseItemRemainingTicks() > 0) {
                UseAnim $$3 = $$2.getUseAnimation();
                if ($$3 == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if ($$3 == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if ($$3 == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if ($$3 == UseAnim.CROSSBOW && $$1 == entity_303.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if ($$3 == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if ($$3 == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }
            } else if (!entity_303.swinging && $$2.is(Items.CROSSBOW) && CrossbowItem.isCharged($$2)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            return HumanoidModel.ArmPose.ITEM;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(ClonePlayerEntity clonePlayer) {
        return clonePlayer.getSkinTextureLocation();
    }

    @Override
    protected void scale(ClonePlayerEntity $$0, PoseStack $$1, float $$2) {
        float $$3 = 0.9375F;
        $$1.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public void renderRightHand(PoseStack $$0, MultiBufferSource $$1, int $$2, ClonePlayerEntity $$3) {
        this.renderHand($$0, $$1, $$2, $$3, this.model.rightArm, this.model.rightSleeve);
    }

    public void renderLeftHand(PoseStack $$0, MultiBufferSource $$1, int $$2, ClonePlayerEntity $$3) {
        this.renderHand($$0, $$1, $$2, $$3, this.model.leftArm, this.model.leftSleeve);
    }

    private void renderHand(PoseStack $$0, MultiBufferSource $$1, int $$2, ClonePlayerEntity entity_303, ModelPart $$4, ModelPart $$5) {
        PlayerModel<ClonePlayerEntity> model1 = this.getModel();
        this.setModelProperties(entity_303);
        model1.attackTime = 0.0F;
        model1.crouching = false;
        model1.swimAmount = 0.0F;
        model1.setupAnim(entity_303, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        $$4.xRot = 0.0F;
        $$4.render($$0, $$1.getBuffer(RenderType.entitySolid(getTextureLocation(entity_303))), $$2, OverlayTexture.NO_OVERLAY);
        $$5.xRot = 0.0F;
        $$5.render($$0, $$1.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity_303))), $$2, OverlayTexture.NO_OVERLAY);
    }

    @Override
    protected void setupRotations(ClonePlayerEntity pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        float f = pEntityLiving.getSwimAmount(pPartialTicks);
        if (pEntityLiving.isFallFlying()) {
            super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
            float f1 = (float)pEntityLiving.getFallFlyingTicks() + pPartialTicks;
            float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!pEntityLiving.isAutoSpinAttack()) {
                pPoseStack.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - pEntityLiving.getXRot())));
            }

            Vec3 vec3 = pEntityLiving.getViewVector(pPartialTicks);
            Vec3 vec31 = pEntityLiving.getDeltaMovement();
            double d0 = vec31.horizontalDistanceSqr();
            double d1 = vec3.horizontalDistanceSqr();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
                double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
                pPoseStack.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
            float f3 = pEntityLiving.isInWater() /*|| pEntityLiving.isInFluidType((fluidType, height) -> pEntityLiving.canSwimInFluidType(fluidType))*/ ? -90.0F - pEntityLiving.getXRot() : -90.0F;
            float f4 = Mth.lerp(f, 0.0F, f3);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(f4));
            if (pEntityLiving.isVisuallySwimming()) {
                pPoseStack.translate(0.0F, -1.0F, 0.3F);
            }
        } else {
            super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        }

    }

}
