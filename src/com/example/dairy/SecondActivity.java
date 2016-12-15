package com.example.dairy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends Activity implements OnClickListener {

	private EditText edit;
	private Button btn;
	private TextView title;

	private Day day;
	private List<Day> diarylist;
	String[] month = { "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" };
	String[] week = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday", "Sunday" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		edit = (EditText) findViewById(R.id.edit);
		title = (TextView) findViewById(R.id.title);

		Intent intent = getIntent();
		day = (Day) intent.getSerializableExtra("data");

		edit.setText(day.getContent());
		edit.setSelection(day.getContent().length());
		title.setText(week[day.getWeek()] + "/" + month[day.getMonth()] + " "
				+ day.getDate() + "/" + day.getYear());

		btn = (Button) findViewById(R.id.save);
		btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		read();
		diarylist.get(day.getDate() - 1).setContent(edit.getText().toString());
		write();
		finish();
	}

	private void read() {
		FileInputStream in;
		ObjectInputStream objectin;

		String year = day.getYear() + "";
		String month = this.month[day.getMonth()];
		if (day.getMonth() != 5 && day.getMonth() != 6)
			month = month.substring(0, 3);
		else
			month = month.substring(0, 4);

		try {
			in = openFileInput(year + month);
			objectin = new ObjectInputStream(in);

			diarylist = (List<Day>) objectin.readObject();

			in.close();
			objectin.close();
		} catch (Exception exp) {
			diarylist = new ArrayList<Day>();
			int num = getDayInMonth(day.getYear(), day.getMonth());
			for (int i = 0; i < num; i++) {
				Day day = new Day();
				day.setYear(day.getYear());
				day.setMonth(day.getMonth());
				day.setDate(i + 1);

				Calendar calendar = Calendar.getInstance();
				calendar.set(day.getYear(), day.getMonth(), i);
				day.setWeek(calendar.get(Calendar.DAY_OF_WEEK) - 1);
				diarylist.add(day);
			}
		}
	}

	private void write() {
		String year = day.getYear() + "";
		String month = this.month[day.getMonth()];
		if (day.getMonth() != 5 && day.getMonth() != 6)
			month = month.substring(0, 3);

		int i;
		for (i = 0; i < diarylist.size(); i++) {
			if ("".equals(diarylist.get(i).getContent()))
				break;
		}
		if (i != diarylist.size()) {
			FileOutputStream out;
			ObjectOutputStream objectout;
			try {
				out = openFileOutput(year + month, Context.MODE_PRIVATE);
				objectout = new ObjectOutputStream(out);

				objectout.writeObject(diarylist);

				objectout.close();
				out.close();
			} catch (Exception exp) {
			}
		} else {
			File file = new File(getFilesDir().getPath() + "/" + year + month);
			if (file.exists())
				file.delete();
		}
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
