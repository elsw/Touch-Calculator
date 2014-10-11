package uk.co.withersnet.formulacalculator.util;

import android.graphics.Canvas;
import android.graphics.Paint;

abstract public class Operator extends Unselectable{
	
	protected Double number1;
	protected Double number2;
	
	public Operator(){
		
	}
	public void draw(Canvas canvas,Paint paint){
		super.draw(canvas, paint);
	}
	public abstract Number execute() throws Exception;	
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		paint.getTextBounds("0", 0, 1, bounds);
		bounds.set(x, y - bounds.height(), x + bounds.width(), y);
	}
	
	public void setNumber1(Number n){
		number1 = n.valueOf();
	}
	public void setNumber2(Number n){
		number2 = n.valueOf();
	}
	@Override
	public boolean isEmpty() {
		return false;
	}
}
