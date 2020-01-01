package project.griffin.newbold.calculus;

import java.text.DecimalFormat;
import java.util.*;

public class IntegralCopyForTeacher{
	
	/*This is the region of code you should edit
	 *  If you wish to view implementation you may look below
	 *  but otherwise remain within the main method and 
	 *  follow the instructions
	 */ 
	
	public static void main(String[] args) {
		integral(0, 3, true, x -> {
			return 3 * Math.pow(x, 3) + 5 * Math.pow(x, Math.E);
		});
	
		threeDimensionalVolumeDiskX(0, Math.log(4), x -> {
			return Math.pow(Math.pow(Math.E, -x), 2);
		});
	}
	
	
	
	private static DecimalFormat df = new DecimalFormat("###.###");
	private static final double INCREMENT = 9E-7;

	
	public static double integral(double a, double b, boolean standAlone, FunctionInterface function) {
		double area = 0;
		double modifer = 1;
		if (a > b) {
			double tempA = a;
			a = b;
			b = tempA;
			modifer = -1;
		}
		for (double i = a + INCREMENT; i < b; i += INCREMENT) {
			double dFromA = i - a;
			area += (INCREMENT / 2) * (function.f(a + dFromA) + function.f(a + dFromA - INCREMENT));
		}
		if(standAlone) {
			System.out.println("The area under the curve from: " + a + " to " + b + " is:   "+ (Math.round(area * 1000.0)/ 1000.0) * modifer);
		}
		return (Math.round(area * 1000.0)/ 1000.0) * modifer;
	}
	
	//used for x axis rotating
	public static String threeDimensionalVolumeDiskX(double a, double b, FunctionInterface function) {
		System.out.println(df.format((Math.PI * integral(a, b, false, function) * 1000.0) / 1000.0));
		return df.format((Math.PI * integral(a, b, false, function) * 1000.0) / 1000.0);
	}
	
	public static String threeDimensionalVolumeWasherX(double a, double b, FunctionInterface function1, FunctionInterface function2) {
		System.out.println(df.format((Math.PI * (integral(a, b, false, function1) - integral(a, b, false, function2)))));
		return df.format((Math.PI * (integral(a, b, false, function1) - integral(a, b, false, function2))));
	}
	
	
}
