package com.example.bean;

import java.io.Serializable;

public class BookingHistory  implements Serializable {

	private String bookingId;
	private String pickupLocation;
	private String dropLocation;
	private String fare;
	private String distanceKM;
	private String status;
	private String bookingDateTime;	
	
	public String getBookingId() {
		return bookingId;
	}
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	public String getPickupLocation() {
		return pickupLocation;
	}
	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}
	public String getDropLocation() {
		return dropLocation;
	}
	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}
	public String getFare() {
		return fare;
	}
	public void setFare(String fare) {
		this.fare = fare;
	}
	public String getDistanceKM() {
		return distanceKM;
	}
	public void setDistanceKM(String distanceKM) {
		this.distanceKM = distanceKM;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBookingDateTime() {
		return bookingDateTime;
	}
	public void setBookingDateTime(String bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

     
}
