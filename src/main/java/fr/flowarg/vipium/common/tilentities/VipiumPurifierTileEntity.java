package fr.flowarg.vipium.common.tilentities;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import fr.flowarg.vipium.common.blocks.VipiumPurifierBlock;
import fr.flowarg.vipium.common.containers.VipiumPurifierContainer;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import fr.flowarg.vipium.common.utils.PurifierElementStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.DamageSource;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VipiumPurifierTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity
{
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    private final int maxTimeTick = 40;
    private int timer = this.maxTimeTick;
    private final List<Block> xSide = Arrays.asList(Blocks.QUARTZ_PILLAR, RegistryHandler.VIPIUM_BLOCK.get(), RegistryHandler.VIPIUM_BLOCK.get(), Blocks.QUARTZ_PILLAR, RegistryHandler.VIPIUM_BLOCK.get(), RegistryHandler.VIPIUM_BLOCK.get(), Blocks.QUARTZ_PILLAR);
    private final List<Block> zSide = Arrays.asList(RegistryHandler.VIPIUM_BLOCK.get(), RegistryHandler.VIPIUM_BLOCK.get(), Blocks.QUARTZ_PILLAR, RegistryHandler.VIPIUM_BLOCK.get(), RegistryHandler.VIPIUM_BLOCK.get());
    private final List<Integer> five = Arrays.asList(0, 1, 2, 3, 4);
    private PurifierElementStorage purifierStorage;

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
            else
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(VipiumPurifierBlock.LIT, false));
        }
    }

    private boolean canPurify()
    {
        if(!this.items.get(0).isEmpty())
        {
            return this.isStructureValid();
        }
        else return false;
    }

    @SuppressWarnings("all")
    public boolean isStructureValid()
    {
        if(this.world != null)
        {
            final int top = this.pos.getX() - 3;
            final int bottom = this.pos.getX() + 3;
            final int left = this.pos.getZ() - 3;
            final int right = this.pos.getZ() + 3;

            for (int x = 0; x < 7; x++)
            {
                if(this.xSide.get(x) != this.toBlock(new BlockPos(top + x, this.pos.getY() - 1, left)))
                    return false;
                else if(this.xSide.get(x) != this.toBlock(new BlockPos(top + x, this.pos.getY() - 1, right)))
                    return false;
            }

            for (int z = 0; z < 5; z++)
            {
                if(this.zSide.get(z) != this.toBlock(new BlockPos(top, this.pos.getY() - 1, left + 1 + z)))
                    return false;
                else if(this.zSide.get(z) != this.toBlock(new BlockPos(bottom, this.pos.getY() - 1, left + 1 + z)))
                    return false;
            }

            final int xBase = top + 1;
            final int zBase = left + 1;
            final Table<Integer, Integer, Block> table = ArrayTable.create(this.five, this.five);

            for (int x = 0; x < 5; x++)
            {
                for (int z = 0; z < 5; z++)
                {
                    final Block searchedBlock = this.toBlock(new BlockPos(xBase + x, this.pos.getY() - 1, zBase + z));
                    if(searchedBlock == Blocks.QUARTZ_BLOCK)
                    {
                        table.put(x, z, searchedBlock);
                    }
                    else return false;
                }
            }

            final BlockPos playerPos = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
            final PlayerEntity player = this.world.getClosestPlayer(playerPos.getX(), playerPos.getY(), playerPos.getZ());

            if(player == null || player.getPosition() != playerPos)
                return false;

            final List<Block> columns = new ArrayList<>(8);
            final List<Block> vipiumBlocks = new ArrayList<>(16);

            this.xSide.forEach(block -> {
                if(block.getRegistryName().getPath().equals(RegistryHandler.VIPIUM_BLOCK.get().getRegistryName().getPath()))
                    vipiumBlocks.add(block);
                else columns.add(block);
            });

            this.purifierStorage = new PurifierElementStorage(table.values(),
                                                              this.toBlock(this.pos),
                                                              player,
                                                              columns,
                                                              vipiumBlocks);
            ((ArrayTable)table).eraseAll();
            return true;
        }
        else return false;
    }

    @Nonnull
    private Block toBlock(BlockPos pos)
    {
        assert this.world != null;
        return this.toBlock(this.world.getBlockState(pos));
    }

    @Nonnull
    private Block toBlock(@Nonnull BlockState state)
    {
        return state.getBlock();
    }

    private void purify()
    {
        if(this.timer == 0)
        {
            final Random random = new Random();
            final int number = random.nextInt(30);
            if(number != 0)
            {
                final PlayerEntity player = this.purifierStorage.getPlayer();
                player.setHealth(player.getHealth() - 0.5F);
                this.items.get(0).shrink(1);
                player.inventory.addItemStackToInventory(new ItemStack(RegistryHandler.VIPIUM_PURE_FRAGMENT.get(), 3));
            }
            else this.world.createExplosion(this.purifierStorage.getPlayer(), DamageSource.causeExplosionDamage((Explosion)null), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 6f, true, Explosion.Mode.DESTROY);
            this.timer = this.maxTimeTick;
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
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound)
    {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
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
