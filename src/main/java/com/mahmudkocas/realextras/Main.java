package com.mahmudkocas.realextras;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

import com.mahmudkocas.realextras.events.ClientSideJoin;
import com.mahmudkocas.realextras.events.RegisterItem;
import com.mahmudkocas.realextras.events.ServerSideJoin;
import com.mahmudkocas.realextras.message.MyMessage;
import com.mahmudkocas.realextras.message.MyMessageHandler;
import com.mahmudkocas.realextras.proxy.CommonProxy;
import com.mahmudkocas.realextras.util.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;

@Mod(modid = Reference.MOD_ID, name= Reference.NAME, version = Reference.VERSION)
public class Main {
	
	@Instance
	public static Main instance;
	
	public static boolean SHOULD_KICK = true;
	public static int CLOSE_INV_FOR_HOW_LONG_IN_TICKS = 200;
	public static String KICK_MESSAGE = "Seems like you installed a mod. In order to enter server, remove the mod you installed.";
	public static String ABSOLUTE_MOD_LIST = "@AdLods-1.12.2-1.0.8.0-build.0504.jar@BetterBuildersWands-1.12-0.11.1.245+69d0d70.jar@BetterFps-1.4.8.jar@bonsaitrees-1.1.4-b170.jar@Chisel-MC1.12.2-1.0.2.45.jar@CraftPresence-1.12.2-1.6.5.jar@CTM-MC1.12.2-1.0.2.31.jar@CustomMainMenu-MC1.12.2-2.0.9.jar@ForgeEndertech-1.12.2-4.5.3.0-build.0528.jar@FTBLib-5.4.7.2.jar@FTBQuests-1.9.0.10.jar@FTBUtilities-5.4.1.130.jar@hexlands-1.9.0.jar@InventoryTweaks-1.63.jar@ItemFilters-1.0.4.2.jar@jei_1.12.2-4.15.0.296.jar@MouseTweaks-2.10-mc1.12.2.jar@OreExcavation-1.4.150.jar@PixelExtras-1.12.2-2.5.7-universal.jar@Pixelmon-1.12.2-8.0.2-universal.jar@PixelOnExtras-0.1.jar@ProjectE-1.12.2-PE1.4.1.jar@ResourceLoader-MC1.12.1-1.5.3.jar@SilentLib-1.12.2-3.0.14+168.jar@SilentsGems-1.12.2-2.8.22+327.jar@WonderTrade-1.12.2-4.6.2-universal.jar@";
	public static ArrayList<String> ALLOWED_PLAYERS = new ArrayList<>();
	
	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("mkrex");
	public static Side SIDE;
	public static boolean IS_PLAYER_IN_SERVER = false;
	public static ArrayList<File> FILES; 
	public static String FILE_LIST_STRING;
	public static File logFile = null;
    public static int discriminator = 0;
    public static int discriminator_received = 0;
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		SIDE = event.getSide();
		
		//ALLOWED_PLAYERS.add("TheDoctorOne");
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		network.registerMessage(MyMessageHandler.class, MyMessage.class, discriminator, Side.CLIENT);
		network.registerMessage(MyMessageHandler.class, MyMessage.class, discriminator, Side.SERVER);
		
		MinecraftForge.EVENT_BUS.register(ServerSideJoin.class);
		if(Side.CLIENT == SIDE)
			MinecraftForge.EVENT_BUS.register(ClientSideJoin.class);
		
		/*CONFIG WORKS*/
		try {
			if(Side.SERVER == SIDE) {
				Path currentPath = Paths.get("");
				File configFolder = new File(currentPath.toAbsolutePath().toString() + "/realExtras");
				File configFile = new File(currentPath.toAbsolutePath().toString() + "/realExtras/extra.conf");
				System.out.println("path : " + configFile.getAbsolutePath());
				if(!configFile.exists()) {
					if(!configFolder.exists())
						configFolder.mkdir();
					
					configFile.createNewFile();
					FileWriter fileWriter = new FileWriter(configFile);
					BufferedWriter bWriter = new BufferedWriter(fileWriter);
					bWriter.write("#Hashtag(Sharp) is for comments. Only use them at the start of the line.\n#Do we wanna kick?\n"
							+ "SHOULD_KICK=true\n"
							+ "#Locking player inventory when player joins the server for glitch purposes. 20 Ticks = 1 Sec\n"
							+ "CLOSE_INV_FOR_HOW_LONG_IN_TICKS=200\n"
							+ "#If 'should kick' is 'true' then what will be the kick message should be? Don't put equal sign in kick message.\n"
							+ "KICK_MESSAGE=Seems like you installed a mod. In order to enter server, remove the mod you installed.\n"
							+ "#Exculuded players from kicking event. Seperate names with comma.\n"
							+ "ALLOWED_PLAYERS=MahmudKocas,chocopufs\n"
							+ "#IF YOU DON'T KNOW WHAT YOU ARE DOING DON'T CHANGE WHAT YOU SEE BELOW!\n"
							+ "ABSOLUTE_MOD_LIST=" + ABSOLUTE_MOD_LIST);
					bWriter.flush();
					bWriter.close();
					fileWriter.close();
				} else {
					FileReader fReader = new FileReader(configFile);
					BufferedReader bReader = new BufferedReader(fReader);
					String temp = "";
					while((temp = bReader.readLine()) != null) {
						if(temp.trim().startsWith("#"))
							continue;
						if(temp.contains("SHOULD_KICK")) {
							if(temp.split("=")[1].toLowerCase().contains("true")) {
								SHOULD_KICK = true;
							} else if(temp.split("=")[1].toLowerCase().contains("false")) {
								SHOULD_KICK = false;
							}
						} else if(temp.contains("CLOSE_INV_FOR_HOW_LONG_IN_TICKS")) {
							CLOSE_INV_FOR_HOW_LONG_IN_TICKS = Integer.parseInt(temp.split("=")[1].split("\n")[0]);
						} else if(temp.toLowerCase().contains("KICK_MESSAGE")) {
							KICK_MESSAGE = temp.split("=")[1];
							if(KICK_MESSAGE == null) {
								KICK_MESSAGE = "Seems like you installed a mod. In order to enter server, remove the mod you installed.";
							}
						} else if (temp.toLowerCase().contains("ALLOWED_PLAYERS")) {
							for(String string : temp.split("=")[1].split(",")) {
								ALLOWED_PLAYERS.add(string);
							}
						}else if(temp.toLowerCase().contains("ABSOLUTE_MOD_LIST")) {
							try {
								ABSOLUTE_MOD_LIST = temp.split("=")[1];
							} catch (NullPointerException e) {}
							Logger.getLogger("PixelOnExtras").info(ABSOLUTE_MOD_LIST);
							if(ABSOLUTE_MOD_LIST == null) {
								System.out.println("MOD LIST NOT FOUND PLAYERS WILL NOT BE KICKED!!!!");
								SHOULD_KICK = false;
							}
						}
					}
					bReader.close();
					fReader.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {
		Path currentPath = Paths.get("");
		MinecraftForge.EVENT_BUS.register(RegisterItem.class);
		
		
		if(Side.CLIENT == SIDE) { //DELETE RESOURCE PACKS
			File resourcePackFile = new File(currentPath.toAbsolutePath().toString() + "/resourcepacks");
			File[] resourceList = resourcePackFile.listFiles();
			for (File f : resourceList)
				if(!f.getName().toLowerCase().contains("weCan".toLowerCase()))
					f.delete();
		}
		
		File file = new File(currentPath.toAbsolutePath().toString() + "/mods"); //GETTING MOD LIST
		ArrayList<File> files = new ArrayList<>(Arrays.asList(file.listFiles()));
		FILES = files;
		ArrayList<String> fileNames = new ArrayList<>();
		for(File f : files)
			fileNames.add(f.getName() + "&" + new SimpleDateFormat("MM:dd:yyyy-HH:mm:ss").format(f.lastModified()));
			//fileNames.add(f.getName());
		Collections.sort(fileNames);
		String completeList = "";
		completeList += "&" + fileNames.size() + " Mods @";
		for(String string : fileNames) {
			completeList += string + "@";
		}
		FILE_LIST_STRING = completeList;
		System.out.println("MOD LIST: " + completeList);
		Logger.getLogger("MOD LIST: ").warning(completeList);
		
		
		
	}
	
	
	
	
	
}
