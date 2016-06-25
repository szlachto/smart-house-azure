package com.mycompany.app;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.microsoft.azure.iothub.DeviceClient;
import com.microsoft.azure.iothub.IotHubClientProtocol;
import com.microsoft.azure.iothub.IotHubEventCallback;
import com.microsoft.azure.iothub.IotHubMessageResult;
import com.microsoft.azure.iothub.IotHubStatusCode;
import com.microsoft.azure.iothub.Message;
import com.microsoft.azure.iothub.MessageProperty;

/**
 * Hello world!
 * 
 */
public class App {
    private static String connString = "HostName=iGrid.azure-devices.net;DeviceId=javadevice;SharedAccessKey=GpNHMH1HLCZU2zdv5teUCQ==";

    private static IotHubClientProtocol protocol = IotHubClientProtocol.HTTPS;
    private static boolean stopThread = false;

    public static void main(final String[] args) throws IOException, URISyntaxException {

        MessageSender ms0 = new MessageSender();
        Thread t0 = new Thread(ms0);
        t0.start();

        System.out.println("Press ENTER to exit.");
        System.in.read();
        ms0.stopThread = true;
    }

    private static class EventCallback implements IotHubEventCallback {
        public void execute(final IotHubStatusCode status, final Object context) {
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
        // private InputStream input;
        private List<HomeControllerEntry> entries;

        public void run() {
            try {

                // this.input = new
                // FileInputStream("resources/entityAzure.json");
                ReadEntity jsonUtility = new ReadEntity();
                // this.entries = jsonUtility.readJsonStream(this.input);

                this.entries = new ArrayList<HomeControllerEntry>();

                int index = 0;
                for (int i = 15; i < 36; ++i) {
                    this.entries.add(new HomeControllerEntry(i, i * i * 10, !(i < 30)));
                    ++index;
                }

                DeviceClient client;
                client = new DeviceClient(App.connString, App.protocol);
                client.open();          


                while (--index > 0) {

                    String msgFogger = jsonUtility.serialize(Arrays.asList(this.entries.get(index)));

                    Message msg = new Message(msgFogger);

                    System.out.println(msgFogger);

                    Object lockobj = new Object();
                    EventCallback callback = new EventCallback();                    
                    client.sendEventAsync(msg, callback, lockobj);     
                    
                    MessageCallback messageCallback = new MessageCallback();
                    Counter counter = new Counter(0);
                    client.setMessageCallback(messageCallback, counter);
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
    
    /** Used as a counter in the message callback. */
    protected static class Counter
    {
        protected int num;

        public Counter(int num)
        {
            this.num = num;
        }

        public int get()
        {
            return this.num;
        }

        public void increment()
        {
            this.num++;
        }

        @Override
        public String toString()
        {
            return Integer.toString(this.num);
        }
    }

    protected static class MessageCallback implements com.microsoft.azure.iothub.MessageCallback
    {
    	private int acPower = 0;
    	
        public IotHubMessageResult execute(Message msg, Object context)
        {
        	MessageProperty[] messageProperty = msg.getProperties();
        	acPower = new Integer(messageProperty[0].getValue().toString());
        	
        	
        	Counter counter = (Counter) context;
            System.out.println(
                    "Received message " + counter.toString()
                            + " with content: "+ messageProperty[0].getName().toString() + new String(msg.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));

            int switchVal = counter.get() % 3;
            IotHubMessageResult res;
            switch (switchVal)
            {
                case 0:
                    res = IotHubMessageResult.COMPLETE;
                    break;
                case 1:
                    res = IotHubMessageResult.ABANDON;
                    break;
                case 2:
                    res = IotHubMessageResult.REJECT;
                    break;
                default:
                    // should never happen.
                    throw new IllegalStateException("Invalid message result specified.");
            }

            System.out.println("Responding to message " + counter.toString() + " with " + res.name());

            counter.increment();

            return res;
        }
        
        public int getPower(){
        	return this.acPower;
        }
    } 
}

