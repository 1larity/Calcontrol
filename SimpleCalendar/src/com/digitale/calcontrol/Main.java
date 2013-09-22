//Test harness for calcontrol
//(c)2013 Richard Beech, Apache 2 Licence
package com.digitale.calcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Main extends Activity{

	private Calcontrol calendar;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		addListenerOnCalendar();
	}
	
	public void addListenerOnCalendar() {
		 
		calendar = (Calcontrol) findViewById(R.id.myCalendar);
	
		calendar.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
 
				String output=calendar.getUserDate();
				Toast.makeText(getBaseContext(), output,
						Toast.LENGTH_SHORT).show();
				
 
			}
 
		});
		//test
		
	}

	
	}

