//CalControl (c) Richard Beech 2013 plug in calendar control for android 2.2 up
//Apache 2 Licence
//Grid cell adaptor for Calcontrol

package com.digitale.calcontrol;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

//
public class GridCellAdapter extends BaseAdapter implements OnTouchListener
{

	//Parent Calcontrol(viewgroup)
	private final Calcontrol calcontrol;
	//Debug tag
	private static final String tag = "GridCellAdapter";
	//Debug flag
	private static final boolean DEBUG = false;
	private static final int TOP_LEFT = 1;
	//App context
	private final Context _context;
	//Array holding the colour strings for tinting day numbers in grid, and date
	private final List<String> dayList;
	//Array holding long English names of week days
	private final String[] weekdays = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	//Array holding long English names of months
	private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	//Array holding the number of days in each month
	private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private final int month, year;
	private String userDate; 
	int daysInMonth, prevMonthDays;
	private final int currentDayOfMonth;
	private Button gridcell;

	// Days in Current Month
	public GridCellAdapter(Calcontrol calcontrol, Context context, int textViewResourceId, int month, int year)
	{
		super();
		this.calcontrol = calcontrol;
		this._context = context;
		this.dayList = new ArrayList<String>();
		this.month = month;
		this.year = year;
		userDate="unset";
		if(DEBUG) Log.d(tag, "Month: " + month + " " + "Year: " + year);
		Calendar calendar = Calendar.getInstance();
		currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		printMonth(month, year);
	}

	public String getItem(int position)
	{
		return dayList.get(position);
	}

	@Override
	public int getCount()
	{
		return dayList.size();
	}

	private void printMonth(int mm, int yy)
	{
		// The number of days to leave blank at
		// the start of this month.
		int trailingSpaces = 0;
		//int leadSpaces = 0;
		int daysInPrevMonth = 0;
		int prevMonth = 0;
		int prevYear = 0;
		int nextMonth = 0;
		int nextYear = 0;

		GregorianCalendar cal = new GregorianCalendar(yy, mm, currentDayOfMonth);

		// Days in Current Month
		daysInMonth = daysOfMonth[mm];
		int currentMonth = mm;
		if (currentMonth == 11)
		{
			prevMonth = 10;
			daysInPrevMonth = daysOfMonth[prevMonth];
			nextMonth = 0;
			prevYear = yy;
			nextYear = yy + 1;
		} else if (currentMonth == 0)
		{
			prevMonth = 11;
			prevYear = yy - 1;
			nextYear = yy;
			daysInPrevMonth = daysOfMonth[prevMonth];
			nextMonth = 1;
		} else
		{
			prevMonth = currentMonth - 1;
			nextMonth = currentMonth + 1;
			nextYear = yy;
			prevYear = yy;
			daysInPrevMonth = daysOfMonth[prevMonth];
		}

	
		
		trailingSpaces = cal.get(Calendar.DAY_OF_WEEK) - 1;
		
		//add extraday if this month is February and this year is a leap year
		if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
		{
			++daysInMonth;
		}

		//Compute padding days after the last day of this month
		//set font colour for those cells to grey.
		for (int i = 0; i < trailingSpaces; i++)
		{
			dayList.add(String.valueOf((daysInPrevMonth - trailingSpaces + 1) + i)
					+ "-GRAY" + "-" + months[prevMonth] + "-" + prevYear);
			
		}

		// Compute if this day is in the active month, if so set fontcolour
		// to white, or set to cyan if this day is IRL today
		for (int i = 1; i <= daysInMonth; i++)
		{
			if (i==currentDayOfMonth){
				dayList.add(String.valueOf(i) + "-CYAN"
			+ "-" + months[mm] + "-" + yy);
			}else{
			dayList.add(String.valueOf(i) + "-WHITE" 
			+ "-" + months[mm] + "-" + yy);
			}
		}

		// Compute padding days before the first day of this month
		// set fontcolour for those cells to grey
		for (int i = 0; i < dayList.size() % 7; i++)
		{
			if(DEBUG) Log.d(tag, "NEXT MONTH:= " + months[nextMonth]);
			dayList.add(String.valueOf(i + 1) + "-GRAY"
			+ "-" + months[nextMonth] + "-" + nextYear);
		}
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(DEBUG) Log.d(tag, "getView ...");
		View row = convertView;
		if (row == null)
		{
			// ROW INFLATION
			if(DEBUG) Log.d(tag, "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.gridcell, parent, false);

			if (DEBUG)Log.d(tag, "Successfully completed XML Row Inflation!");
		}

		gridcell = (Button) row.findViewById(R.id.gridcell);
		gridcell.setOnTouchListener(this);
		
		Drawable background=compositBackground(parent);
		gridcell.setBackgroundDrawable(background);
		

		if(DEBUG) Log.d(tag, "Current Day: " + currentDayOfMonth);
		//decompose date and colour from day list
		String[] day_color = dayList.get(position).split("-");
		//set cell day number
		gridcell.setText(day_color[0]);
		//set cell tag with actual date
		gridcell.setTag(day_color[0] + "-" + day_color[2] + "-" + day_color[3]);
		// Set font colour of cells
		if (day_color[1].equals("BLACK"))
		{
			gridcell.setTextColor(Color.BLACK);
		}
		if (day_color[1].equals("GRAY"))
		{
			gridcell.setTextColor(Color.GRAY);
		}
		if (day_color[1].equals("RED"))
		{
			gridcell.setTextColor(Color.RED);
		}
		if (day_color[1].equals("WHITE"))
		{
			gridcell.setTextColor(Color.WHITE);
		}
		if (day_color[1].equals("CYAN"))
		{
			gridcell.setTextColor(Color.CYAN);
		}

		return row;
	}
	
	/**
	 * build state list images and composite icons onto images
	 */
	private Drawable compositBackground(ViewGroup parent) {
		Drawable bgDrawableUp;
		Drawable bgDrawableDown;
		
		Resources res=parent.getResources();
		
		//render bg bitmap for Up state
		Bitmap mBackgroundUp = BitmapFactory.decodeResource( res, R.drawable.froyo_cell );
		Bitmap mOverlayUp = BitmapFactory.decodeResource( res, R.drawable.left_arrow );
		//Process image, scale it
		Bitmap scaledBitmapUp =Bitmap.createScaledBitmap(mBackgroundUp, 128, 128, true);
		scaledBitmapUp=overlay(scaledBitmapUp, mOverlayUp,TOP_LEFT);
		//derive drawable
		bgDrawableUp=new BitmapDrawable(scaledBitmapUp);
		
		//render bg bitmap for pressed state
		Bitmap mBackgroundDown = BitmapFactory.decodeResource( res, R.drawable.froyo_cell_selected );
		//Process image, scale it
		Bitmap scaledBitmapDown =Bitmap.createScaledBitmap(mBackgroundDown, 128, 128, true);
		//derive drawable
		bgDrawableDown=new BitmapDrawable(scaledBitmapDown);
		
		//Construct state list
		StateListDrawable mIcon = new StateListDrawable();
		//set button DOWN state grapgic
        mIcon.addState(new int[] { android.R.attr.state_pressed },  bgDrawableDown);
        //set button UP (all other) state graphic
        mIcon.addState(StateSet.WILD_CARD, bgDrawableUp);
		
        //return the state list drawable;
        return mIcon;
	}

	/**
	 * bitmap overlay function
	 */
    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2, int overlayPosition) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }
	/**
	 * Store user selection when grid is touched
	 */
	@Override
	public boolean onTouch(View view, MotionEvent ev)
	{
		String date_month_year = (String) view.getTag();
		//Toast.makeText(this.calcontrol.getContext(), date_month_year, Toast.LENGTH_SHORT).show();
		
		setUserDate(date_month_year);
		Log.d(tag,getUserDate());
		return false;
		
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the weekdays
	 */
	public String[] getWeekdays() {
		return weekdays;
	}

	/**
	 * @return the userDate
	 */
	public  String getUserDate() {
		return this.userDate;
	}

	/**
	 * @param userDate the userDate to set
	 */
	public void setUserDate(String userDate) {
		this.userDate = userDate;
	

}
}