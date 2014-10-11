package uk.co.withersnet.formulacalculator.brackets;

import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Tan1 extends OpenBracket {
	
	
	@Override
	public Number modify(Number n) {
		double number = n.valueOf();
		if(GlobalConstants.deg){
			return new Number(Math.toDegrees(Math.atan(n.valueOf())));
		}else{
			return new Number(Math.atan(n.valueOf()));
		}
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		paint.getTextBounds("tana(", 0, 5, bounds);
		bounds.set(x, y, x + bounds.width() + GlobalConstants.spacing, y);
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawText("tan", bounds.left, bounds.bottom, paint);
		Rect r = new Rect();
		paint.getTextBounds("tan", 0, 3, r);
		
		paint.setTextSize(GlobalConstants.errorFontSize);
		canvas.drawText("-1", bounds.left + r.width(), bounds.bottom - (r.height()* 0.7f), paint);
		
		paint.setTextSize(GlobalConstants.fontSize);
		paint.getTextBounds("tana", 0, 4, r);
		
		paint.setTextSize(bounds.height() * 1.3f);
		paint.setTextScaleX(((float)GlobalConstants.fontSize) / ((float)bounds.height()));
		
		canvas.drawText("(", bounds.left + r.width(), bounds.bottom - (bounds.height() * 0.1f), paint);
		
		paint.setTextSize(GlobalConstants.fontSize);
		paint.setTextScaleX(1);
	}
	@Override
	public String toString() {
		return "atan(";
	}
	
	@Override
	public Node getClone() {
		return new Tan1();
	}
}
