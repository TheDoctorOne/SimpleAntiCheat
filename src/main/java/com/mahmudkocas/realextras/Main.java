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
	public static String ABSOLUTE_MOD_LIST = "@1.12.2@Aroma1997Core-1.12.2-2.0.0.2.jar@Aroma1997s-Dimensional-World-1.12.2-2.0.0.2.jar@AutoRegLib-1.3-31.jar@BetterBuildersWands-1.12.2-0.13.2.271+5997513.jar@BiblioCraft[v2.4.5][MC1.12.2].jar@CTM-MC1.12.2-0.3.3.22.jar@ChineseWorkshop-1.12.2_1.2.6.jar@Chisel-MC1.12.2-0.2.1.35.jar@ClientTweaks_1.12.2-3.1.11.jar@CodeChickenLib-1.12.2-3.2.2.353-universal.jar@Controlling-3.0.7.jar@CosmeticArmorReworked-1.12.2-v4a.jar@CraftPresence-1.12.2-1.5.5.jar@CraftTweaker2-1.12-4.1.19.jar@CraftingTweaks_1.12.2-8.1.9.jar@CustomMainMenu-MC1.12.2-2.0.9.jar@FTBLib-5.4.3.127.jar@FTBUtilities-5.4.0.100.jar@FTBUtilitiesBackups-1.0.0.4.jar@FastLeafDecay-v14.jar@FenceOverhaul-1.3.4.jar@Forgelin-1.8.3.jar@InitialInventory-3.0.0.jar@InventoryTweaks-1.63.jar@JEROreIntegration-1.12.2-1.1.0.jar@JustEnoughResources-1.12.2-0.9.2.60.jar@MCMultiPart-2.5.3.jar@MTLib-3.0.6.jar@MouseTweaks-2.10-mc1.12.2.jar@NaturesCompass-1.12.2-1.5.1.jar@OfflineSkins-1.12.2-v5g.jar@OpenBlocks-1.12.2-1.8.1.jar@OpenModsLib-1.12.2-0.12.2.jar@PixelExtras-1.12.2-2.5.0-universal.jar@PixelOnExtras-0.1.jar@Pixelmon-1.12.2-7.0.6-universal.jar@Quark-r1.5-167.jar@ResourceLoader-MC1.12.1-1.5.3.jar@SilentLib-1.12.2-3.0.13+167.jar@SilentsGems-1.12.2-2.8.18+322.jar@WonderTrade-1.12.2-4.6.2-universal.jar@apritree-1.12.2-2.0.5.jar@blockcraftery-1.12.2-1.3.1.jar@bonsaitrees-1.1.3-b156.jar@chiselsandbits-14.33.jar@flatcoloredblocks-mc1.12-6.8.jar@furnus-1.12.2-2.1.8.jar@hexlands-1.9.0.jar@jei_1.12.2-4.15.0.268.jar@journeymap-1.12.2-5.5.4.jar@limelib-1.12.2-1.7.12.jar@lumberjack-1.4.1.jar@malisisblocks-1.12.2-6.1.0.jar@malisiscore-1.12.2-6.5.1.jar@malisisdoors-1.12.2-7.3.0.jar@malisisswitches-1.12.2-5.1.0.jar@mysticallib-1.12.2-1.3.1.jar@randompatches-1.12.2-1.17.2.0.jar@refinedstorage-1.6.14.jar@stackable-1.12.2-1.3.3.jar@theoneprobe-1.12-1.4.28.jar@worleycaves-1.4.1.jar@";
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
		
		ALLOWED_PLAYERS.add("MahmudKocas");
		ALLOWED_PLAYERS.add("chocopufs");
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
							ABSOLUTE_MOD_LIST = temp.split("=")[1];
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
		Collections.sort(fileNames);
		String completeList = "";
		completeList += "&" + fileNames.size() + " Mods @";
		for(String string : fileNames) {
			completeList += string + "@";
		}
		FILE_LIST_STRING = completeList;		
		//System.out.println(completeList);
		
		
		
	}
	
	
	
	
	
}
