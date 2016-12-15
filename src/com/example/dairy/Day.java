package com.example.dairy;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Day implements Serializable {
	
	private int month;
	private int year;
	private int week;
	private int date;
	private String content;
	
	public Day(){
		content=new String();
	}
	
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content=content;
	}
	
	public int getYear(){
		return year;
	}
	public void setYear(int year){
		this.year=year;
	}
	
	public int getMonth(){
		return month;
	}
	public void setMonth(int month){
		this.month=month;
	}
	
	public int getDate(){
		return date;
	}
	public void setDate(int date){
		this.date=date;
	}
	
	public void setWeek(int week){
		this.week=week;
	}
	public int getWeek(){
		return week;
	}
}
