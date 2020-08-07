package fr.flowarg.vipium.common.items;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import fr.flowarg.vipium.common.utils.XZStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VipiumCompassItem extends Item
{
    public VipiumCompassItem()
    {
        super(new Properties()
                .group(Main.ITEM_GROUP)
                .maxStackSize(1)
                .defaultMaxDamage((20 * 60) * 15)
                .maxDamage((20 * 60) * 15)
                .rarity(Rarity.RARE));

        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
        {
            @OnlyIn(Dist.CLIENT)
            double rotation;
            @OnlyIn(Dist.CLIENT)
            double rota;
            @OnlyIn(Dist.CLIENT)
            long lastUpdateTick;

            double blockX = 0;
            double blockZ = 0;

            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entityLiving)
            {
                if (entityLiving == null && !stack.isOnItemFrame()) return 0.0F;
                final boolean entityExists = entityLiving != null;
                final Entity  entity       = entityExists ? entityLiving : stack.getItemFrame();
                if (world == null && entity != null) world = entity.world;

                assert entity != null;
                final XZStorage vipiumPos = this.foundNearbyVipiumOre(entity.getBoundingBox(), world);
                this.blockX = vipiumPos.getX();
                this.blockZ = vipiumPos.getZ();

                double rotation = entityExists ? (double) entity.rotationYaw : this.getFrameRotation((ItemFrameEntity) entity);
                rotation %= 360.0D;
                double adjusted = Math.PI - ((rotation - 90.0D) * 0.01745329238474369D - this.getAngle(entity));
                if (entityExists) adjusted = this.wobble(world, adjusted);

                final float f = (float) (adjusted / (Math.PI * 2D));

                VipiumCompassItem.this.damageItem(VipiumCompassItem.this.getDefaultInstance(), 1, entityLiving, null);

                return MathHelper.positiveModulo(f, 1.0F);
            }

            private XZStorage foundNearbyVipiumOre(@Nonnull AxisAlignedBB aabb, World world)
            {
                final double minX = aabb.minX - 150;
                final double minY = aabb.minY - 150;
                final double minZ = aabb.minZ - 150;
                final double maxX = aabb.maxX + 150;
                final double maxY = aabb.maxY + 150;
                final double maxZ = aabb.maxZ + 150;

                for (double x = minX; x < maxX; x++)
                {
                    for (double y = minY; y < maxY; y++)
                    {
                        for (double z = minZ; z < maxZ; z++)
                        {
                            final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                            if(block == RegistryHandler.VIPIUM_ORE.get())
                            {
                                return new XZStorage(x, z);
                            }
                        }
                    }
                }

                return new XZStorage(0, 0);
            }

            @OnlyIn(Dist.CLIENT)
            private double getFrameRotation(ItemFrameEntity itemFrame)
            {
                return MathHelper.wrapDegrees(180 + itemFrame.getHorizontalFacing().getHorizontalIndex() * 90);
            }

            @OnlyIn(Dist.CLIENT)
            private double getAngle(@Nonnull Entity entity)
            {
                return Math.atan2(this.blockZ - entity.getPosZ(), this.blockX - entity.getPosX());
            }

            @OnlyIn(Dist.CLIENT)
            private double wobble(@Nonnull World world, double amount)
            {
                if (world.getGameTime() != this.lastUpdateTick)
                {
                    this.lastUpdateTick = world.getGameTime();
                    double d = amount - this.rotation;
                    d %= Math.PI * 2D;
                    d = MathHelper.clamp(d, -1.0D, 1.0D);
                    this.rota += d * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation += rota;
                }
                return this.rotation;
            }
        });
    }
}
