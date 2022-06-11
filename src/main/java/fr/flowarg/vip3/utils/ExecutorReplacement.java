/**
 *
 * MIT License
 *
 * Copyright 2022 Steven Cao
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.flowarg.vip3.utils;

import fr.flowarg.vip3.VIP3;
import net.minecraft.Util;
import net.minecraft.util.Mth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

// https://github.com/UltimateBoomer/mc-smoothboot/blob/1.18/src/main/java/io/github/ultimateboomer/smoothboot/mixin/UtilMixin.java
@CalledAtRuntime
public final class ExecutorReplacement
{
	@CalledAtRuntime
	public static ExecutorService makeExecutor(String name) {
		return new ForkJoinPool(Mth.clamp(name.equals("Bootstrap") ? 1 : Mth.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, 255), 1, 0x7fff), (forkJoinPool) -> {
			String workerName = "Worker-" + name + "-" + Util.WORKER_COUNT.getAndIncrement();
			VIP3.LOGGER.debug("Initialized " + workerName);

			ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, VIP3.LOGGER);
			forkJoinWorkerThread.setPriority(1);
			forkJoinWorkerThread.setName(workerName);
			return forkJoinWorkerThread;
		}, Util::onThreadException, true);
	}

	@CalledAtRuntime
	public static ExecutorService makeIoExecutor() {
		return Executors.newCachedThreadPool((runnable) -> {
			String workerName = "IO-Worker-" + Util.WORKER_COUNT.getAndIncrement();
			VIP3.LOGGER.debug("Initialized " + workerName);
			
			Thread thread = new Thread(runnable);
			thread.setName(workerName);
			thread.setDaemon(true);
			thread.setPriority(1);
			thread.setUncaughtExceptionHandler(Util::onThreadException);
			return thread;
		});
	}
}
