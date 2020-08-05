package model;

public class Number implements Expression {
	private double num;
	public Number(double d) {
			num = d;
	}
	@Override
	public double calculate() {
		return num;
	}

}
