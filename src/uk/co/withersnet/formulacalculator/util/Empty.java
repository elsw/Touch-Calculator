package uk.co.withersnet.formulacalculator.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*these are removed when executed*/
public class Empty extends Node {
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		paint.getTextBounds("1", 0, 1, bounds);
		bounds.set(x, y - bounds.height(), x + 3, y);
	}
	@Override
	public void drawCusor(Canvas canvas, Paint paint) {
		paint.setColor(GlobalConstants.cursorColor);
		canvas.drawText("|", bounds.left - 1, bounds.bottom, paint);
		paint.setColor(Color.BLACK);
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		if(isDrawingBounds()){
			canvas.drawRect(bounds.left,bounds.top,bounds.right,bounds.bottom, paint);
		}
	}
	@Override
	public boolean cursorAtEnd() {
		return true;
	}
	@Override
	public boolean cursorAtStart() {
		return true;
	}
	@Override
	public Node getClone() {
		//no need to clone these
		return this;
	}
	@Override
	public String toString() {
		return "E";
	}
}
