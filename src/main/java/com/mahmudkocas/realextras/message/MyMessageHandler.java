package com.mahmudkocas.realextras.message;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import com.mahmudkocas.realextras.Main;
import com.mahmudkocas.realextras.events.ClientSideJoin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

//The params of the IMessageHandler are <REQ, REPLY>
//This means that the first param is the packet you are receiving, and the second is the packet you are returning.
//The returned packet can be used as a "response" from a sent packet.
public class MyMessageHandler implements IMessageHandler<MyMessage, IMessage> {
// Do note that the default constructor is required, but implicitly defined in this case
	public MyMessageHandler() {
		// TODO Auto-generated constructor stub
	}
	@Override 
	public IMessage onMessage(MyMessage message, MessageContext ctx) {
		if(Minecraft.getMinecraft().isSingleplayer())
			return null;
		if(Main.SIDE == Side.SERVER) {
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(()-> {
					if(Main.SHOULD_KICK)
						if(!Main.ALLOWED_PLAYERS.contains(ctx.getServerHandler().player.getName()))
							checkMods(message.toSend.toString(), ctx);
					writeLog(ctx.getServerHandler().player.getName() + "^" + ctx.getServerHandler().player.getPlayerIP() +  "\n\t" + message.toSend + "\n", ctx);
					//System.out.print(ctx.getServerHandler().player.getName() + "=" + message.toSend);
						});
				
				
			}
		
		if(Main.SIDE == Side.CLIENT) {
			ClientSideJoin.PLAYER_IN = true;
			Main.CLOSE_INV_FOR_HOW_LONG_IN_TICKS = Integer.parseInt(message.toSend.toString().split("=")[1]);
			ClientSideJoin.IS_MESSAGE_RECEIVED = true;
		}
	 return null;
	}

	public void writeLog(String toWrite, MessageContext ctx) {
		try {
			if(Main.logFile == null) {
				ctx.getServerHandler().player.getServer();
				File folder = new File(Paths.get("").toAbsolutePath().toString() + "/realextras/logs");
				if(!folder.exists()) {
					folder.mkdir();
				}
				Main.logFile = new File(Paths.get("").toAbsolutePath().toString() + "/realextras/logs/log-" + MinecraftServer.getCurrentTimeMillis() + ".log");
				Main.logFile.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(Main.logFile, true);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.append(toWrite);
			writer.flush();
			writer.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void checkMods(String list, MessageContext ctx) {
		/*
		 * List objects will be seperated with "@"
		 * and inside they will be seperated with timestamp with "&"
		 * so at "0.Index" String will be out mod.
		 * */
		String putRealListHere = Main.ABSOLUTE_MOD_LIST;
		String[] seperatedList = list.split("@");
		String tempString = "";
		for(String s : seperatedList) {
			try {
				tempString += s.split("&")[0] + "@";}
			catch(java.lang.ArrayIndexOutOfBoundsException exception) {
				continue;
				}
		}
		System.out.println("LIST : " + tempString + "\n");
		if(!tempString.trim().equals(putRealListHere.trim()))
			if(Main.SHOULD_KICK)
				ctx.getServerHandler().player.connection.disconnect(new TextComponentString(Main.KICK_MESSAGE));
	}
	
	
}