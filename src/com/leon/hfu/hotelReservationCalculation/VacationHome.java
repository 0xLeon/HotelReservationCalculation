package com.leon.hfu.hotelReservationCalculation;

import com.leon.hfu.customDate.Date;

/**
 * Represents a vacation home based on {@link ReservableObject}.
 * 
 * @author	Stefan Hahn
 */
public class VacationHome extends ReservableObject {
	private int stars = 0;
	
	/**
	 * Creates a new vacation home object with given attributes.
	 * 
	 * @param	name					Name of this vacation home
	 * @param	address					Address of this vacation home
	 * @param	basePrice				Price for one night in this vacation home
	 * @param	maxPersons				Amount of beds in this vacation home
	 * @param	stars					Stars rating this vacation home received, maximum of 5
	 * 
	 * @see		ReservableObject#ReservableObject(String, String, double, int)
	 */
	public VacationHome(String name, String address, double basePrice, int maxPersons, int stars) {
		super(name, address, basePrice, maxPersons);
		this.setStars(stars);
	}

	/**
	 * Returns the amount of stars of this vacation home.
	 * 
	 * @return							Amount of stars of this vacation home
	 */
	public int getStars() {
		return this.stars;
	}

	/**
	 * Sets the amount of stars of this vacation home.
	 * 
	 * @param	stars					New amount of stars of this vacation home
	 */
	public void setStars(int stars) {
		this.stars = stars;
	}
	
	/**
	 * @see		Showable#show()
	 * @see		ReservableObject#show()
	 */
	@Override
	public void show() {
		super.show();
		
		System.out.println("Sterne: " + this.getStars());
		
		this.printReservationInformation();
	}
	
	/**
	 * Calculates early bird discount for the given {@link Reservation}.
	 * 
	 * @return							Rounded early bird discount
	 * @see		ReservableObject#getEarlyBirdDiscount(Reservation)
	 */
	@Override
	double getEarlyBirdDiscount(Reservation reservation) {
		double discountRate = 0;
		int daysTillArrival = Date.getCurrentDate().delta(reservation.getArrivalDate());
		
		if (daysTillArrival >= 180) {
			discountRate = 0.1;
		}
		else if (daysTillArrival >= 90) {
			discountRate = 0.05;
		}
		
		return Reservation.roundPrice(this.getBasePrice() * reservation.getNights() * discountRate);
	}
	
	/**
	 * Calculates quantity discount for the given {@link Reservation}.
	 * 
	 * @return							Rounded quantity discount
	 */
	@Override
	double getQuantityDiscount(Reservation reservation) {
		return Reservation.roundPrice(this.getBasePrice() * reservation.getNights() - this.getQuantityDiscountedPrice(reservation.getNights()));
	}
	
	/**
	 * Calculates quanity discounted price for the given {@link Reservation}.
	 * 
	 * @param	n						Recursion parameter, must be amount of nights in initial call
	 * @return							Quantity discounted price
	 */
	private double getQuantityDiscountedPrice(int n) {
		if (n <= 1) {
			return this.getBasePrice();
		}
		else {
			return (0.88 / (n - 1)) * (this.getBasePrice() + this.getQuantityDiscountedPrice(n - 1));
		}
	}
	
	/**
	 * @see		Object#toString()
	 * @see		ReservableObject#toString()
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(super.toString());
		
		s.append(" - ");
		s.append(this.getStars());
		s.append(" Sterne");
		
		return s.toString();
	}
}
