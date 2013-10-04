/**
 * A very quick and dirty ceasar cipher coder/decoder for Android
 * Written to determine the cipher in a GCHQ code breaking competition
 * (C) Richard Beech 2013, Released under the Apache 2 licence
 */

package com.digitale.decrypt;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	String encrypted = "SECOND LAW OF PLANETARY {MOTION}: A LINE THAT CONNECTS A PLANET TO THE SUN SWEEPS OUT EQUAL AREAS IN EQUAL TIMES";
	// String encrypted = "ABCDEF";
	String decrypted;
	EditText outputText;
	EditText inputText;
	int letterTotal[];

	int i = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		letterTotal = new int[76];
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		outputText = (EditText) findViewById(R.id.textViewOutput);
		inputText = (EditText) findViewById(R.id.textViewInput);
		TextView iterationCount = (TextView) findViewById(R.id.textView1);
		iterationCount.setText("Letter Shift: " + i);
		/**
		 * set cyphertext.
		 */
		encrypted = "" + inputText.getText();
		decrypted = decryptshit(encrypted, i);
		outputText.setText(decrypted);

		Button button = (Button) findViewById(R.id.button1);
		Button buttonReset = (Button) findViewById(R.id.buttonReset);
		/**
		 * apply rotation shift to text.
		 */
		inputText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				i++;
				if (i > 26)
					i = 1;
				TextView iterationCount = (TextView) findViewById(R.id.textView1);

				iterationCount.setText("Letter Shift: " + i);
				encrypted = "" + inputText.getText();
				decrypted = decryptshit(encrypted, i);
				outputText.setText(decrypted);

			}
		});
		/**
		 * apply rotation shift to text.
		 */
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				i++;
				if (i > 76)
					i = 1;
				TextView iterationCount = (TextView) findViewById(R.id.textView1);

				iterationCount.setText("Letter Shift: " + i);
				encrypted = "" + inputText.getText();
				decrypted = decryptshit(encrypted, i);
				outputText.setText(decrypted);

			}
		});

		/**
		 * Reset iteration count button.
		 */
		buttonReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				i = 1;
				TextView iterationCount = (TextView) findViewById(R.id.textView1);
				iterationCount.setText("Letter Shift: " + i);
				/**
				 * set cyphertext.
				 */
				encrypted = "" + inputText.getText();
				decrypted = decryptshit(encrypted, i);
				outputText.setText(decrypted);

			}
		});
	}

	private String decryptshit(String encrypted, int offset) {
		String decrypt = "";
		// for (int i =0; i<25; i++){
		for (int j = 0; j < encrypted.length(); j++) {
			char character = encrypted.charAt(j); // This gives the current char
			int ascii = (int) character; // ascii is now ascii value.
			if (ascii > 47 && ascii < 123) {// ignore non apha
				// add offset to currentvalue
				ascii = ascii - 47;
				// tally letter use, for letter frquency analysis
				letterTotal[ascii] = letterTotal[ascii] + 1;
				ascii = (ascii + offset) % 76;
				ascii = ascii + 47;
				String newchar = String.valueOf(Character.toChars(ascii));

				decrypt = decrypt + newchar;
			} else {
				// re-append non alpha char
				decrypt = decrypt + character;
			}
		}
		// }
		return decrypt;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
