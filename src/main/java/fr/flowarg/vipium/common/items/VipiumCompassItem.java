package fr.flowarg.vipium.common.items;

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
                //.group(Main.ITEM_GROUP)
                .maxStackSize(1)
                .rarity(Rarity.RARE));

        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
        {
            @OnlyIn(Dist.CLIENT)
            double rotation;
            @OnlyIn(Dist.CLIENT)
            double rota;
            @OnlyIn(Dist.CLIENT)
            long lastUpdateTick;

            @OnlyIn(Dist.CLIENT)
            final BlockPos.Mutable mutable = new BlockPos.Mutable();

            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(@Nonnull ItemStack stack, @Nullable World world, @Nullable LivingEntity entity)
            {
                if (entity == null || stack.isOnItemFrame()) return 0.0F;
                if (world == null) world = entity.world;

                this.foundNearbyVipiumOre(entity.getBoundingBox(), world);

                double rotation = entity.rotationYaw;
                rotation %= 360.0D;

                final double adjusted = this.wobble(world, Math.PI - ((rotation - 90.0D) * 0.01745329238474369D - this.getAngle(entity)));
                final float result = (float) (adjusted / (Math.PI * 2D));

                return MathHelper.positiveModulo(result, 1.0F);
            }

            @OnlyIn(Dist.CLIENT)
            private void foundNearbyVipiumOre(@Nonnull AxisAlignedBB aabb, World world)
            {
                final double minX = aabb.minX - 50;
                final double minY = aabb.minY - 50;
                final double minZ = aabb.minZ - 50;
                final double maxX = aabb.maxX + 50;
                final double maxY = aabb.maxY + 50;
                final double maxZ = aabb.maxZ + 50;

                for (double x = minX; x < maxX; x++)
                {
                    this.mutable.setX((int)x);
                    for (double y = minY; y < maxY; y++)
                    {
                        this.mutable.setY((int)y);
                        for (double z = minZ; z < maxZ; z++)
                        {
                            this.mutable.setZ((int)z);
                            final Block block = world.getBlockState(this.mutable).getBlock();
                            if(block == RegistryHandler.VIPIUM_ORE.get())
                                return;
                        }
                    }
                }

                this.mutable.setX(0);
                this.mutable.setY(0);
                this.mutable.setZ(0);
            }

            @OnlyIn(Dist.CLIENT)
            private double getAngle(@Nonnull Entity entity)
            {
                return Math.atan2(this.mutable.getZ() - entity.getPosZ(), this.mutable.getX() - entity.getPosX());
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
