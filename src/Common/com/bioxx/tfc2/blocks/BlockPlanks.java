package com.bioxx.tfc2.blocks;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bioxx.tfc2.Core;
import com.bioxx.tfc2.TFCBlocks;
import com.bioxx.tfc2.api.interfaces.ISupportBlock;
import com.bioxx.tfc2.api.interfaces.IWeightedBlock;
import com.bioxx.tfc2.api.types.WoodType;
import com.bioxx.tfc2.blocks.terrain.BlockCollapsible;

public class BlockPlanks extends BlockCollapsible implements ISupportBlock, IWeightedBlock
{
	public static PropertyEnum META_PROPERTY = PropertyEnum.create("wood", WoodType.class, Arrays.copyOfRange(WoodType.values(), 0, 16));

	public BlockPlanks()
	{
		super(Material.ground, META_PROPERTY);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public BlockPlanks(Material m, PropertyHelper p)
	{
		super(m, p);
	}

	/*******************************************************************************
	 * 1. Content 
	 *******************************************************************************/
	@Override
	public IBlockState getFallBlockType(IBlockState myState)
	{
		return TFCBlocks.Rubble.getDefaultState();
	}

	@Override
	public int getNaturalSupportRange(IBlockAccess world, BlockPos pos,IBlockState myState)
	{
		return 5;
	}

	@Override
	public boolean canBeSupportedBy(IBlockState myState, IBlockState otherState)
	{
		if(otherState.getBlock() == this || Core.isSoil(otherState) || Core.isStone(otherState) || otherState.getBlock() instanceof ISupportBlock)
			return true;
		return false;
	}

	@Override
	public int getMaxSupportWeight(IBlockAccess world, BlockPos pos, IBlockState myState) {
		int maxWeight = 25;
		Block b = world.getBlockState(pos.east()).getBlock();
		if(b instanceof ISupportBlock && ((ISupportBlock)b).isSpan(world, pos.east()))
			return maxWeight*2;
		b = world.getBlockState(pos.west()).getBlock();
		if(b instanceof ISupportBlock && ((ISupportBlock)b).isSpan(world, pos.west()))
			return maxWeight*2;
		b = world.getBlockState(pos.north()).getBlock();
		if(b instanceof ISupportBlock && ((ISupportBlock)b).isSpan(world, pos.north()))
			return maxWeight*2;
		b = world.getBlockState(pos.south()).getBlock();
		if(b instanceof ISupportBlock && ((ISupportBlock)b).isSpan(world, pos.south()))
			return maxWeight*2;
		return maxWeight;
	}

	@Override
	public boolean isSpan(IBlockAccess world, BlockPos pos) 
	{
		//If this block has an air block or partial block beneath it should be considered to be holding all of the weight above it.
		return world.getBlockState(pos.down()).getBlock().isSideSolid(world, pos.down(), EnumFacing.UP);
	}

	@Override
	public int getWeight(IBlockState myState) 
	{
		return 1;
	}

	@Override
	public void createFallingEntity(World world, BlockPos pos, IBlockState state)
	{
		world.setBlockToAir(pos);
		EntityItem ei = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.stick, 1+world.rand.nextInt(3)));
		world.spawnEntityInWorld(ei);
	}

	/*******************************************************************************
	 * 2. Rendering 
	 *******************************************************************************/

	/*******************************************************************************
	 * 3. Blockstate 
	 *******************************************************************************/

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[]{META_PROPERTY});
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(META_PROPERTY, WoodType.getTypeFromMeta(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((WoodType)state.getValue(META_PROPERTY)).getMeta();
	}
}
