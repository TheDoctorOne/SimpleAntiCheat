package com.mahmudkocas.realextras.events;

import com.mahmudkocas.realextras.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TargetBlock extends Block {
    public static final String NAME = "target";
    private static final String UNLOCALIZED_NAME = "target_block";
    private static final String REGISTRY_NAME = "target_block_registry";

    public TargetBlock() {
    	super(Material.ROCK);
        this.setRegistryName(Reference.MOD_ID, REGISTRY_NAME);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        this.setUnlocalizedName(UNLOCALIZED_NAME);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
      return BlockRenderLayer.SOLID;
    }
}