package fr.flowarg.vipium.common.tileentities;

import fr.flowarg.vipium.common.blocks.VipiumPurifierBlock;
import fr.flowarg.vipium.common.containers.VipiumPurifierContainer;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class VipiumPurifierTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity
{
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    private PlayerEntity player;
    private static final int MAX_TIME_TICK = 40;
    private int timer = MAX_TIME_TICK;
    private final Random rand = new Random();

    public VipiumPurifierTileEntity()
    {
        super(RegistryHandler.VIPIUM_PURIFIER_TILE_ENTITY.get());
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull Direction side)
    {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nullable Direction direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction)
    {
        return true;
    }

    @Nonnull
    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.vipium_purifier");
    }

    @Nonnull
    @Override
    protected Container createMenu(int id, @Nonnull PlayerInventory player)
    {
        return new VipiumPurifierContainer(id, player, this);
    }

    @Override
    public int getSizeInventory()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        for(ItemStack itemstack : this.items)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.items.get(index);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
    {
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player)
    {
        if (this.world != null && this.world.getTileEntity(this.pos) != this) return false;
         else return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack)
    {
        return stack.getItem() == RegistryHandler.VIPIUM_INGOT.get();
    }

    @Override
    public void clear()
    {
        this.items.clear();
    }

    @Override
    public void tick()
    {
        if(this.world != null && !this.world.isRemote)
        {
            if(this.canPurify())
            {
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(VipiumPurifierBlock.LIT, true));
                this.purify();
            }
            else this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(VipiumPurifierBlock.LIT, false));
        }
        this.markDirty();
    }

    private boolean canPurify()
    {
        if(!this.items.get(0).isEmpty() && this.items.get(0).getItem() == RegistryHandler.VIPIUM_INGOT.get())
        {
            if(this.world != null)
            {
                final BlockPos playerPos = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                final PlayerEntity player = this.world.getClosestPlayer(playerPos.getX(), playerPos.getY(), playerPos.getZ());
                this.player = player;
                return player != null && player.getPosition().equals(playerPos);
            }
            else return false;
        }
        else return false;
    }

    private void purify()
    {
        if(this.timer == 0)
        {
            if(this.rand.nextInt(100) != 0)
            {
                this.player.setHealth(this.player.getHealth() - 0.5F);
                if(this.items.get(0).getCount() >= 2)
                {
                    if(this.items.get(0).getCount() >= 4)
                        this.items.get(0).shrink(2 + this.rand.nextInt(3));
                    else this.items.get(0).shrink(2);
                    if(this.rand.nextInt(180) == 0)
                        this.player.inventory.addItemStackToInventory(new ItemStack(RegistryHandler.VIPIUM_PURE_INGOT.get(), 1));
                    else this.player.inventory.addItemStackToInventory(new ItemStack(RegistryHandler.VIPIUM_PURE_FRAGMENT.get(), 3));
                }
                else this.player.sendMessage(new TranslationTextComponent("purifier.err.notenoughitems"));
            }
            else
            {
                if(this.world != null)
                {
                    this.world.createExplosion(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 20f, Explosion.Mode.BREAK);
                    if(this.rand.nextInt(3) == 2)
                        this.player.addItemStackToInventory(new ItemStack(RegistryHandler.VIPIUM_PURIFIER_ITEM.get()));
                }
            }
            this.timer = MAX_TIME_TICK;
        }
        else if(this.timer > 0)
            this.timer--;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void read(@Nonnull CompoundNBT compound)
    {
        super.read(compound);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
        this.timer = compound.getInt("timer");
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound)
    {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
        compound.putInt("timer", this.timer);
        return compound;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if(!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (side == Direction.UP)
                return handlers[0].cast();
            else if (side == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove()
    {
        super.remove();
        for (LazyOptional<? extends IItemHandler> handler : this.handlers) handler.invalidate();
    }
}
