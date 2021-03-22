package cet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XirrDate {

	public static final double tol = 0.001;

	public static double dateDiff(Date d1, Date d2) {
		long day = 24 * 60 * 60 * 1000;

		return (d1.getTime() - d2.getTime()) / day;
	}

	public static double f_xirr(double payment, Date dt, Date dt0, double guess) {
		return payment * Math.pow((1.0 + guess), (dateDiff(dt0, dt) / 365.0));
	}

	public static double df_xirr(double payment, Date dt, Date dt0, double guess) {
		return (1.0 / 365.0) * dateDiff(dt0, dt) * payment * Math.pow((guess + 1.0), ((dateDiff(dt0, dt) / 365.0) - 1.0));
	}

	public static double total_f_xirr(double[] payments, Date[] days, double guess) {
		double result1 = 0.0;

		for (int i = 0; i < payments.length; i++) {
			result1 = result1 + f_xirr(payments[i], days[i], days[0], guess);
		}

		return result1;
	}

	public static double total_df_xirr(double[] payments, Date[] days, double guess) {
		double result2 = 0.0;

		for (int i = 0; i < payments.length; i++) {
			result2 = result2 + df_xirr(payments[i], days[i], days[0], guess);
		}

		return result2;
	}

	/*public static double Newtons_method(double guess, double[] payments, Date[] days) {
		double total = 0.0;
		double err = 1e+100;

		while (err > tol) {
			total = guess - total_f_xirr(payments, days, guess) / total_df_xirr(payments, days, guess);
			err = Math.abs(total - guess);
			guess = total;
		}

		return guess;
	}*/
	
	public static double Newtons_method(double guess, double[] payments, Date[] days) {
		double total = 0.0;
		double err = 1e+100;
		
		while (err > tol) {
			System.out.println("error: "+err);
			double result1 = 0.0;
			double result2 = 0.0;
			double diferenceDays = 0;
			
			for (int i = 0; i < payments.length; i++) {
				diferenceDays = dateDiff( days[0], days[i]);
				System.out.println("Dias: "+diferenceDays + " i: "+ i);
				result1 = result1 + (payments[i] * Math.pow((1.0 + guess), (diferenceDays / 365.0)));
				result2 = result2 + (1.0 / 365.0) * diferenceDays * payments[i] * Math.pow((guess + 1.0), ((diferenceDays / 365.0) - 1.0));
			}
			
			total = guess - result1 / result2;
			
			System.out.println("result1: "+total_f_xirr(payments, days, guess));
			System.out.println("result2: "+total_df_xirr(payments, days, guess));
			err = Math.abs(total - guess);
			guess = total;
			System.out.println("err :"+err + " guess: "+guess);
		}

		return guess;
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public static Date strToDate(String str) {
		try {
			return sdf.parse(str);
		} catch (ParseException ex) {
			return null;
		}
	}
	
	public static void main(String... args) {
        double[] payments = {-2000,428.53,428.53,428.53,428.53,428.53}; // payments
        Date[] days = {strToDate("31/10/2019"),strToDate("01/12/2019"),strToDate("01/01/2020"),strToDate("01/02/2020"),strToDate("01/03/2020"),strToDate("01/04/2020")}; // days of payment (as day of year)
        double xirr = Newtons_method(0.01, payments, days);

        System.out.println("XIRR value is " + xirr);    
    }
}