package com.bioxx.tfc2.Items.ItemBlocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.bioxx.tfc2.Core;
import com.bioxx.tfc2.api.Global;

public class ItemWood extends ItemTerraBlock
{
	public ItemWood(Block b)
	{
		super(b);
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		super.addInformation(is, player, arraylist, flag);

		if (is.getItemDamage() < Global.WOOD_STANDARD.length)
			arraylist.add(EnumChatFormatting.DARK_GRAY + Core.translate("global." + Global.WOOD_STANDARD[is.getItemDamage()]));
		else
			arraylist.add(EnumChatFormatting.DARK_RED + Core.translate("global.unknown"));
	}
}