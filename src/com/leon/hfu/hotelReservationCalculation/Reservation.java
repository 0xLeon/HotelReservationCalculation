package com.leon.hfu.hotelReservationCalculation;

import java.text.NumberFormat;
import java.util.UUID;

import com.leon.hfu.customDate.Date;

/**
 * Represents a reservation on any {@link ReservableObject}.
 * TODO: add person count handling
 * 
 * @author	Stefan Hahn
 */
public class Reservation implements Showable {
	private UUID ident = UUID.randomUUID();
	private Date arrival = null;
	private Date departure = null;
	private int nights = 0;
	private int persons = 0;
	private ReservableObject reservedReservableObject = null;
	
	/**
	 * Creates a new <code>Reservation</code> with given parameters.
	 * 
	 * @param	arrival					Arrival date of this <code>Reservation</code>
	 * @param	departure				Departure of this <code>Reservation</code>
	 * @param	persons					Persons of this <code>Reservation</code>
	 * @param	reservableObject		{@link ReservableObject} this <code>Reservation</code> is linked to
	 * @throws	ReservationException	Thrown if <code>Reservation</code> is corrupted, arrival in past, departure before arrival or concurrent reservations
	 */
	public Reservation(Date arrival, Date departure, int persons, ReservableObject reservableObject) throws ReservationException {
		this.arrival = arrival;
		this.departure = departure;
		this.persons = persons;
		
		if (Date.getCurrentDate().compareTo(this.arrival) > 0) {
			throw new ReservationException("Reservierung in der Vergangenheit nicht möglich.");
		}
		
		if (this.arrival.equals(this.departure)) {
			throw new ReservationException("Mindestaufenthalt von einer Nacht.");
		}
		else if (this.arrival.compareTo(this.departure) > 0) {
			throw new ReservationException("Datum der Abreise muss nach Datum der Ankunft sein.");
		}
		
		this.nights = this.arrival.delta(this.departure);
		this.reservedReservableObject = reservableObject;
		this.reservedReservableObject.addReservation(this);
	}
	
	/**
	 * Creates a new <code>Reservation</code> with given parameters.
	 * 
	 * @param	arrival					Arrival date of this <code>Reservation</code>
	 * @param	nights					Nights to sleep over of this <code>Reservation</code>
	 * @param	persons					Persons of this <code>Reservation</code>
	 * @param	reservableObject		{@link ReservableObject} this <code>Reservation</code> is linked to
	 * @throws	ReservationException	Thrown if <code>Reservation</code> is corrupted, arrival in past, departure before arrival or concurrent reservations	
	 */
	public Reservation(Date arrival, int nights, int persons, ReservableObject reservableObject) throws ReservationException {
		this(arrival, arrival.getFollowingDate(nights), persons, reservableObject);
	}
	
	/**
	 * Cancels this reservation.
	 * 
	 * @throws	ReservationException	Thrown if this reservation isn't linked to this {@link ReservableObject} anymore.
	 */
	public void cancel() throws ReservationException {
		this.reservedReservableObject.cancelReservation(this);
	}
	
	/**
	 * Returns the {@link UUID} object of this {@linkplain Reservation}.
	 * 
	 * @return							The {@link UUID} object of this {@linkplain Reservation}.
	 */
	public UUID getID() {
		return this.ident;
	}
	
	/**
	 * Gets the arrival {@link Date} of this <code>Reservation</code>.
	 * 
	 * @return							Arrival date
	 */
	public Date getArrivalDate() {
		return this.arrival;
	}
	
	/**
	 * Gets the departure {@link Date} of this <code>Reservation</code>.
	 * 
	 * @return							Departure date
	 */
	public Date getDepartureDate() {
		return this.departure;
	}
	
	/**
	 * Gets the nights to sleep over of this <code>Reservation</code>.
	 * 
	 * @return							Nights to sleep over
	 */
	public int getNights() {
		return this.nights;
	}
	
	/**
	 * Persons to sleep over of this <code>Reservation</code>.
	 * 
	 * @return							Persons of this <code>Reservation</code>.
	 */
	public int getPersons() {
		return this.persons;
	}
	
	/**
	 * Calculates pre tax price for this <code>Reservation</code>.
	 * 
	 * @return							Rounded pre tax price
	 */
	public double getPreTaxPrice() {
		return Reservation.roundPrice(this.reservedReservableObject.getBasePrice() * this.nights - this.getDiscount());
	}
	
	/**
	 * Calculates sales tax for this <code>Reservation</code>.
	 * 
	 * @return							Rounded sales tax
	 */
	public double getSalesTax() {
		return Reservation.roundPrice(this.reservedReservableObject.getBasePrice() * VacationHome.VAT_RATE * this.nights);
	}
	
	/**
	 * Calculates after tax price for this <code>Reservation</code>.
	 * 
	 * @return							Rounded after tax price
	 */
	public double getAfterTaxPrice() {
		return Reservation.roundPrice(this.getPreTaxPrice() + this.getSalesTax());
	}
	
	/**
	 * Calculates discount for this <code>Reservation</code>.
	 * 
	 * @return							Rounded discount
	 */
	public double getDiscount() {
		return Reservation.roundPrice(this.getEarlyBirdDiscount() + this.getQuantityDiscount());
	}
	
	/**
	 * Gets early bird discount for this <code>Reservation</code>.
	 * 
	 * @see		ReservableObject#getEarlyBirdDiscount(Reservation)
	 * @return							Rounded early bird discount
	 */
	public double getEarlyBirdDiscount() {
		return this.reservedReservableObject.getEarlyBirdDiscount(this);
	}
	
	/**
	 * Gets early quantity discount for this <code>Reservation</code>.
	 * 
	 * @see		ReservableObject#getQuantityDiscount(Reservation)
	 * @return							Rounded quantity discount
	 */
	public double getQuantityDiscount() {
		return this.reservedReservableObject.getQuantityDiscount(this);
	}
	
	/**
	 * Gets short information about this <code>Reservation</code>.
	 * 
	 * @return							Short infrmation string
	 */
	public String getInformation() {
		return "Reservierung in »" + this.reservedReservableObject.getName() + "« vom " + this.arrival + " bis zum " + this.departure + ".";
	}
	
	/**
	 * @see	Showable#show()
	 */
	@Override
	public void show() {
		System.out.println("Reservierung: " + this.getID());
		System.out.println("Ankunft: " + this.getArrivalDate());
		System.out.println("Abfahrt: " + this.getDepartureDate());
		System.out.println("Nächte: " + this.getNights());
		System.out.println("Personen: " + this.getPersons());
		System.out.println("Preis: " + Reservation.formatPrice(this.getAfterTaxPrice()));
		System.out.println("Reserviertes Objekt: " + this.reservedReservableObject);
	}
	
	/**
	 * @see	Object#toString()
	 */
	@Override
	public String toString() {
		return this.ident.toString();
	}
	
	/**
	 * @see	Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Reservation)) {
			return false;
		}
		
		return this.ident.equals(((Reservation) obj).getID());
	}

	/**
	 * Formats a double as price
	 * @param	price					Any double number
	 * @return							Formatted price
	 */
	public static String formatPrice(double price) {
		return NumberFormat.getCurrencyInstance().format(price); 
	}

	/**
	 * Rounds a double to two decimal places.
	 * 
	 * @param	price					Any double number
	 * @return							price rounded to two decimal places
	 */
	public static double roundPrice(double price) {
		return ((double) Math.round(price * 100)) / 100;
	}
}
