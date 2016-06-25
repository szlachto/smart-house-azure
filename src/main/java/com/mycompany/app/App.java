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

    private static int getPowerConsumption(final int acMode) {
        return 5_000 * acMode;
    }

    // 3 s -> 30 min
    private static double getTemperature(final double currentTemperature, final int acMode) {
        double result = currentTemperature - acMode * 0.5
            + (App.getExternalTemperature() > App.internalTemperature ? App.getExternalTemperature() / 40 : 0);
        return result >= 16 ? result : 16;
    }

    private static double getExternalTemperature() {
        return 35;
    }

    private static double internalTemperature = 36;
    private static int acMode = 2;

    private static long dayTime = 0;

    private static class MessageSender implements Runnable {
        public volatile boolean stopThread = false;
        // private InputStream input;
        private List<HomeControllerEntry> entries;

        public void run() {
            try {

                ReadEntity jsonUtility = new ReadEntity();

                this.entries = new ArrayList<HomeControllerEntry>();


                DeviceClient client;
                client = new DeviceClient(App.connString, App.protocol);
                client.open();


                while (!this.stopThread) {

                    String msgFogger = jsonUtility.serialize(Arrays.asList(new HomeControllerEntry(getDateTimeAsString(),
                        App.internalTemperature, getExternalTemperature(), getPowerConsumption(App.acMode), true)));
                    App.internalTemperature = getTemperature(App.internalTemperature, App.acMode);
                    App.dayTime = (App.dayTime + 30) % (24 * 60);

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

        /**
         * <p>
         * <b> TODO : Insert description of the method's responsibility/role.
         * </b>
         * </p>
         * 
         * @return
         */
        private String getDateTimeAsString() {
            return "" + String.format("%02d", App.dayTime / 60) + ":" + String.format("%02d", App.dayTime % 60);
        }
    }

    /** Used as a counter in the message callback. */
    protected static class Counter {
        protected int num;

        public Counter(final int num) {
            this.num = num;
        }

        public int get() {
            return this.num;
        }

        public void increment() {
            this.num++;
        }

        @Override
        public String toString() {
            return Integer.toString(this.num);
        }
    }

    protected static class MessageCallback implements com.microsoft.azure.iothub.MessageCallback {
        private int acPower = 0;

        public IotHubMessageResult execute(final Message msg, final Object context) {
            MessageProperty[] messageProperty = msg.getProperties();
            this.acPower = new Integer(messageProperty[0].getValue().toString());


            Counter counter = (Counter) context;
            System.out.println("Received message " + counter.toString() + " with content: "
                + messageProperty[0].getName().toString() + new String(msg.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));

            int switchVal = counter.get() % 3;
            IotHubMessageResult res;
            switch (switchVal) {
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

        public int getPower() {
            return this.acPower;
        }
    }
}
