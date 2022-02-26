package com.gamebuster19901.heart;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class HC06 {
	
	public static BluetoothDevice searchForHC06() {
		return searchForHC06("HC-06");
	}

	public static BluetoothDevice searchForHC06(String name) {
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		
		BroadcastReceiver receiver = BroadcastReceiver;
		
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		adapter.startDiscovery();

	}
	
}
