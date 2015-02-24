package sk.branislavremen.universityapp.vo;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

public class PlaceData {

	String title;
	String adresa;
	String typ;
	ParseGeoPoint gps;
	String detail;
	ParseFile picture;

	public PlaceData(String title, String adresa, String typ,
			ParseGeoPoint gps, String detail, ParseFile picture) {
		super();
		this.title = title;
		this.adresa = adresa;
		this.typ = typ;
		this.gps = gps;
		this.detail = detail;
		this.picture = picture;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public ParseGeoPoint getGps() {
		return gps;
	}

	public void setGps(ParseGeoPoint gps) {
		this.gps = gps;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public ParseFile getPicture() {
		return picture;
	}

	public void setPicture(ParseFile picture) {
		this.picture = picture;
	}

}
