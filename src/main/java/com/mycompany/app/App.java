package com.mycompany.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.List;

import com.microsoft.azure.iothub.DeviceClient;
import com.microsoft.azure.iothub.IotHubClientProtocol;
import com.microsoft.azure.iothub.IotHubEventCallback;
import com.microsoft.azure.iothub.IotHubStatusCode;
import com.microsoft.azure.iothub.Message;
/**
 * Hello world!
 *
 */
public class App 
{
	private static String connString = "HostName=iGrid.azure-devices.net;DeviceId=javadevice;SharedAccessKey=GpNHMH1HLCZU2zdv5teUCQ==";

	private static IotHubClientProtocol protocol = IotHubClientProtocol.HTTPS;
	private static boolean stopThread = false;
	
	public static void main( String[] args ) throws IOException, URISyntaxException {

		  MessageSender ms0 = new MessageSender();
		  Thread t0 = new Thread(ms0);
		  t0.start(); 

		  System.out.println("Press ENTER to exit.");
		  System.in.read();
		  ms0.stopThread = true;
		}
    
    private static class EventCallback implements IotHubEventCallback
    {
      public void execute(IotHubStatusCode status, Object context) {
        System.out.println("IoT Hub responded to message with status " + status.name());

        if (context != null) {
          synchronized (context) {
            context.notify();
          }
        }
      }
    }
    
    private static class MessageSender implements Runnable {
    	  public volatile boolean stopThread = false;
    	  private InputStream input;
    	  private List<FoggerEntity> foggerEntity;
    	  public void run()  {
    	    try {
    	    	
    			input = new FileInputStream("resources/entityAzure.json");
    			ReadEntity jsonUtility = new ReadEntity();
    			foggerEntity = jsonUtility.readJsonStream(input);

    	      DeviceClient client;
    	      client = new DeviceClient(connString, protocol);
    	      client.open();

    	      while (!stopThread) {    	    	  
    	    	
    	    	String msgFogger = jsonUtility.serialize(foggerEntity);    	    	

    	        Message msg = new Message(msgFogger);
 	        
    	        System.out.println(msgFogger);

    	        Object lockobj = new Object();
    	        EventCallback callback = new EventCallback();
    	        client.sendEventAsync(msg, callback, lockobj);

    	        synchronized (lockobj) {
    	          lockobj.wait();
    	        }
    	        Thread.sleep(5000);
    	      }
    	      client.close();
    	    } catch (Exception e) {
    	      e.printStackTrace();
    	    }
    	  }
    	}
}
