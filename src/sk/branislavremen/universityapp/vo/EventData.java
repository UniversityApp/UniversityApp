package sk.branislavremen.universityapp.vo;

import java.util.Date;

public class EventData {

	private String eventTitle;
	private String eventDescription;
	private Date eventStartDate;
	private Date eventEndDate;
	private String eventPlaceData;

	public EventData(String title, String descr, Date start, Date end,
			String place) {
		// TODO Auto-generated constructor stub
		this.eventTitle = title;
		this.eventDescription = descr;
		this.eventStartDate = start;
		this.eventEndDate = end;
		this.eventPlaceData = place;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Date getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(Date eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public Date getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public String getEventPlaceData() {
		return eventPlaceData;
	}

	public void setEventPlaceData(String eventPlaceData) {
		this.eventPlaceData = eventPlaceData;
	}

}
