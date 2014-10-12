package uk.co.withersnet.formulacalculator.brackets;

import java.text.DecimalFormat;

import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sine extends OpenBracket {
	
	
	@Override
	public Number modify(Number n) {
		//rounding probems occur using sin
		DecimalFormat df = new DecimalFormat("#." + GlobalConstants.maxPrecision);
		if(GlobalConstants.deg){
			return new Number(df.format(Math.sin(Math.toRadians(n.valueOf()))));
		}else{
			return new Number(df.format(Math.sin(n.valueOf())));
		}
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		paint.getTextBounds("sin(", 0, 3, bounds);
		bounds.set(x, y, x + bounds.width() + GlobalConstants.spacing, y);
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawText("sin", bounds.left, bounds.bottom, paint);
		Rect r = new Rect();
		paint.getTextBounds("sin", 0, 3, r);
		
		float oldFontSize = paint.getTextSize();
		
		paint.setTextSize(bounds.height() * 1.3f);
		paint.setTextScaleX(((float)GlobalConstants.fontSize) / ((float)bounds.height()));
		
		canvas.drawText("(", bounds.left + r.width(), bounds.bottom - (bounds.height() * 0.1f), paint);
		
		paint.setTextSize(oldFontSize);
		paint.setTextScaleX(1);
	}
	@Override
	public String toString() {
		return "sin(";
	}
	
	@Override
	public Node getClone() {
		return new Sine();
	}
}
