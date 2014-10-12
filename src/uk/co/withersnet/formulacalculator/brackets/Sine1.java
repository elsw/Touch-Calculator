package uk.co.withersnet.formulacalculator.brackets;

import java.security.InvalidParameterException;

import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sine1 extends OpenBracket {
	
	
	@Override
	public Number modify(Number n) {
		double number = n.valueOf();
		if(number <= 1 && number >= -1){
			if(GlobalConstants.deg){
				return new Number(Math.toDegrees(Math.asin(n.valueOf())));
			}else{
				return new Number(Math.asin(n.valueOf()));
			}
		}else{
			throw new InvalidParameterException("Invalid Sine argument");
		}
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		paint.getTextBounds("sina(", 0, 5, bounds);
		bounds.set(x, y, x + bounds.width() + GlobalConstants.spacing, y);
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawText("sin", bounds.left, bounds.bottom, paint);
		Rect r = new Rect();
		paint.getTextBounds("sin", 0, 3, r);
		
		float oldFontSize = paint.getTextSize();
		
		paint.setTextSize(oldFontSize / 2);
		canvas.drawText("-1", bounds.left + r.width(), bounds.bottom - (r.height()* 0.7f), paint);
		
		paint.setTextSize(oldFontSize);
		paint.getTextBounds("sina", 0, 4, r);
		
		paint.setTextSize(bounds.height() * 1.3f);
		paint.setTextScaleX(((float)GlobalConstants.fontSize) / ((float)bounds.height()));
		
		canvas.drawText("(", bounds.left + r.width(), bounds.bottom - (bounds.height() * 0.1f), paint);
		
		paint.setTextSize(oldFontSize);
		paint.setTextScaleX(1);
	}
	@Override
	public String toString() {
		return "asin(";
	}
	
	@Override
	public Node getClone() {
		return new Sine1();
	}
}
