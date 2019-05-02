package org.stenerud.hse.tools;

import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;

/**
 * Analyzes exceptions and interprets what happened. <br>
 * Unfortunately, we can't catch and interpret them in a DAO because it's being wrapped in a call pointcut by Spring's
 * AOP transaction mechanism. If the DB goes down, it will be the transaction setup in the advice that throws the
 * exception, and not a single line of code in the DAO will get called before the exception gets thrown.
 * 
 * @author Karl Stenerud
 */
public class ExceptionAnalyzer
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Check if this exception resulted from loss of connectivity to the database.
	 * 
	 * @param exception the exception.
	 * @return true if database connectivity has been lost.
	 */
	public static boolean isDatabaseDisconnect(Throwable exception)
	{
		/*
		 * UncategorizedSQLException might be stretching things a bit, but postgresql throws out some pretty weird
		 * errors. Take it out if it behaves badly with your DB.
		 */
		return (exception instanceof TransactionSystemException)
				|| (exception instanceof CannotCreateTransactionException)
				|| (exception instanceof UncategorizedSQLException);
	}
}
