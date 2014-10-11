package uk.co.withersnet.formulacalculator.util;

import android.graphics.Color;

public final class GlobalConstants {
	public static int fontSize = 30;
	public static int exponentFontSize = 23;//the size reduction of the text when displaying exponents
	public static int errorFontSize = 17;
	public static int spacing = 6;
	public static int cursorColor = Color.argb(160, 255, 0, 0);
	public static float minBeforeExp = 999999;
	public static String maxPrecision = "###############";//15d.p used on decimal formatter construction, 
	//if this is higher than 16 is produces small errors using trig
	
	
	public static final float drawStartX = 0.1f;//percentage between 0 and 1 from top left of canvas
	public static final float drawStartY = 0.3f;
	public static final float drawAnsX   = 0.9f;//end of ans draw
	public static final float drawAnsY   = 0.85f;
	public static final float drawErrorX   = 0.4f;
	public static final float drawErrorY   = 0.95f;
	
	public static boolean deg = false;
	public static boolean rad = true;
	
}
