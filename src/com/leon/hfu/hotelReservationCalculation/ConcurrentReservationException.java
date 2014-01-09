package com.leon.hfu.hotelReservationCalculation;

/**
 * Represents an exception when trying to reserve a {@link ReservableObject} which is already
 * reserved in the period specified by the new {@link Reservation}.
 * A {@link Reservation} objects must be linked with instances of this exception pointing to
 * the reservation blocking the new reservation.
 * 
 * @author	Stefan Hahn
 */
public class ConcurrentReservationException extends ReservationException {
	private static final long serialVersionUID = 1634038818031626097L;
	
	private Reservation concurringReservation;
	
	/**
	 * Creates a new <code>ConcurrentReservationException</code> object with given message
	 * and concurring {@link Reservation}.
	 * 
	 * @param	message					Message text
	 * @param	concurringReservation	{@link Reservation} blocking a new reservation
	 */
	public ConcurrentReservationException(String message, Reservation concurringReservation) {
		super(message);
		this.concurringReservation = concurringReservation;
	}
	
	/**
	 * Creates a new <code>ConcurrentReservationException</code> object with given
	 * concurring {@link Reservation}.
	 * 
	 * @param	concurringReservation	{@link Reservation} blocking a new reservation
	 */
	public ConcurrentReservationException(Reservation concurringReservation) {
		this("Überschneidende Buchungen", concurringReservation);
	}
	
	/**
	 * @see		Throwable#getMessage()
	 * @see		Exception#getMessage()
	 */
	@Override
	public String getMessage() {
		return super.getMessage() + ": " + this.concurringReservation.getID();
	}
}
