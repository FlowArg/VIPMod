package fr.flowarg.vipium.common.items;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
            public float call(@Nonnull ItemStack stack, @Nullable World world, @Nullable LivingEntity entity)
            {
                if (entity == null || stack.isOnItemFrame()) return 0.0F;
                if (world == null) world = entity.world;

                final double[] vipiumPos = this.foundNearbyVipiumOre(entity.getBoundingBox(), world);
                this.blockX = vipiumPos[0];
                this.blockZ = vipiumPos[1];

                double rotation = entity.rotationYaw;
                rotation %= 360.0D;

                final double adjusted = this.wobble(world, Math.PI - ((this.rotation - 90.0D) * 0.01745329238474369D - this.getAngle(entity)));
                final float result = (float) (adjusted / (Math.PI * 2D));

                VipiumCompassItem.this.damageItem(VipiumCompassItem.this.getDefaultInstance(), 1, entity, null);
                return MathHelper.positiveModulo(result, 1.0F);
            }

            private double[] foundNearbyVipiumOre(@Nonnull AxisAlignedBB aabb, World world)
            {
                final double minX = aabb.minX - 100;
                final double minY = aabb.minY - 100;
                final double minZ = aabb.minZ - 100;
                final double maxX = aabb.maxX + 100;
                final double maxY = aabb.maxY + 100;
                final double maxZ = aabb.maxZ + 100;

                for (double x = minX; x < maxX; x++)
                {
                    for (double y = minY; y < maxY; y++)
                    {
                        for (double z = minZ; z < maxZ; z++)
                        {
                            final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                            if(block == RegistryHandler.VIPIUM_ORE.get())
                            {
                                return new double[]{x, z};
                            }
                        }
                    }
                }

                return new double[]{0, 0};
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
                    double tmp = amount - this.rotation;
                    tmp = MathHelper.positiveModulo(tmp + 0.5D, 1.0D) - 0.5D;
                    this.rota += tmp * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }
                return this.rotation;
            }
        });
    }
}
