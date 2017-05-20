package com.surine.homeme.MQTT;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PushCallBack implements MqttCallback {

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		System.out.println("链接断开");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		System.out.println("deliveryComplete----------"+arg0.isComplete());
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		Log.d("MQTT_MESSAGE", "接受消息内容"+new String(arg1.getPayload()));
		Log.d("MQTT_MESSAGE", "接收消息主题"+arg0);
		//System.out.println("接受消息Qos"+arg1.getQos());
	}

	
}
