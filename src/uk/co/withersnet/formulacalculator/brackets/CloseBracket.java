package uk.co.withersnet.formulacalculator.brackets;

import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Node;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CloseBracket extends Bracket {
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		paint.getTextBounds(")", 0, 1, bounds);
		bounds.set(x, y, x + bounds.width(), y);
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		float oldFontSize = paint.getTextSize();
		paint.setTextSize(bounds.height() * 1.3f);
		
		paint.setTextScaleX(((float)GlobalConstants.fontSize) / ((float)bounds.height()));
		canvas.drawText(")", bounds.left, bounds.bottom - (bounds.height() * 0.1f), paint);
		
		paint.setTextSize(oldFontSize);
		paint.setTextScaleX(1);
	}
	@Override
	public String toString() {
		return ")";
	}
	@Override
	public Node getClone() {
		// TODO Auto-generated method stub
		return new CloseBracket();
	}
}
