package starryskyline.teastory.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import starryskyline.teastory.TeaStory;
import starryskyline.teastory.achievement.AchievementLoader;
import starryskyline.teastory.block.Barrel.EnumType;
import starryskyline.teastory.creativetab.CreativeTabsLoader;
import starryskyline.teastory.inventory.GuiElementLoader;
import starryskyline.teastory.item.ItemLoader;
import starryskyline.teastory.tileentity.TileEntityTeaStove;

public class TeaStove extends BlockContainer
{
	private final boolean isBurning;
	private static boolean keepInventory;
	
	public TeaStove(float lightLevel, String name, boolean isBurning)
	{
		super(Material.rock);
		this.setHardness(3.5F);
        this.setStepSound(soundTypeStone);
        this.setUnlocalizedName(name);
        this.setLightLevel(lightLevel);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.isBurning = isBurning;
	}
	
	@Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityTeaStove();
    }
	
	@Override
    public int getRenderType()
    {
        return 3;
    }
	
	@Override
	public ArrayList getDrops(IBlockAccess world, BlockPos pos, IBlockState blockstate, int fortune)
	{
    	ArrayList drops = new ArrayList();
	    drops.add(new ItemStack(BlockLoader.tea_stove, 1));
		return drops;
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
        {
            int id = GuiElementLoader.GUI_TEASTOVE;
            playerIn.openGui(TeaStory.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
		return true;
	}
	
	@SideOnly(Side.CLIENT)
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this.isBurning)
        {
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            switch (enumfacing)
            {
                case WEST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case EAST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case NORTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case SOUTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
            }
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1 + 1.1D, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1 + 0.8D, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }
	
	@Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING);
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.getHorizontal(meta & 3);
        Boolean burning = Boolean.valueOf((meta & 4) != 0);
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int facing = state.getValue(FACING).getHorizontalIndex();
        return facing;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        list.add(new ItemStack(BlockLoader.tea_stove, 1, 0));
    }
    
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(BlockLoader.tea_stove);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
            int meta, EntityLivingBase placer)
    {
    	((EntityPlayer) placer).triggerAchievement(AchievementLoader.teaStove);
        IBlockState origin = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        return origin.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	if (!keepInventory)
        {
    	    TileEntityTeaStove te = (TileEntityTeaStove) worldIn.getTileEntity(pos);
            IItemHandler inventory0 = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
            IItemHandler inventory1 = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            IItemHandler inventory2 = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            IItemHandler inventory3 = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

            for (int i = inventory0.getSlots() - 1; i >= 0; --i)
            {
                if (inventory0.getStackInSlot(i) != null)
                {
                    Block.spawnAsEntity(worldIn, pos, inventory0.getStackInSlot(i));
                    ((IItemHandlerModifiable) inventory0).setStackInSlot(i, null);
                }
            }

            for (int i = inventory1.getSlots() - 1; i >= 0; --i)
            {
                if (inventory1.getStackInSlot(i) != null)
                {
                    Block.spawnAsEntity(worldIn, pos, inventory1.getStackInSlot(i));
                    ((IItemHandlerModifiable) inventory1).setStackInSlot(i, null);
                }
            }
        
            for (int i = inventory2.getSlots() - 1; i >= 0; --i)
            {
                if (inventory2.getStackInSlot(i) != null)
                {
                    Block.spawnAsEntity(worldIn, pos, inventory2.getStackInSlot(i));
                    ((IItemHandlerModifiable) inventory2).setStackInSlot(i, null);
                }
            }
        
            for (int i = inventory3.getSlots() - 1; i >= 0; --i)
            {
                if (inventory3.getStackInSlot(i) != null)
                {
                    Block.spawnAsEntity(worldIn, pos, inventory3.getStackInSlot(i));
                    ((IItemHandlerModifiable) inventory3).setStackInSlot(i, null);
                }
            }
        }

        super.breakBlock(worldIn, pos, state);
    }
    
    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (active)
        {
            worldIn.setBlockState(pos, BlockLoader.lit_tea_stove.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        else
        {
            worldIn.setBlockState(pos, BlockLoader.tea_stove.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        keepInventory = false;

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
}
