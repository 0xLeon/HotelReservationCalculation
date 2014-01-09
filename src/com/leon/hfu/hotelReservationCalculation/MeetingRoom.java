package com.leon.hfu.hotelReservationCalculation;

/**
 * Represents a meeting room based on {@link ReservableObject}.
 * 
 * @author	Stefan Hahn
 */
public class MeetingRoom extends ReservableObject {
	/**
	 * Creates a new <code>MeetingRoom</code> object with given parameters. 
	 * 
	 * @param	name			Name of this <code>MeetingRoom</code>
	 * @param	address			Address of this <code>MeetingRoom</code>
	 * @param	basePrice		Base price of this <code>MeetingRoom</code>
	 * @param	maxPersons		Maximum amount of persons in< this <code>MeetingRoom</code>
	 * @see		ReservableObject#ReservableObject(String, String, double, int)
	 */
	public MeetingRoom(String name, String address, double basePrice, int maxPersons) {
		super(name, address, basePrice, maxPersons);
	}
	
	/**
	 * Always returns zero, because meeting rooms don't have any discount.
	 * 
	 * @return					Always zero
	 * @see		ReservableObject#getEarlyBIrdDiscount(Reservation)
	 */
	@Override
	double getEarlyBirdDiscount(Reservation reservation) {
		return 0;
	}
	
	/**
	 * Always returns zero, because meeting rooms don't have any discount.
	 * 
	 * @return					Always zero
	 * @see		ReservableObject#getQuantityDiscount(Reservation)
	 */
	@Override
	double getQuantityDiscount(Reservation reservation) {
		return 0;
	}
	
	/**
	 * @see		Showable#show()
	 * @see		ReservableObject#show()
	 */
	@Override
	public void show() {
		super.show();
		this.printReservationInformation();
	}
}
