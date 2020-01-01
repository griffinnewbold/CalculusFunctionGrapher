package project.griffin.newbold.calculus;

import java.text.DecimalFormat;
import java.util.*;

public class Integral extends Unary{
	private static DecimalFormat df = new DecimalFormat("###.###");
	private static final double INCREMENT = 9E-7;

	public Integral(Quantity p) {
		super(p);
	}
	public static double integral(double a, double b, FunctionInterface function) {
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
		return (Math.round(area * 1000.0)/ 1000.0) * modifer;
	}
	
	//used for x axis rotating
	public static String threeDimensionalVolumeDiskX(double a, double b, FunctionInterface function) {
		
		return df.format((Math.PI * integral(a, b, function) * 1000.0) / 1000.0);
	}
	
	public static String threeDimensionalVolumeWasherX(double a, double b, FunctionInterface function1, FunctionInterface function2) {
		return df.format((Math.PI * (integral(a, b, function1) - integral(a, b, function2))));
	}
	
	public static void main(String[] args) {
		System.out.println(integral(0, 3, x -> {
			return 3 * Math.pow(x, 3) + 5 * Math.pow(x, Math.E);
		}));
	
		System.out.println(threeDimensionalVolumeDiskX(0, Math.log(4), x -> {
			return Math.pow(Math.pow(Math.E, -x), 2);
		}));
		double d = 0_1234.5_6;
		System.out.print(d);
		
	}
	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
