package com.example.dairy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {
	private String[] month = { "Jan", "Feb", "Mar", "Apr", "May", "June",
			"July", "Aug", "Sept", "Oct", "Nov", "Dec" };
	private String[] year = { "2015", "2016", "2017", "2018" };
	private Button bmonth;
	
	ListViewMainAdapter pointAdapter;
	private int index_year;
	
	private int index_month;
	
	private Button byear;
	private Button badd;
	ListView listView_point;
	private List<Day> diarylist;
	
	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.year:
			AlertDialog.Builder dialog_year = new AlertDialog.Builder(
					MainActivity.this);
			View view_year = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.list, null);

			ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
					MainActivity.this, android.R.layout.simple_list_item_1,
					year);
			ListView listView_year = (ListView) view_year
					.findViewById(R.id.month_ListView);
			listView_year.setAdapter(yearAdapter);
			dialog_year.setView(view_year);
			final AlertDialog dialog2 = dialog_year.create();
			dialog2.show();
			listView_year.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					index_year = position;
					write();
					byear.setText(year[index_year]);
					read();
					pointAdapter.notifyDataSetChanged();
					dialog2.dismiss();
				}

			});
			break;
		case R.id.month:
			AlertDialog.Builder dialog_month = new AlertDialog.Builder(
					MainActivity.this);
			View view_month = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.list, null);

			ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(
					MainActivity.this, android.R.layout.simple_list_item_1,
					month);
			ListView listView_month = (ListView) view_month
					.findViewById(R.id.month_ListView);
			listView_month.setAdapter(monthAdapter);
			dialog_month.setView(view_month);
			final AlertDialog dialog = dialog_month.create();
			dialog.show();
			listView_month.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					index_month = position;
					write();
					bmonth.setText(month[index_month]);
					read();
					pointAdapter.notifyDataSetChanged();
					dialog.dismiss();
				}

			});
			break;
		case R.id.add:
			write();
			
			Calendar calendar = Calendar.getInstance();
			index_year=calendar.get(Calendar.YEAR)-2015;
			index_month=calendar.get(Calendar.MONTH);
			byear.setText(year[index_year]);
			bmonth.setText(month[index_month]);
			read();
			
			Day diary = diarylist.get(calendar.get(Calendar.DATE) - 1);
			Intent intent=new Intent(MainActivity.this,SecondActivity.class);
			intent.putExtra("data", diary);
			startActivity(intent);
		default:
			break;
		}
	}
	
	private void write() {
		if (diarylist != null) {
			int i;
			for (i = 0; i < diarylist.size(); i++) {
				if ("".equals(diarylist.get(i).getContent()))
					break;
			}
			if (i != diarylist.size()) {
				FileOutputStream out;
				ObjectOutputStream objectout;
				try {
					out = openFileOutput(
							byear.getText() + "" + bmonth.getText(),
							Context.MODE_PRIVATE);
					objectout = new ObjectOutputStream(out);

					objectout.writeObject(diarylist);

					objectout.close();
					out.close();
				} catch (Exception exp) {
				}
			} else {
				File file = new File(getFilesDir().getPath() + "/"
						+ byear.getText() + bmonth.getText());
				if (file.exists())
					file.delete();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		byear = (Button) findViewById(R.id.year);
		byear.setOnClickListener(this);
		badd=(Button) findViewById(R.id.add);
		badd.setOnClickListener(this);
		bmonth = (Button) findViewById(R.id.month);
		bmonth.setOnClickListener(this);

		index_month = index_year = 0;
		bmonth.setText(month[0]);
		byear.setText(year[0]);

		diarylist = null;
		read();
		
		pointAdapter = new ListViewMainAdapter(this,
				R.layout.listview_main_item, diarylist);
		listView_point = (ListView) findViewById(R.id.listView_main);
		listView_point.setAdapter(pointAdapter);
		listView_point.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						SecondActivity.class);
				intent.putExtra("data", diarylist.get(position));
				startActivity(intent);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void read() {
		FileInputStream in;
		ObjectInputStream objectin;
		try {
			in = openFileInput(byear.getText() + "" + bmonth.getText());
			objectin = new ObjectInputStream(in);
			if (diarylist == null)
				diarylist = (List<Day>) objectin.readObject();
			else {
				diarylist.clear();
				List<Day> templist = (List<Day>) objectin.readObject();
				for (int i = 0; i < templist.size(); i++)
					diarylist.add(templist.get(i));
			}
			in.close();
			objectin.close();
		} catch (Exception exp) {
			if (diarylist == null)
				diarylist = new ArrayList<Day>();
			else
				diarylist.clear();
			int num = getDayInMonth(index_year + 2015, index_month);
			for (int i = 0; i < num; i++) {
				Day day = new Day();
				day.setYear(index_year + 2015);
				day.setMonth(index_month);
				day.setDate(i+1);

				Calendar calendar = Calendar.getInstance();
				calendar.set(index_year + 2015, index_month, i);
				day.setWeek(calendar.get(Calendar.DAY_OF_WEEK) - 1);
				diarylist.add(day);
			}
		}
	}

	@Override
	protected void onRestart(){
		super.onRestart();
		read();
		pointAdapter.notifyDataSetChanged();
	}
	
	private int getDayInMonth(int year, int month) {
		int day;
		month += 1;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			day = 31;
		} else if (month == 2) {
			if (year % 400 == 0 || year % 100 != 0 && year % 4 == 0)
				day = 29;
			else
				day = 28;
		} else {
			day = 30;
		}
		return day;
	}

}
