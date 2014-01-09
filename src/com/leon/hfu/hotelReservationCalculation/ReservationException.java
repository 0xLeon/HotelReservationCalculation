package com.leon.hfu.hotelReservationCalculation;

/**
 * Represents an exception within {@link Reservation} handling.
 * 
 * @author	Stefan Hahn
 */
public class ReservationException extends Exception {
	private static final long serialVersionUID = 7165110371351256941L;
	
	/**
	 * Creates a new <code>ReservationException</code> object with given message.
	 * 
	 * @param		message					Message text
	 */
	public ReservationException(String message) {
		super(message);
	}
	
	/**
	 * Creates a new <code>ReservationException</code> object with empty message.
	 */
	public ReservationException() {
		super();
	}
}
