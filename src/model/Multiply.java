package model;

public class Multiply extends BinaryExpression {
	public Multiply(Expression l , Expression r) {
		super.left = l;
		super.right = r;
	}
	@Override
	public double calculate() {
		return super.left.calculate()*super.right.calculate();
	}

}
