package model;

public class Minus extends BinaryExpression {
	public Minus(Expression l , Expression r) {
		super.left = l;
		super.right = r;
	}
	@Override
	public double calculate() {
		return super.left.calculate() - super.right.calculate();
	}

}
