//Test harness for calcontrol
//(c)2013 Richard Beech, Apache 2 Licence
package com.digitale.calcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {

	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button l_buttonOK  = (Button) findViewById(R.id.buttonOK);
		Button l_buttonCancel=(Button) findViewById(R.id.buttonCancel);
		final Calcontrol calendar = (Calcontrol) findViewById(R.id.myCalendar);
		
		
		l_buttonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String output = calendar.getUserDate();
				Intent data = new Intent();
				data.putExtra("returnDate", output);
				setResult(Activity.RESULT_OK, data);
				finish();
			}
		});
		
		l_buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String output = " cancel";
				Intent data = new Intent();
				data.putExtra("returnDate", output);
				setResult(Activity.RESULT_CANCELED, data);
				finish();
			}
		});
		
	}
}
