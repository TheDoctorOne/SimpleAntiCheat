package com.mahmudkocas.realextras.events;

import com.mahmudkocas.realextras.Main;
import com.mahmudkocas.realextras.message.MyMessage;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerSideJoin {
	@SubscribeEvent
	public static void onPlayerJoin(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof EntityPlayerMP && ClientSideJoin.IS_PLAYER_NOT_GONNA_JOIN_SERVER) {
			Main.network.sendTo(new MyMessage("CLOSE_INV_FOR_HOW_LONG_IN_TICKS=" + Main.CLOSE_INV_FOR_HOW_LONG_IN_TICKS), (EntityPlayerMP) event.getEntity());
		}
	}
}
