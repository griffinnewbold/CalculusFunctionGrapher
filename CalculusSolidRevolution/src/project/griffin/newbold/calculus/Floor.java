package project.griffin.newbold.calculus;

public class Floor extends Unary {
	
	public Floor(Quantity q) {
		super(q);
	}

	@Override
	public double getValue() {
		double val = realValue(q);
		return Math.floor(val);
	}
}