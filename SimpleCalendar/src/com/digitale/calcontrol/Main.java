//Test harness for calcontrol
//(c)2013 Richard Beech, Apache 2 Licence
package com.digitale.calcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {

	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button button  = (Button) findViewById(R.id.button1);
		final Calcontrol calendar = (Calcontrol) findViewById(R.id.myCalendar);
		
		calendar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String output = calendar.getUserDate() + " cal output";
				Toast.makeText(getBaseContext(), output, Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String output = calendar.getUserDate() + " button output";
				Toast.makeText(getBaseContext(), output, Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		
		
	}
}
