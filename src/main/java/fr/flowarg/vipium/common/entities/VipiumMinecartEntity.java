package fr.flowarg.vipium.common.entities;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class VipiumMinecartEntity extends ContainerMinecartEntity
{
    public VipiumMinecartEntity(EntityType<VipiumMinecartEntity> type, World world)
    {
        super(type, world);
    }

    public VipiumMinecartEntity(double x, double y, double z, World world)
    {
        super(RegistryHandler.VIPIUM_MINECART_ENTITY.get(), x, y, z, world);
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        this.remove();
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
        {
            ItemStack itemStack = new ItemStack(Items.MINECART);
            if (this.hasCustomName())
                itemStack.setDisplayName(this.getCustomName());

            this.entityDropItem(itemStack);
        }
        InventoryHelper.dropInventoryItems(this.world, this, this);
    }

    @Override
    public Type getMinecartType()
    {
        return Type.CHEST;
    }

    @Override
    public ItemStack getCartItem()
    {
        return new ItemStack(RegistryHandler.VIPIUM_RAIL_ITEM.get());
    }

    @Override
    protected Container createContainer(int id, PlayerInventory playerInventoryIn)
    {
        return new ChestContainer(ContainerType.GENERIC_9X1, id, playerInventoryIn, this, 1);
    }

    @Override
    public int getSizeInventory()
    {
        return 9;
    }

    @Override
    public int getDefaultDisplayTileOffset()
    {
        return 8;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 6;
    }
}