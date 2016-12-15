package com.example.dairy;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewMainAdapter extends ArrayAdapter<Day> {
	
	private int resourceId;
	private static String week[]={"Mon","Tue","Wed","Fur","Fri","Sat","Sun"};
	
	public ListViewMainAdapter(Context context, int textViewResourceId, List<Day> days){
		super(context, textViewResourceId, days);
		resourceId=textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Day day=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder=new ViewHolder();
			
			viewHolder.pointImage=(ImageView) view.findViewById(R.id.point);
			viewHolder.txt_date=(TextView) view.findViewById(R.id.txt_date);
			viewHolder.txt_week=(TextView) view.findViewById(R.id.txt_week);
			viewHolder.txt_content=(TextView) view.findViewById(R.id.txt_content);
			
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}
		if("".equals(day.getContent())){
			viewHolder.pointImage.setVisibility(View.VISIBLE);
			viewHolder.txt_date.setVisibility(View.GONE);
			viewHolder.txt_content.setVisibility(View.GONE);
			viewHolder.txt_week.setVisibility(View.GONE);
		}else{
			viewHolder.pointImage.setVisibility(View.GONE);
			viewHolder.txt_date.setVisibility(View.VISIBLE);
			viewHolder.txt_content.setVisibility(View.VISIBLE);
			viewHolder.txt_week.setVisibility(View.VISIBLE);
			
			viewHolder.txt_date.setText(day.getDate()+"");
			viewHolder.txt_content.setText(day.getContent());
			viewHolder.txt_week.setText(week[day.getWeek()]);
		}
		return view;
	}
	
	class ViewHolder{
		ImageView pointImage;
		TextView txt_date;
		TextView txt_week;
		TextView txt_content;
	}
}
