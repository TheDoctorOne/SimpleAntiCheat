package com.mahmudkocas.realextras.events;


import com.mahmudkocas.realextras.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@EventBusSubscriber
public class RegisterItem {
	public static final String RESOURCE_INVENTORY  = "inventory";
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Block> event) {
		try {
		/*ItemBlock itemTargetBlock;
		
		Block targetBlock = new TargetBlock();
		
		ForgeRegistries.BLOCKS.register(targetBlock);

        itemTargetBlock = new ItemBlock(targetBlock);
        itemTargetBlock.setRegistryName(targetBlock.getRegistryName());
        ForgeRegistries.ITEMS.register(itemTargetBlock);

        ModelResourceLocation chinaModelResourceLocation = new ModelResourceLocation(
                Reference.MOD_ID + ":" + TargetBlock.NAME, RESOURCE_INVENTORY);
        final int DEFAULT_ITEM_SUBTYPE = 0;
        ModelLoader.setCustomModelResourceLocation(itemTargetBlock, DEFAULT_ITEM_SUBTYPE, chinaModelResourceLocation);
		event.getRegistry().register(targetBlock);*/
	} catch (NoSuchFieldError ex) {
		
	}
		
	}
	
}
