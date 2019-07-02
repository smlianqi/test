package com.elex.common.net.service.httpclient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public abstract class ARequestCallback implements FutureCallback<String> {
	protected static final ILogger logger = XLogUtil.logger();

	/** 是否为异步，默认异步 */
	private boolean isASync = true;
	private Future<HttpResponse> future;

	public ARequestCallback() {
	}

	public ARequestCallback(boolean isASync) {
		this.isASync = isASync;
	}

	public Future<String> getFuture() {
		if (future == null) {
			return null;
		}
		Future<String> f = new Future<String>() {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				return future.cancel(mayInterruptIfRunning);
			}

			@Override
			public boolean isCancelled() {
				return future.isCancelled();
			}

			@Override
			public boolean isDone() {
				return future.isDone();
			}

			@Override
			public String get() throws InterruptedException, ExecutionException {
				if (isASync) {
					return null;
				}
				HttpResponse response = future.get();
				String httpStr = getEntityContent(response);
				return httpStr;
			}

			@Override
			public String get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				if (isASync) {
					return null;
				}
				HttpResponse response = future.get(timeout, unit);
				String httpStr = getEntityContent(response);
				return httpStr;
			}
		};
		return f;
	}

	public boolean isASync() {
		return isASync;
	}

	public void setFuture(Future<HttpResponse> future) {
		this.future = future;
	}

	static String getEntityContent(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		String httpStr = null;
		try {
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				// Consume response content
				EntityUtils.consume(entity);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return httpStr;
	}
}
