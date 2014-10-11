package uk.co.withersnet.formulacalculator.modifiers;

import uk.co.withersnet.formulacalculator.util.DualModifier;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Fraction extends DualModifier {
	//modifier is top, super nodes array is bottom
	
	int lineXStart = 0;
	int lineYStart = 0;
	
	final int gap = 8;
	final int minWidth = 20;
	
	public Fraction(){
		super();
	}
	public Fraction(Fraction f){
		super(f);
		this.lineXStart = f.lineXStart;
		this.lineYStart = f.lineYStart;
		
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawLine(lineXStart, lineYStart, lineXStart + this.getBounds().width(), lineYStart, paint);
		super.draw(canvas, paint);
		modifier.draw(canvas, paint);
	}

	@Override
	public void updateBounds(int x, int y, Paint paint) {
		lineXStart = x;
		lineYStart = y;
		modifier.updateBoundsFromBottom(x, y - gap, paint);
		super.updateBoundsFromBottom(0 ,0 , paint);
		
		if(super.getBounds().width() > modifier.getBounds().width()){
			modifier.updateBoundsFromBottom(x + ((super.getBounds().width() - modifier.getBounds().width())/2), y - gap, paint);
			super.updateBoundsFromBottom(x, y + gap + super.getBounds().height(), paint);

			int right;
			if(super.getBounds().width() > minWidth){
				right = x + super.getBounds().width();
			}else{
				right = x + minWidth;
			}
			
			this.bounds.set(x, modifier.getBounds().top, right, super.getBounds().bottom);
		}else{
			super.updateBoundsFromBottom(x + ((modifier.getBounds().width() - super.getBounds().width())/2), y + gap + super.getBounds().height(), paint);
			
			int right;
			if(modifier.getBounds().width() > minWidth){
				right = x + modifier.getBounds().width();
			}else{
				right = x + minWidth;
			}
			
			this.bounds.set(x, modifier.getBounds().top, right, super.getBounds().bottom);
		}
	}

	@Override
	public void updateBoundsFromBottom(int x, int y, Paint paint) {
		super.updateBoundsFromBottom(x ,y , paint);
		lineXStart = x;
		lineYStart = y - gap - super.getBounds().height();
		modifier.updateBoundsFromBottom(x, lineYStart - gap, paint);
		
		
		if(super.getBounds().width() > modifier.getBounds().width()){
			modifier.updateBoundsFromBottom(x + ((super.getBounds().width() - modifier.getBounds().width())/2), lineYStart - gap, paint);
	
			int right;
			if(super.getBounds().width() > minWidth){
				right = x + super.getBounds().width();
			}else{
				right = x + minWidth;
			}
			
			this.bounds.set(x, modifier.getBounds().top, right, super.getBounds().bottom);
		}else{
			super.updateBoundsFromBottom(x + ((modifier.getBounds().width() - super.getBounds().width())/2), y, paint);
			
			int right;
			if(modifier.getBounds().width() > minWidth){
				right = x + modifier.getBounds().width();
			}else{
				right = x + minWidth;
			}
			
			this.bounds.set(x, modifier.getBounds().top, right, super.getBounds().bottom);
		}
	}
	
	
	@Override
	public Number execute() throws Exception {
		return new Number(modifier.execute().valueOf() / super.execute().valueOf());
	}
	@Override
	public void setCursor(int x, int y) {
		if(y > lineYStart){
			super.superSetCursor(x, y);
			modActive = false;
		}else{ 
			modifier.setCursor(x, y);
			modActive = true;
		}
	}
	@Override
	public String toString() {
		return "frac("+modifier.toString()+"/"+super.toString()+")";
	}
	@Override
	public Node getClone() {
		return new Fraction(this);
	}
	@Override
	public void deleteDigit() {
		super.deleteDigit();
	}
	@Override
	public void moveCursorUp() {
		if(modActive){
			modifier.moveCursorUp();
		}else{
			if(super.cursorAtTop()){
				modActive = true;
			}else{
				super.moveCursorUp();
			}
		}
	}
	@Override
	public void moveCursorDown() {
		if(!modActive){
			super.moveCursorDown();
		}else{
			if(modifier.cursorAtBottom()){
				modActive = false;
			}else{
				
			}
		}
	}
	
	@Override
	public void moveCursorLeft() {
		if(modActive){
			modifier.moveCursorLeft();
		}else{
			super.moveCursorLeft();
		}
	}

	@Override
	public boolean cursorAtEnd() {
		if(modActive){
			return modifier.cursorAtEnd();
		}else{
			return super.superCursorAtEnd();
		}
	}
	@Override
	public boolean cursorAtStart() {
		if(modActive){
			return modifier.cursorAtStart();
		}else{
			return super.superCursorAtStart();
		}
	}
	public boolean cursorAtTop(){
		if(modActive){
			return modifier.cursorAtTop();
		}else{
			return false;
		}
	}
	public boolean cursorAtBottom(){
		if(!modActive){
			return super.cursorAtTop();
		}else{
			return false;
		}
	}
	@Override
	public void putCursorAtTop() {
		modActive = true;
		modifier.putCursorAtStart();
	}
	@Override
	public void putCursorAtBottom() {
		modActive = false;
		super.putSuperCursorAtStart();
	}
	@Override
	public void putCursorAtEnd() {
		if(modActive){
			modifier.putCursorAtEnd();
		}else{
			super.putSuperCursorAtEnd();
		}
	}
	@Override
	public void putCursorAtStart() {
		if(modActive){
			modifier.putCursorAtStart();
		}else{
			super.putSuperCursorAtStart();
		}
	}
}
