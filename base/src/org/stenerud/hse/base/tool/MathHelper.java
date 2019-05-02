package org.stenerud.hse.base.tool;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Helper class to perform some math functions.
 * 
 * @author Karl Stenerud
 */
public class MathHelper
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Compute the greatest common divisor of a set of positive values
	 * 
	 * @param values the values
	 * @return the gcd
	 */
	public static long gcd(TreeSet values)
	{
		// Get the smallest value
		long smallestValue = ((Long)values.iterator().next()).longValue();

		if ( smallestValue <= 1 )
		{
			return 1;
		}

		// Find GCD
		for ( long divisor = smallestValue; divisor > 1; divisor-- )
		{
			boolean divisible = true;
			for ( Iterator iter = values.iterator(); iter.hasNext(); )
			{
				long value = ((Long)iter.next()).longValue();
				divisible = value % divisor == 0;
				if ( !divisible )
				{
					break;
				}
			}
			if ( divisible )
			{
				return divisor;
			}
		}
		return 1;
	}
}
