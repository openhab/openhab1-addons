/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.urtsi.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * The DedicatedThreadExecutor executes the given lambda expression in a dedicated thread.
 * You can use this class in order to ensure that some logic execution is not executed simultaneously.
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
public class DedicatedThreadExecutor {
	private final ExecutorService executorService = Executors
			.newSingleThreadExecutor();

	/**
	 * The method ensures that the given lamdaExp is executed in a dedicated thread.
	 * @param lambdaExp some business logic
	 * @return
	 */
	public <T> Future<T> execute(final Function1<Object, T> lambdaExp) {
		return executorService.submit(new Callable<T>() {

			@Override
			public T call() throws Exception {
				return lambdaExp.apply(null);
			}
		});
	}

}
