//CalControl (c) Richard Beech 2013 plug in calendar control for android 2.2 up
//Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
//  in compliance with the License. You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License
// is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and limitations under
// the License.
//Grid cell adaptor for Calcontrol

package com.digitale.calcontrol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

//
public class GridCellAdapter extends BaseAdapter implements OnTouchListener {

	// Debug tag
	private static final String m_tag = "GridCellAdapter";
	// Debug flag
	private static final boolean DEBUG = false;
	// position constants for placing corner flags
	private static final int TOP_LEFT = 1;
	private static final int TOP_RIGHT = 2;
	private static final int BOTTOM_LEFT = 3;
	private static final int BOTTOM_RIGHT = 4;
	// App context
	private final Context m_context;
	// Array holding the colour strings for tinting day numbers in grid, and
	// date
	private final List<String> m_dayList;
	// Array holding long English names of week days
	private final String[] m_weekdays = new String[] { "Sun", "Mon", "Tue",
			"Wed", "Thu", "Fri", "Sat" };
	// Array holding long English names of months
	private final String[] m_months = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	// Array holding the number of days in each month
	private final int[] m_daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	private final int m_month, m_year;
	private String m_userDate;
	int m_daysInMonth, m_prevMonthDays;
	private final int m_currentDayOfMonth;
	private Button m_gridcell;

	// Days in Current Month
	public GridCellAdapter(Calcontrol calcontrol, Context context,
			int textViewResourceId, int month, int year) {
		super();

		this.m_context = context;
		this.m_dayList = new ArrayList<String>();
		this.m_month = month;
		this.m_year = year;
		m_userDate = "unset";
		if (DEBUG)
			Log.d(m_tag, "Month: " + month + " " + "Year: " + year);
		Calendar l_calendar = Calendar.getInstance();
		m_currentDayOfMonth = l_calendar.get(Calendar.DAY_OF_MONTH);

		printMonth(month, year);
	}

	public String getItem(int position) {
		return m_dayList.get(position);
	}

	@Override
	public int getCount() {
		return m_dayList.size();
	}

	private void printMonth(int mm, int yy) {
		// The number of days
		// to leave blank at
		// the start of this month.
		int l_trailingSpaces = 0;
		int l_daysInPrevMonth = 0;
		int l_prevMonth = 0;
		int l_prevYear = 0;
		int l_nextMonth = 0;
		int l_nextYear = 0;

		GregorianCalendar l_calendar = new GregorianCalendar(yy, mm,
				m_currentDayOfMonth);

		// Days in Current Month
		m_daysInMonth = m_daysOfMonth[mm];
		int l_currentMonth = mm;
		if (l_currentMonth == 11) {
			l_prevMonth = 10;
			l_daysInPrevMonth = m_daysOfMonth[l_prevMonth];
			l_nextMonth = 0;
			l_prevYear = yy;
			l_nextYear = yy + 1;
		} else if (l_currentMonth == 0) {
			l_prevMonth = 11;
			l_prevYear = yy - 1;
			l_nextYear = yy;
			l_daysInPrevMonth = m_daysOfMonth[l_prevMonth];
			l_nextMonth = 1;
		} else {
			l_prevMonth = l_currentMonth - 1;
			l_nextMonth = l_currentMonth + 1;
			l_nextYear = yy;
			l_prevYear = yy;
			l_daysInPrevMonth = m_daysOfMonth[l_prevMonth];
		}

		l_trailingSpaces = l_calendar.get(Calendar.DAY_OF_WEEK) - 1;

		// add extraday if this month is February and this year is a leap year
		if (l_calendar.isLeapYear(l_calendar.get(Calendar.YEAR)) && mm == 1) {
			++m_daysInMonth;
		}

		// Compute padding days after the last day of this month
		// set font colour for those cells to grey.
		for (int i = 0; i < l_trailingSpaces; i++) {
			m_dayList.add(String
					.valueOf((l_daysInPrevMonth - l_trailingSpaces + 1) + i)
					+ "-GRAY" + "-" + m_months[l_prevMonth] + "-" + l_prevYear);

		}

		// Compute if this day is in the active month, if so set fontcolour
		// to white, or set to cyan if this day is IRL today
		for (int l_i = 1; l_i <= m_daysInMonth; l_i++) {
			if (l_i == m_currentDayOfMonth) {
				m_dayList.add(String.valueOf(l_i) + "-CYAN" + "-"
						+ m_months[mm] + "-" + yy);
			} else {
				m_dayList.add(String.valueOf(l_i) + "-WHITE" + "-"
						+ m_months[mm] + "-" + yy);
			}
		}

		// Compute padding days before the first day of this month
		// set fontcolour for those cells to grey
		for (int l_i = 0; l_i < m_dayList.size() % 7; l_i++) {
			if (DEBUG)
				Log.d(m_tag, "NEXT MONTH:= " + m_months[l_nextMonth]);
			m_dayList.add(String.valueOf(l_i + 1) + "-GRAY" + "-"
					+ m_months[l_nextMonth] + "-" + l_nextYear);
		}
	}

	@Override
	public long getItemId(int l_position) {
		return l_position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (DEBUG)
			Log.d(m_tag, "getView ...");
		View l_row = convertView;
		if (l_row == null) {
			// ROW INFLATION
			if (DEBUG)
				Log.d(m_tag, "Starting XML Row Inflation ... ");
			LayoutInflater l_inflater = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			l_row = l_inflater.inflate(R.layout.gridcell, parent, false);

			if (DEBUG)
				Log.d(m_tag, "Successfully completed XML Row Inflation!");
		}

		m_gridcell = (Button) l_row.findViewById(R.id.gridcell);
		m_gridcell.setOnTouchListener(this);

		Drawable l_background = compositBackground(parent);
		m_gridcell.setBackgroundDrawable(l_background);

		if (DEBUG)
			Log.d(m_tag, "Current Day: " + m_currentDayOfMonth);
		// decompose date and colour from day list
		String[] day_color = m_dayList.get(position).split("-");
		// set cell day number
		m_gridcell.setText(day_color[0]);
		// set cell tag with actual date
		m_gridcell.setTag(day_color[0] + "-" + day_color[2] + "-"
				+ day_color[3]);
		// Set font colour of cells
		if (day_color[1].equals("BLACK")) {
			m_gridcell.setTextColor(Color.BLACK);
		}
		if (day_color[1].equals("GRAY")) {
			m_gridcell.setTextColor(Color.GRAY);
		}
		if (day_color[1].equals("RED")) {
			m_gridcell.setTextColor(Color.RED);
		}
		if (day_color[1].equals("WHITE")) {
			m_gridcell.setTextColor(Color.WHITE);
		}
		if (day_color[1].equals("CYAN")) {
			m_gridcell.setTextColor(Color.CYAN);
		}

		return l_row;
	}

	/**
	 * build state list images and composite icons onto images
	 */
	private Drawable compositBackground(ViewGroup parent) {
		Drawable bgDrawableUp=null;
		Drawable bgDrawableDown=null;

		Resources res = parent.getResources();

		// render bg bitmap for Up state
		Bitmap mBackgroundUp = BitmapFactory.decodeResource(res,
				R.drawable.froyo_cell);
		Bitmap mOverlayUp = BitmapFactory.decodeResource(res,
				R.drawable.cornertag);
		Bitmap scaledBitmapOverlay = Bitmap.createScaledBitmap(mOverlayUp, 32,
				32, true);
		// Process image, scale it
		Bitmap scaledBitmapUp = Bitmap.createScaledBitmap(mBackgroundUp, 128,
				128, true);
		for (int l_i = 0; l_i <= 3; l_i++){
		Random l_rand = new Random();
		int l_pickedpos = l_rand.nextInt(4);
		int l_pickedolour = l_rand.nextInt(4);
		switch (l_pickedolour) {
		case 0:
			scaledBitmapUp = overlay(scaledBitmapUp, scaledBitmapOverlay,
					l_pickedpos, Color.CYAN);
			break;
		case 1:
			scaledBitmapUp = overlay(scaledBitmapUp, scaledBitmapOverlay,
					l_pickedpos, Color.RED);
			break;
		case 2:
			scaledBitmapUp = overlay(scaledBitmapUp, scaledBitmapOverlay,
					l_pickedpos, Color.YELLOW);
			break;
		case 3:
			scaledBitmapUp = overlay(scaledBitmapUp, scaledBitmapOverlay,
					l_pickedpos, Color.GREEN);
			break;
		}
		// derive drawable
		bgDrawableUp = new BitmapDrawable(scaledBitmapUp);

		// render bg bitmap for pressed state
		Bitmap mBackgroundDown = BitmapFactory.decodeResource(res,
				R.drawable.froyo_cell_selected);
		// Process image, scale it
		Bitmap scaledBitmapDown = Bitmap.createScaledBitmap(mBackgroundDown,
				128, 128, true);
		switch (l_pickedolour) {
		case 0:
			scaledBitmapDown = overlay(scaledBitmapDown, scaledBitmapOverlay,
					l_pickedpos, Color.CYAN);
			break;
		case 1:
			scaledBitmapDown = overlay(scaledBitmapDown, scaledBitmapOverlay,
					l_pickedpos, Color.RED);
			break;
		case 2:
			scaledBitmapDown = overlay(scaledBitmapDown, scaledBitmapOverlay,
					l_pickedpos, Color.YELLOW);
			break;
		case 3:
			scaledBitmapDown = overlay(scaledBitmapDown, scaledBitmapOverlay,
					l_pickedpos, Color.GREEN);
			break;
		}
		// derive drawable
		bgDrawableDown = new BitmapDrawable(scaledBitmapDown);
		}
		// Construct state list
		StateListDrawable mIcon = new StateListDrawable();
		// set button DOWN state grapgic
		mIcon.addState(new int[] { android.R.attr.state_pressed },
				bgDrawableDown);
		// set button UP (all other) state graphic
		mIcon.addState(StateSet.WILD_CARD, bgDrawableUp);

		// return the state list drawable;
		return mIcon;
	}

	/**
	 * bitmap overlay function
	 * 
	 * @param colour
	 */
	private Bitmap overlay(Bitmap bmp1, Bitmap bmp2, int overlayPosition,
			int colour) {
		Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(),
				bmp1.getHeight(), bmp1.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bmp1, new Matrix(), null);
		Paint p = new Paint(colour);
		ColorFilter filter = new LightingColorFilter(colour, 1);
		p.setColorFilter(filter);

		if (overlayPosition == TOP_LEFT) {
			canvas.drawBitmap(bmp2, new Matrix(), p);

		} else if (overlayPosition == TOP_RIGHT) {
			canvas.drawBitmap(bmp2, 128 - 32, 0, p);

		} else if (overlayPosition == BOTTOM_LEFT) {
			canvas.drawBitmap(bmp2, 0, 128 - 32, p);

		} else if (overlayPosition == BOTTOM_RIGHT) {
			canvas.drawBitmap(bmp2, 128 - 32, 128 - 32, p);

		}
		return bmOverlay;
	}

	/**
	 * Store user selection when grid is touched * @param view * @param
	 * motionevent
	 */
	@Override
	public boolean onTouch(View view, MotionEvent ev) {
		String date_month_year = (String) view.getTag();
		// Toast.makeText(this.calcontrol.getContext(), date_month_year,
		// Toast.LENGTH_SHORT).show();

		setUserDate(date_month_year);
		Log.d(m_tag, getUserDate());
		return false;

	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return m_year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return m_month;
	}

	/**
	 * @return the weekdays
	 */
	public String[] getWeekdays() {
		return m_weekdays;
	}

	/**
	 * @return the userDate
	 */
	public String getUserDate() {
		return this.m_userDate;
	}

	/**
	 * @param userDate
	 *            the userDate to set
	 */
	public void setUserDate(String userDate) {
		this.m_userDate = userDate;

	}
}