package uk.co.withersnet.formulacalculator.modifiers;

import uk.co.withersnet.formulacalculator.util.DualModifier;
import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;

public class  ModLogab extends DualModifier {
	
	public ModLogab(){
		super();
		modifier.setFontSize(GlobalConstants.exponentFontSize);
		moveActiveNumber = false;
	}
	public ModLogab(ModLogab mod){
		super(mod);
		modifier.setFontSize(GlobalConstants.exponentFontSize);
		moveActiveNumber = false;
	}
	@Override
	public Number execute() throws Exception {
		return new Number(Math.log(super.execute().valueOf()) / Math.log(modifier.execute().valueOf()));
		//ln(x)/ln(base)
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawText("log",bounds.left,modifier.getBounds().top,paint);
		modifier.draw(canvas, paint);
		super.draw(canvas, paint);
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		modifier.updateBounds(0, 0, paint);
		paint.getTextBounds("log",0,3,bounds);
		modifier.updateBoundsFromBottom(x + bounds.width(), y + modifier.getBounds().height(), paint);
		
		super.updateBoundsFromBottom(modifier.getBounds().right, y, paint);
		
		this.bounds.set(x, super.getBounds().top,super.getBounds().right , modifier.getBounds().bottom);
	}
	@Override
	public void updateBoundsFromBottom(int x, int y, Paint paint) {
		paint.getTextBounds("log",0,3,bounds);
		modifier.updateBoundsFromBottom(x + bounds.width(), y, paint);
		super.updateBoundsFromBottom(modifier.getBounds().right, modifier.getBounds().top, paint);
	}
	
	@Override
	public Node getClone() {
		return new ModLogab(this);
	}
	@Override
	public String toString() {
		return "Log(" + modifier.toString()+" , "+super.toString()+")";
	}
}
