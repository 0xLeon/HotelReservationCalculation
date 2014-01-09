package com.leon.hfu.hotelReservationCalculation;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import com.leon.hfu.customDate.Date;
import com.leon.hfu.customDate.DateFormatException;

/**
 * Main class containing the whole program.
 * 
 * @author	Stefan Hahn
 */
public final class Main {
	private Main() { }
	
	/**
	 * Starts the program.
	 * Asks for the date of arrival and nights to sleep over. Prints various
	 * information about all defined {@link ReservableObject} instances and
	 * their state of reservation.
	 * 
	 * @param		args		Console parameters
	 */
	public static void main(String[] args) {
		Date arrivalDate = null;
		int nights = 0;
		
		Vector<ReservableObject> vacationHomes = new Vector<>(Arrays.asList(new ReservableObject[] {
				new VacationHome("Abstellkammer", "Hinterhof 17", 23, 1, 2),
				new VacationHome("Abendruh", "Sonnenweg 3", 56.7, 4, 4),
				new VacationHome("Schwarzwaldpalast", "Baumallee 1", 79.9, 5, 5),
				new MeetingRoom("Tagungshotel", "Stuttgarter Straße 15", 100, 50),
				new MeetingRoom("Veranstaltungsraum Hotel Foo", "Bar Straße 120", 150, 100)
		}));
		
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Gib das Datum der Ankunft ein.\n > ");
			arrivalDate = new Date(scanner.nextLine());
			
			System.out.print("Gib die Dauer des Aufenthalts in Tagen an.\n > ");
			nights = Integer.parseInt(scanner.nextLine()) - 1;
			
			for (ReservableObject vacationHome: vacationHomes) {
				showObjects(new Reservation(arrivalDate, nights, 3, vacationHome));
				showObjects(vacationHome);
			}
			
			System.out.println("-------------------------------------------------");
			showObjects(new Reservation(arrivalDate.getFollowingDate(), 1, 1, vacationHomes.get(4)));
			showObjects(vacationHomes.get(4));
		}
		catch (DateFormatException | ReservationException | NumberFormatException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Calls {@link Showable#show() show()} method on given {@link Showable} object.
	 * 
	 * @param		showable	Any object implementing {@link Showable} interface.
	 */
	public static void showObjects(Showable showable) {
		System.out.println("-------------------------------------------------");
		showable.show();
	}
}
