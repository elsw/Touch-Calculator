package uk.co.withersnet.formulacalculator.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Number extends Node {
	
	private String number = "";
	private int cursorPosition = 0;//position of cursor within number, 0 = before first digit 
	
	public Number(){
		
	}
	public Number(String number){
		addDigit(number);
	}
	public Number(double number){
		addDigit(String.valueOf(number));
	}
	public Number(Number n){
		this.number = n.number;
		this.cursorPosition = n.cursorPosition;
	}
	public boolean cursorInMiddle(){
		return(!(cursorAtStart() || cursorAtEnd()));
	}
	@Override
	public boolean cursorAtEnd(){
		return (cursorPosition == number.length());
	}
	@Override
	public boolean cursorAtStart(){
		return (cursorPosition == 0);
	}
	@Override
	public boolean isEmpty(){
		if(number.length() == 0){
			return true;
		}else{
			return false;
		}		
	}
	/* splits the number around the cursor
	 * 
	 * numbers before cursor are kept
	 * @returns new Number with digits after cursor
	 */
	public Number splitNumber(){
		Number n = new Number();
		n.appendDigits(number.substring(cursorPosition, number.length()));
		number = number.substring(0, cursorPosition);
		return n;
	}
	
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawText(number, bounds.left, bounds.bottom, paint);
		super.draw(canvas, paint);
	}
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		paint.getTextBounds(number, 0, number.length(), bounds);
		bounds.set(x, y - bounds.height() , x + bounds.width(), y);
	}
	@Override
	public void drawCusor(Canvas canvas,Paint paint) {
		Rect rect = new Rect();
		paint.getTextBounds(number, 0, cursorPosition, rect);
		if(cursorPosition == 0){
			rect.set(0, 0, 0, 0);
		}
		paint.setColor(GlobalConstants.cursorColor);
		canvas.drawText("|", this.bounds.left + rect.width(),this.bounds.bottom, paint);
		paint.setColor(Color.BLACK);
	}
	@Override
	public Number execute(){
		return this;
	}
	public double valueOf(){
		String n = number.substring(number.length() - 1, number.length());
		if(number.substring(number.length() - 1, number.length()).equals("\u03C0")){
			if(number.length() > 1){
				return Double.valueOf(number.substring(0, number.length() - 1)) * Math.PI;
			}else{
				return Math.PI;
			}
		}else{
			return Double.valueOf(number);
		}
	}
	@Override
	public void addDigit(String str){
		number = new StringBuilder(number).insert(cursorPosition, str).toString();
		cursorPosition+= str.length();
	}
	/*
	 * Check if cursorAtStart() before calling this
	 */
	public void deleteDigit(){
		number = new StringBuilder(number).deleteCharAt(cursorPosition - 1).toString();
		cursorPosition--;
	}
	
	/*
	 * puts digits at the end,Does not move cursor		
	 */
	public void appendDigits(String str){
		number += str;
	}
	public void insertDigits(String str){
		number = new StringBuilder(number).insert(cursorPosition, str).toString();
		cursorPosition += str.length();
	}
	@Override
	public void putCursorAtEnd() {
		cursorPosition = number.length();
	}
	@Override
	public void putCursorAtStart() {
		cursorPosition = 0;
	}
	
	@Override
	public String toString() {
		if(number.length() == 0){
			return "EMPTY";
		}
		return number;
	}
	@Override
	public void invertNumber(){
		number = Float.toString(Float.valueOf(number) * -1);
	}
	@Override
	public Node getClone() {
		return new Number(this);
	}
	@Override
	public void setCursor(int x, int y) {
			//assumes characters are linearly spaced
			cursorPosition = (int)(((float)(x - bounds.left))/((float)(bounds.right - bounds.left)) * (number.length() + 1));
			if(cursorPosition < 0){
				cursorPosition = 0;
			}else if(cursorPosition > number.length()){
				cursorPosition = number.length();
			}
	}
	@Override
	public void moveCursorLeft() {
		cursorPosition--;
	}
	@Override
	public void moveCursorRight() {
		cursorPosition++;
	}
}
