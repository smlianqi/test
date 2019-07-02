package com.elex.common.component.function.info;

import java.io.IOException;

import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 功能地址
 * 
 * @author mausmars
 *
 */
public class ServiceAddress implements IWriteReadable {
	protected static final ILogger logger = XLogUtil.logger();

	/**
	 * host或域名
	 */
	protected String host = ""; //
	/**
	 * 端口
	 */
	protected int port;
	/**
	 * 网络协议类型
	 */
	protected NetProtocolType netProtocolType;

	public ServiceAddress() {
	}

	public ServiceAddress(String host, int port, NetProtocolType netProtocolType) {
		this.host = host;
		this.port = port;
		this.netProtocolType = netProtocolType;
	}

	public ServiceAddress(byte[] bytes) throws IOException {
		initField(bytes);
	}

	public void write(IDataOutput out) throws IOException {
		out.writeUTF(host);
		out.writeInt(port);
		out.writeUTF(netProtocolType.name());
	}

	public void read(IDataInput in) throws IOException {
		host = in.readUTF();
		port = in.readInt();
		netProtocolType = NetProtocolType.valueOf(in.readUTF());
	}

	public void initField(byte[] bytes) throws IOException {
		ByteArrayInput byteArrayInput = new ByteArrayInput(bytes);
		this.read(byteArrayInput);
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutput out = new ByteArrayOutput();
		this.write(out);
		return out.toByteArray();
	}

	// ----------------------
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public NetProtocolType getNetProtocolType() {
		return netProtocolType;
	}

	public void setNetProtocolType(NetProtocolType netProtocolType) {
		this.netProtocolType = netProtocolType;
	}

	@Override
	public String toString() {
		return "ServiceAddress [host=" + host + ", port=" + port + ", netProtocolType=" + netProtocolType + "]";
	}
}
