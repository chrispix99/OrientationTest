package com.chrispix.orientation_test;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private static final String TAG = "TestApp";
	
	private TextView magneticView;
	private TextView gravityView;
	private TextView orientationView;
	
	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;
	
	private Float azimut;  // View to draw a compass
	private float[] mGravity;
	private float[] mGeomagnetic;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		magneticView = (TextView)findViewById(R.id.magnetic);
		gravityView = (TextView)findViewById(R.id.gravity);
		orientationView = (TextView)findViewById(R.id.orientation);
		
	    mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	    accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}



	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			  // On Galaxy Nexus events.values are different array lists based on the SensorEvent
			  // On Nexus 4 they are the same array object. 
			  android.util.Log.e(TAG,"GravityValues " + event.values.toString());
		      mGravity = event.values;
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			  android.util.Log.e(TAG,"MagValues " + event.values.toString());
		      mGeomagnetic = event.values;
		}

		
		if (mGravity != null && mGeomagnetic != null) {

		      float R[] = new float[9];
		      float I[] = new float[9];
		      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
		      
		      String gravity = "grav: ";
		      for (int i = 0; i < mGravity.length; i++) {
		    	  gravity = gravity + mGravity[i] + ", ";
		      }
		      
		      String magnetic = "mag: ";
		      for (int i = 0; i < mGeomagnetic.length; i++) {
		    	  magnetic = magnetic + mGeomagnetic[i] + ", ";
		      }

		      android.util.Log.e(TAG,"Sensor event: " + success + " " + gravity + " " + magnetic);
		      gravityView.setText(gravity);
		      magneticView.setText(magnetic);
		      
		      if (success) {

		        float orientation[] = new float[3];
		        SensorManager.getOrientation(R, orientation);
		        azimut = orientation[0]; // orientation contains: azimut, pitch and roll
		        
		        if (azimut != null) { 
		        	orientationView.setText(Math.toDegrees(azimut) + " degrees");
		        }
		        
		      }
		    }
		
	}

}
