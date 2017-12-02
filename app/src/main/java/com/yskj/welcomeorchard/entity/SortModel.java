package com.yskj.welcomeorchard.entity;

public class SortModel {

	private String name;//显示的数据
	private String sortLetters;//显示数据拼音的首字母
	private int cityId; //城市id
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCity_Id() {
		return cityId;
	}
	public void setCity_Id(int cityId) {
		this.cityId = cityId;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
