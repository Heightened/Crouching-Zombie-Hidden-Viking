package util;

import java.util.Random;

public class Rand
{
	public static final Random rand = new Random();
	
	public static int randInt(int min, int max) {
	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
