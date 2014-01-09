package com.leon.hfu.hotelReservationCalculation;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import com.leon.hfu.customDate.Date;

/**
 * Abstract class providing basic implementation on any reservable
 * object reservable by {@link Reservation} objects.
 * 
 * TODO: Add person count handling
 * 
 * @author	Stefan Hahn
 */
public abstract class ReservableObject implements Showable {
	/**
	 * VAT rate
	 */
	public static final double VAT_RATE = 0.07;
	
	private String name = "";
	private String address = "";
	private double basePrice = 0;
	private int maxPersons = 0;
	private HashMap<UUID, Reservation> reservations = new HashMap<>();
	
	/**
	 * Creaes a new <code>ReservableObject</code> objects with given parameters.
	 * 
	 * @param	name			Name of this <code>ReservableObjects</code>
	 * @param	address			Address of this <code>ReservableObjects</code>
	 * @param	basePrice		Base price of this <code>ReservableObjects</code>
	 * @param	maxPersons		Maximum amount of persons in this <code>ReservableObjects</code>
	 */
	public ReservableObject(String name, String address, double basePrice, int maxPersons) {
		this.setName(name);
		this.setBasePrice(basePrice);
		this.setAddress(address);
		this.setMaxPersons(maxPersons);
	}

	/**
	 * Adds a {@link Reservation} to this <code>ReservableObjects</code>.
	 * This method shouldn't be called manually, it is called automatically by
	 * {@link Reservation} within constructors.
	 * 
	 * @param	newReservation					{@link Reservation} object
	 * @throws	ConcurrentReservationException	Thrown if there are concurrent reservations
	 */
	void addReservation(Reservation newReservation) throws ConcurrentReservationException {
		if (this.reservations.size() > 0) {
			Set<UUID> ids = this.reservations.keySet();
			
			for (UUID id: ids) {
				Date currentArrival = this.reservations.get(id).getArrivalDate();
				Date currentDeparture = this.reservations.get(id).getDepartureDate();
				Date newArrival = newReservation.getArrivalDate();
				Date newDeparture = newReservation.getDepartureDate();
				
				if ((!(newArrival.compareTo(currentArrival) > 0) || !(newDeparture.compareTo(currentArrival) <= 0))
					&& (!(newArrival.compareTo(currentDeparture) >= 0) || !(newDeparture.compareTo(currentDeparture) > 0))) {
					throw new ConcurrentReservationException(this.reservations.get(id));
				}
			}
		}
		
		this.reservations.put(newReservation.getID(), newReservation);
	}
	
	/**
	 * Removes a {@link Reservation} from this <code>ReservableObject</code>.
	 * This method shouldn't be called manually, it is called automatically within
	 * {@link Reservation#cancel()}.
	 * 
	 * @param	reservation						{@link Reservation} object
	 * @throws	ReservationException			Thrown if the given {@link Reservation} objects isn't linked to this <code>ReservableObject</code>
	 */
	void cancelReservation(Reservation reservation) throws ReservationException {
		if (!this.reservations.containsKey(reservation.getID())) {
			throw new ReservationException("Reservierung ist für diese Ferienwohnung nicht gültig.");
		}
		
		this.reservations.remove(reservation.getID());
	}
	
	/**
	 * Returns the name of this vacation home
	 * 
	 * @return							Name of this vacation home
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name of this vacation home.
	 * 
	 * @param	name					New name of this vacation home
	 */
	public void setName(String name) {
		if (name.equals("")) {
			throw new IllegalArgumentException("Name cannot be empty.");
		}
		
		this.name = name;
	}
	
	/**
	 * returns the address of this vacation home.
	 * 
	 * @return							Address of this vacation home
	 */
	public String getAddress() {
		return this.address;
	}
	
	/**
	 * Sets the address of this vacation home.
	 * 
	 * @param	address					New address of this vacation home
	 */
	public void setAddress(String address) {
		if (address.equals("")) {
			throw new IllegalArgumentException("Address cannot be empty.");
		}
		
		this.address = address;
	}
	
	/**
	 * Returns the price per night of this vacation home.
	 * 
	 * @return							Price per night of this vacation home
	 */
	public double getBasePrice() {
		return this.basePrice;
	}
	
	/**
	 * Sets the price per night of this vacation home.
	 * 
	 * @param	basePrice			New price per night of this vacation home
	 */
	public void setBasePrice(double basePrice) {
		if (basePrice <= 0) {
			throw new IllegalArgumentException("Base price muste be greater then 0.");
		}
		
		this.basePrice = basePrice;
	}
	
	/**
	 * Returns the amount of beds in this vacation home.
	 * 
	 * @return							Amount of beds in this vacation home
	 */
	public int getMaxPersons() {
		return this.maxPersons;
	}
	
	/**
	 * Sets the amount of beds in this vacation home.
	 * 
	 * @param	maxPersons				New amount of beds in this vacation home
	 */
	public void setMaxPersons(int maxPersons) {
		if (maxPersons <= 0) {
			throw new IllegalArgumentException("Maximum person count cannot be lower then 1");
		}
		
		this.maxPersons = maxPersons;
	}
	
	/**
	 * Calculates the specific early bird discount dependant on implementation.
	 */
	abstract double getEarlyBirdDiscount(Reservation resveration);
	
	/**
	 * Calculates the specific quantitiy discount dependant on implementation.
	 */
	abstract double getQuantityDiscount(Reservation reservation);
	
	/**
	 * Prints basic information on every reservation linked to this <code>ReservableObjects</code>.
	 * 
	 * @see		Reservation#getInformation()
	 */
	public void printReservationInformation() {
		if (this.reservations.size() > 0) {
			Set<UUID> ids = this.reservations.keySet();
			
			for (UUID id: ids) {
				System.out.println(this.reservations.get(id).getInformation());
			}
		}
	}
	
	/**
	 * @see	Showable#show()
	 */
	@Override
	public void show() {
		System.out.println("Typ: " + this.getClass().getSimpleName());
		System.out.println("Name: " + this.getName());
		System.out.println("Adresse: " + this.getAddress());
		System.out.println("Basispreis: " + Reservation.formatPrice(this.getBasePrice()));
		System.out.println("Maximalpersonen: " + this.getMaxPersons());
	}
	
	/**
	 * @see	Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append(this.getName());
		s.append(" - ");
		s.append(this.getAddress());
		s.append(" - ");
		s.append(this.getMaxPersons());
		s.append(" Betten - ");
		s.append(Reservation.formatPrice(this.getBasePrice()));
		s.append(" pro Nacht");
		
		return s.toString();
	}
	
	/**
	 * @see	Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReservableObject)) {
			return false;
		}
		
		return this.toString().toLowerCase().equals(((ReservableObject) obj).toString().toLowerCase());
	}
}
