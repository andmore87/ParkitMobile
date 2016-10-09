package com.andmore.parkitmobile.background.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ExpiredPreReserveReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent preReserveService = new Intent(context, ExpiredPreReserveService.class);
	       context.startService(preReserveService);
		
	}
	
	

}
