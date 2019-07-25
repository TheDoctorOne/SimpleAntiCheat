package com.mahmudkocas.realextras.message;

import java.nio.charset.Charset;


import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MyMessage implements IMessage {
	  public MyMessage(){}

	  public CharSequence toSend;
	  public MyMessage(CharSequence toSend) {
	    this.toSend = toSend;
	    
	  }

	  @Override public void toBytes(ByteBuf buf) {
			System.out.println("TO BYTESS!!!!!!!!!");
		    buf.writeCharSequence(toSend, Charset.defaultCharset());
		    //Main.discriminator++;
			/*  if(Main.SIDE == Side.CLIENT)
				    Main.network.registerMessage(MyMessageHandler.class, MyMessage.class, Main.discriminator, Side.CLIENT);
			  else
				    Main.network.registerMessage(MyMessageHandler.class, MyMessage.class, Main.discriminator, Side.SERVER);*/
	  }

	  @Override public void fromBytes(ByteBuf buf) {
		  System.out.println("FROM BYTESS!!!!!!!!!");
		  toSend = buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset());
		  /*Main.discriminator_received++;
		  if(Main.SIDE == Side.CLIENT)
			    Main.network.registerMessage(MyMessageHandler.class, MyMessage.class, Main.discriminator_received, Side.CLIENT);
		  else
			    Main.network.registerMessage(MyMessageHandler.class, MyMessage.class, Main.discriminator_received, Side.SERVER);*/
	  }
	  
	}
