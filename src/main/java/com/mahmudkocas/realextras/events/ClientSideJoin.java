package com.mahmudkocas.realextras.events;


import com.mahmudkocas.realextras.Main;
import com.mahmudkocas.realextras.message.MyMessage;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ClientSideJoin {
	public static boolean IS_MESSAGE_RECEIVED = false;
	public static int howManyTicksPassed = 0;
	public static boolean PLAYER_IN = false;
	public static boolean IS_PLAYER_NOT_GONNA_JOIN_SERVER = true;
	public static boolean PLAYER_IS_ASSIGNED = false;
	public static EntityPlayer player;
	@SubscribeEvent
	public static void mouseEvent(MouseEvent event) {
		
	}
	
	@SubscribeEvent
	public static void playerTick (PlayerTickEvent event) {
		if(PLAYER_IS_ASSIGNED) {
			PLAYER_IS_ASSIGNED = false;
			player = event.player;
		}
	}
	
	
	@SubscribeEvent
	public static void clientSideJoin(ClientConnectedToServerEvent event) {
		howManyTicksPassed = 0;
		Main.IS_PLAYER_IN_SERVER = true;
		PLAYER_IN = true;
		PLAYER_IS_ASSIGNED = true;
	}
	
	@SubscribeEvent
	public static void clientSideDisconnect(ClientDisconnectionFromServerEvent event) {
		howManyTicksPassed = 0;
		Main.IS_PLAYER_IN_SERVER = false;
		PLAYER_IN = false;
	}
	
	@SubscribeEvent
	public static void clientTic(ClientTickEvent event) {
		if(PLAYER_IN && IS_MESSAGE_RECEIVED)
			howManyTicksPassed++;
		if(howManyTicksPassed  - 200 > 0) {
			if(Main.SIDE == Side.CLIENT && Main.IS_PLAYER_IN_SERVER) {
				Main.IS_PLAYER_IN_SERVER = false;
				Main.network.sendToServer(new MyMessage(Main.FILE_LIST_STRING));
			}
		}
	}
	
	@SubscribeEvent
	public static void closeInv(GuiOpenEvent event) {
		if(event.getGui() instanceof GuiMultiplayer) {
			IS_PLAYER_NOT_GONNA_JOIN_SERVER = false;
		}
		if(event.getGui() instanceof GuiMainMenu) {
			IS_PLAYER_NOT_GONNA_JOIN_SERVER = true;
		}
		if(event.getGui() instanceof GuiScreenResourcePacks)
			event.setCanceled(true);
		if(PLAYER_IN && howManyTicksPassed <= Main.CLOSE_INV_FOR_HOW_LONG_IN_TICKS && event.getGui() instanceof GuiInventory && Main.SIDE == Side.CLIENT) {
			event.setCanceled(true);
			event.isCanceled();
			player.sendMessage(new TextComponentString("Inventory is locked for " + ((Main.CLOSE_INV_FOR_HOW_LONG_IN_TICKS - howManyTicksPassed) / 20) + " secs."));
		} 
	}
}
