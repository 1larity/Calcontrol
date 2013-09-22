//CalControl (c) Richard Beech 2013 plug in calendar control for android 2.2 up
//Apache 2 Licence
//features: Selectable android Themes, automatic layout of days in month within 
//a grid, with padding of previous and following month days.
// User touch input of selected day is returned as userDay
package com.digitale.calcontrol;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Calcontrol extends LinearLayout   implements OnClickListener

{ Context appContext;
private static final String tag = "CalConntrol";
private Button currentMonth;
private ImageView prevMonth;
private ImageView nextMonth;
private GridView calendarGrid;
private GridCellAdapter adapter;
private Calendar _calendar;
private static String userDate;
private int month, year;
private String format;
private SimpleDateFormat formattedDate;
private static final boolean DEBUG = false;
public Calcontrol(Context context) 
{
	super(context);
	if(!isInEditMode())
		init(context);
	appContext=context;
	
}

public Calcontrol(Context context, AttributeSet attrs) 
{
	super(context,attrs);
	if(!isInEditMode())
		init(context);
	appContext=context;
	
}


/*private void getRequestParameters()
			{
				Intent intent = getIntent();
				if (intent != null)
					{
						Bundle extras = intent.getExtras();
						if (extras != null)
							{
								if (extras != null)
									{
										Log.d(tag, "+++++----------------->" + extras.getString("params"));
									}
							}
					}
			}*/

/** Called when the view is first created. */
private void init(Context context){


	try {
		userDate="unset";
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH);
		year = _calendar.get(Calendar.YEAR);
		format ="dd-MMMM-yyyy";
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.calendargrid, this,true);

		calendarGrid = (GridView) this.findViewById(R.id.calendar);
		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (Button) this.findViewById(R.id.currentMonth);
		Date date = _calendar.getTime();
		formattedDate = new SimpleDateFormat(format,Locale.getDefault());
		currentMonth.setText(formattedDate.format(date));
		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);



		// Initialised
		adapter = new GridCellAdapter(this, getContext(), R.id.gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarGrid.setAdapter(adapter);
		
	} catch (Exception e) {
		System.out.println("Calcontrol error "+e);
		Log.d(tag, "Calcontrol error " + e);
		e.printStackTrace();
	}

}
@Override
protected void onLayout(boolean changed, int l, int t, int r, int b) {
    
    for(int i = 0 ; i < getChildCount() ; i++){
        getChildAt(i).layout(l, t, r, b);
    }
}

@Override
protected void onFinishInflate(){


	super.onFinishInflate();
}

@Override
public void onClick(View view)
{
	Log.d(tag,view.toString());
	//if view is previous month button
	if (view == prevMonth)
	{
		if (month <= 1)
		{
			month = 11;
			year--;
		} else
		{
			month--;
		}

		if (DEBUG)Log.d(tag, "Before 1 MONTH " + "Month: " + month + " " + "Year: " + year);
		
		adapter = new GridCellAdapter(this, getContext(), R.id.gridcell, month, year);
		_calendar.set(year, month, _calendar.get(Calendar.DAY_OF_MONTH));
		Date date = _calendar.getTime();
		
		currentMonth.setText(formattedDate.format(date));

		adapter.notifyDataSetChanged();
		calendarGrid.setAdapter(adapter);
	}
	//if view is next month button
	if (view == nextMonth)
	{
		if (month >= 11)
		{
			month = 0;
			year++;
		} else
		{
			month++;
		}
		
		if(DEBUG) Log.d(tag, "After 1 MONTH " + "Month: " + month + " " + "Year: " + year);
		
		adapter = new GridCellAdapter(this, getContext(), R.id.gridcell, month, year);
		_calendar.set(year, month, _calendar.get(Calendar.DAY_OF_MONTH));
		Date date = _calendar.getTime();
		currentMonth.setText(formattedDate.format(date));
		adapter.notifyDataSetChanged();
		calendarGrid.setAdapter(adapter);
	}
	
}

/**
 * Store user selection when grid is touched
 */
@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
    
	if (ev.getActionMasked() == MotionEvent.ACTION_UP)
	{
	setUserDate(adapter.getUserDate());
	Toast.makeText(getContext(), userDate, Toast.LENGTH_SHORT).show();
	}
	return false; 
}
/**
 * @return the userDate
 */
public static String getUserDate() {
	return userDate;
}

/**
 * @param userDate the userDate to set
 */
public void setUserDate(String userDate) {
	this.userDate = userDate;
}


}