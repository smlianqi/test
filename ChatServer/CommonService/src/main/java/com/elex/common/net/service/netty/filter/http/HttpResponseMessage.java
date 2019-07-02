package com.elex.common.net.service.netty.filter.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpResponseMessage {
	private ByteArrayOutputStream baos;
	private PrintWriter out;

	public HttpResponseMessage() {
		baos = new ByteArrayOutputStream();
		out = new PrintWriter(baos);
	}

	public byte[] toByteArray() {
		return baos.toByteArray();
	}

	public void writeUTF(String s) throws IOException {
		out.write(s);
	}

	public void flush() throws IOException {
		out.flush();
	}

	public void close() throws IOException {
		out.close();
	}
}
