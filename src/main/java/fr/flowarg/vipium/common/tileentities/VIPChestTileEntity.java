package fr.flowarg.vipium.common.tileentities;

import fr.flowarg.vipium.common.blocks.VIPChestBlock;
import fr.flowarg.vipium.common.containers.VIPChestContainer;
import fr.flowarg.vipium.common.core.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public class VIPChestTileEntity extends ChestTileEntity implements IChestLid
{
    private NonNullList<ItemStack> chestContents = NonNullList.withSize(80, ItemStack.EMPTY);
    private float lidAngle;
    private float prevLidAngle;
    private int numPlayersUsing;
    private int ticksSinceSync;
    private LazyOptional<IItemHandlerModifiable> chestHandler;

    public VIPChestTileEntity()
    {
        super(RegistryHandler.VIP_CHEST_TILE_ENTITY.get());
    }

    @Override
    public int getSizeInventory()
    {
        return 80;
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.vipium_chest");
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.chestContents);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.chestContents);
        return compound;
    }

    @Override
    public void tick()
    {
        final int i = this.pos.getX();
        final int j = this.pos.getY();
        final int k = this.pos.getZ();
        ++this.ticksSinceSync;
        this.numPlayersUsing = calculatePlayersUsingSync(this.world, this.ticksSinceSync, i, j, k, this.numPlayersUsing);
        this.prevLidAngle = this.lidAngle;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
            this.playSound(SoundEvents.BLOCK_CHEST_OPEN);

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;
            if (this.numPlayersUsing > 0) this.lidAngle += 0.1F;
            else this.lidAngle -= 0.1F;

            if (this.lidAngle > 1.0F) this.lidAngle = 1.0F;

            if (this.lidAngle < 0.5F && f1 >= 0.5F) this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);

            if (this.lidAngle < 0.0F) this.lidAngle = 0.0F;
        }

    }

    public static int calculatePlayersUsingSync(World p_213977_0_, int p_213977_2_, int p_213977_3_, int p_213977_4_, int p_213977_5_, int p_213977_6_)
    {
        if (!p_213977_0_.isRemote && p_213977_6_ != 0 && (p_213977_2_ + p_213977_3_ + p_213977_4_ + p_213977_5_) % 200 == 0)
            p_213977_6_ = calculatePlayersUsing(p_213977_0_, p_213977_3_, p_213977_4_, p_213977_5_);

        return p_213977_6_;
    }

    public static int calculatePlayersUsing(World p_213976_0_, int p_213976_2_, int p_213976_3_, int p_213976_4_) {
        int i = 0;

        for(PlayerEntity playerentity : p_213976_0_.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((float)p_213976_2_ - 5.0F, (float)p_213976_3_ - 5.0F, (float)p_213976_4_ - 5.0F, (float)(p_213976_2_ + 1) + 5.0F, (float)(p_213976_3_ + 1) + 5.0F, (float)(p_213976_4_ + 1) + 5.0F)))
        {
            if (playerentity.openContainer instanceof VIPChestContainer)
            {
                ++i;
            }
        }

        return i;
    }

    private void playSound(SoundEvent soundIn)
    {
        final ChestType chesttype = this.getBlockState().get(VIPChestBlock.TYPE);
        if (chesttype != ChestType.LEFT) {
            double d0 = (double)this.pos.getX() + 0.5D;
            double d1 = (double)this.pos.getY() + 0.5D;
            double d2 = (double)this.pos.getZ() + 0.5D;
            if (chesttype == ChestType.RIGHT)
            {
                final Direction direction = VIPChestBlock.getDirectionToAttached(this.getBlockState());
                d0 += (double)direction.getXOffset() * 0.5D;
                d2 += (double)direction.getZOffset() * 0.5D;
            }

            this.world.playSound(null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else return super.receiveClientEvent(id, type);
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0) this.numPlayersUsing = 0;

            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        if (!player.isSpectator())
        {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose()
    {
        final Block block = this.getBlockState().getBlock();
        if (block instanceof VIPChestBlock)
        {
            this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, block);
        }
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        return this.chestContents;
    }

    public void setItems(NonNullList<ItemStack> itemsIn)
    {
        this.chestContents = itemsIn;
    }

    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks)
    {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player)
    {
        return new VIPChestContainer(id, player);
    }

    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        if (this.chestHandler != null)
        {
            this.chestHandler.invalidate();
            this.chestHandler = null;
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if (!this.removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (this.chestHandler == null)
                this.chestHandler = LazyOptional.of(this::createHandler);
            return this.chestHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler()
    {
        final BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof VIPChestBlock))
            return new InvWrapper(this);
        final IInventory inv = VIPChestBlock.func_226916_a_((VIPChestBlock)state.getBlock(), state, getWorld(), getPos(), true);
        return new InvWrapper(inv == null ? this : inv);
    }

    @Override
    public void remove()
    {
        super.remove();
        if (this.chestHandler != null)
            this.chestHandler.invalidate();
    }
}
