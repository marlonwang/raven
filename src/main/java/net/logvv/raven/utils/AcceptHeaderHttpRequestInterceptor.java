package net.logvv.raven.utils;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class AcceptHeaderHttpRequestInterceptor implements ClientHttpRequestInterceptor
{

	private final MediaType mediaType;

	public AcceptHeaderHttpRequestInterceptor(MediaType mediaType)
	{
		super();
		this.mediaType = mediaType;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException
	{
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
		requestWrapper.getHeaders().setAccept(Collections.singletonList(mediaType));

		return execution.execute(requestWrapper, body);

	}

}
