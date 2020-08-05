package model;

public class Plus extends BinaryExpression {
	public Plus(Expression l , Expression r) {
		super.left = l;
		super.right = r;
	}
	@Override
	public double calculate() {
		return super.left.calculate() + super.right.calculate();
	}

}
