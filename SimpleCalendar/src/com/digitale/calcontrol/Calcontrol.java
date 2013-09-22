//CalControl (c) Richard Beech 2013 plug in calendar control for android 2.2 up
//Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
//  in compliance with the License. You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License
// is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and limitations under
// the License.


//features: Selectable android Themes, automatic layout of days in month within 
//a grid, with padding of previous and following month days.
// User touch input of selected day is returned as userDay
package com.digitale.calcontrol;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

{ 
private static final String m_tag = "CalControl";
private static final boolean DEBUG = false;
private Button m_currentMonth;
private ImageView m_prevMonth;
private ImageView m_nextMonth;
private GridView m_calendarGrid;
private GridCellAdapter m_adapter;
private Calendar m_calendar;
private String m_userDate;
private int m_month, m_year;
private String m_format;
private SimpleDateFormat m_formattedDate;
public Calcontrol(Context context) 
{
	super(context);
	if(!isInEditMode())
		init(context);
	
	
}

public Calcontrol(Context context, AttributeSet attrs) 
{
	super(context,attrs);
	if(!isInEditMode())
		init(context);
	
	
}


/*private void getRequestParameters()
			{
				Intent l_intent = getIntent();
				if (l_intent != null)
					{
						Bundle extras = l_intent.getExtras();
						if (extras != null)
							{
								if (extras != null)
									{
										Log.d(m_tag, "+++" + extras.getString("params"));
									}
							}
					}
			}*/

/** Called when the view is first created. */
private void init(Context context){


	try {
		m_userDate="unset";
		m_calendar = Calendar.getInstance(Locale.getDefault());
		m_month = m_calendar.get(Calendar.MONTH);
		m_year = m_calendar.get(Calendar.YEAR);
		m_format ="dd-MMMM-yyyy";
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.calendargrid, this,true);

		m_calendarGrid = (GridView) this.findViewById(R.id.calendar);
		m_prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		m_prevMonth.setOnClickListener(this);

		m_currentMonth = (Button) this.findViewById(R.id.currentMonth);
		Date date = m_calendar.getTime();
		m_formattedDate = new SimpleDateFormat(m_format,Locale.getDefault());
		m_currentMonth.setText(m_formattedDate.format(date));
		m_nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		m_nextMonth.setOnClickListener(this);



		// Initialised
		m_adapter = new GridCellAdapter(this, getContext(), R.id.gridcell, m_month, m_year);
		m_adapter.notifyDataSetChanged();
		m_calendarGrid.setAdapter(m_adapter);
		
	} catch (Exception e) {
		System.out.println("Calcontrol error "+e);
		Log.d(m_tag, "Calcontrol error " + e);
		e.printStackTrace();
	}

}
@Override
protected void onLayout(boolean changed, int l_l, int l_t, int l_r, int l_b) {
    
    for(int l_i = 0 ; l_i < getChildCount() ; l_i++){
        getChildAt(l_i).layout(l_l, l_t, l_r, l_b);
    }
}

@Override
protected void onFinishInflate(){


	super.onFinishInflate();
}

@Override
public void onClick(View view)
{
	Log.d(m_tag,view.toString());
	//if view is previous month button
	if (view == m_prevMonth)
	{
		if (m_month <= 1)
		{
			m_month = 11;
			m_year--;
		} else
		{
			m_month--;
		}

		if (DEBUG)Log.d(m_tag, "Before 1 MONTH " + "Month: " + m_month + " " + "Year: " + m_year);
		
		m_adapter = new GridCellAdapter(this, getContext(), R.id.gridcell, m_month, m_year);
		m_calendar.set(m_year, m_month, m_calendar.get(Calendar.DAY_OF_MONTH));
		Date date = m_calendar.getTime();
		
		m_currentMonth.setText(m_formattedDate.format(date));

		m_adapter.notifyDataSetChanged();
		m_calendarGrid.setAdapter(m_adapter);
	}
	//if view is next month button
	if (view == m_nextMonth)
	{
		if (m_month >= 11)
		{
			m_month = 0;
			m_year++;
		} else
		{
			m_month++;
		}
		
		if(DEBUG) Log.d(m_tag, "After 1 MONTH " + "Month: " + m_month + " " + "Year: " + m_year);
		
		m_adapter = new GridCellAdapter(this, getContext(), R.id.gridcell, m_month, m_year);
		m_calendar.set(m_year, m_month, m_calendar.get(Calendar.DAY_OF_MONTH));
		Date date = m_calendar.getTime();
		m_currentMonth.setText(m_formattedDate.format(date));
		m_adapter.notifyDataSetChanged();
		m_calendarGrid.setAdapter(m_adapter);
	}
	
}

/**
 * Store user selection when grid is touched
 */
@Override
public boolean onInterceptTouchEvent(MotionEvent l_ev) {
    
	if (l_ev.getActionMasked() == MotionEvent.ACTION_UP)
	{
	setUserDate(m_adapter.getUserDate());
	//Toast.makeText(getContext(), userDate, Toast.LENGTH_SHORT).show();
	}
	return false; 
}
/**
 * @return the userDate
 */
public String getUserDate() {
	return m_userDate;
}

/**
 * @param userDate the userDate to set
 */
public void setUserDate(String userDate) {
	this.m_userDate = userDate;
}


}