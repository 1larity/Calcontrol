//CalControl (c) Richard Beech 2013 plug in calendar control for android 2.2 up
//Apache 2 Licence
//Grid cell adaptor for Calcontrol

package com.digitale.calcontrol;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

//
public class GridCellAdapter extends BaseAdapter implements OnClickListener
{

	//Parent Calcontrol(viewgroup)
	private final Calcontrol calcontrol;
	//Debug tag
	private static final String tag = "GridCellAdapter";
	private static final boolean DEBUG = false;
	//App context
	private final Context _context;
	//Array holding the colour strings for tinting day numbers in grid
	private final List<String> list;
	//Array holding long English names of week days
	private final String[] weekdays = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	//Array holding long English names of months
	private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	//Array holding the number of days in each month
	private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private final int month, year;
	int daysInMonth, prevMonthDays;
	private final int currentDayOfMonth;
	private Button gridcell;

	// Days in Current Month
	public GridCellAdapter(Calcontrol calcontrol, Context context, int textViewResourceId, int month, int year)
	{
		super();
		this.calcontrol = calcontrol;
		this._context = context;
		this.list = new ArrayList<String>();
		this.month = month;
		this.year = year;

		Log.d(tag, "Month: " + month + " " + "Year: " + year);
		Calendar calendar = Calendar.getInstance();
		currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		printMonth(month, year);
	}

	public String getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public int getCount()
	{
		return list.size();
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
			list.add(String.valueOf((daysInPrevMonth - trailingSpaces + 1) + i)
					+ "-GRAY" + "-" + months[prevMonth] + "-" + prevYear);
			
		}

		// Compute if this day is in the active month, if so set fontcolour
		// to white, or set to cyan if this day is IRL today
		for (int i = 1; i <= daysInMonth; i++)
		{
			if (i==currentDayOfMonth){
				list.add(String.valueOf(i) + "-CYAN"
			+ "-" + months[mm] + "-" + yy);
			}else{
			list.add(String.valueOf(i) + "-WHITE" 
			+ "-" + months[mm] + "-" + yy);
			}
		}

		// Compute padding days before the first day of this month
		// set fontcolour for those cells to grey
		for (int i = 0; i < list.size() % 7; i++)
		{
			Log.d(tag, "NEXT MONTH:= " + months[nextMonth]);
			list.add(String.valueOf(i + 1) + "-GRAY"
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
		Log.d(tag, "getView ...");
		View row = convertView;
		if (row == null)
		{
			// ROW INFLATION
			Log.d(tag, "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.gridcell, parent, false);

			if (DEBUG)Log.d(tag, "Successfully completed XML Row Inflation!");
		}

		gridcell = (Button) row.findViewById(R.id.gridcell);
		gridcell.setOnClickListener(this);

		

		Log.d(tag, "Current Day: " + currentDayOfMonth);
		String[] day_color = list.get(position).split("-");
		gridcell.setText(day_color[0]);
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

	@Override
	public void onClick(View view)
	{
		String date_month_year = (String) view.getTag();
		Toast.makeText(this.calcontrol.getContext(), date_month_year, Toast.LENGTH_SHORT).show();

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
}