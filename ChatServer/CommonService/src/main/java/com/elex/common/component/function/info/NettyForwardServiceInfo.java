package com.elex.common.component.function.info;

import java.io.IOException;

import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionPool;
import com.elex.common.net.session.SessionPool;

public class NettyForwardServiceInfo implements IServiceInfo {
	// TODO 网络连接引用
	private int connectCount; // 链接数量
	private String token;// 验证码
	private ISessionPool serverSessionPool;// serverSession池用于推送
	private ISessionPool clientSessionPool;// clientSession池用于主动发送

	public NettyForwardServiceInfo(IDataInput in) throws IOException {
		this.read(in);
		this.serverSessionPool = new SessionPool(connectCount);
	}

	public NettyForwardServiceInfo(int connectCount, String token) {
		this.connectCount = connectCount;
		this.token = token;
		this.serverSessionPool = new SessionPool(connectCount);
	}

	@Override
	public void release() {
		if (clientSessionPool != null) {
			clientSessionPool.release();
		}
	}

	public void bindingSever(int index, ISession session) {
		this.serverSessionPool.bindingServer(index, session);
	}

	@Override
	public IServiceInfo cloneObj() {
		return new NettyForwardServiceInfo(connectCount, token);
	}

	@Override
	public FServiceType getFSType() {
		return FServiceType.NettyForward;
	}

	@Override
	public <T> T getService() {
		if (clientSessionPool != null) {
			return (T) clientSessionPool;
		}
		return (T) serverSessionPool;
	}

	@Override
	public void write(IDataOutput out) throws IOException {
		out.writeInt(connectCount);
		out.writeUTF(token);
	}

	@Override
	public void read(IDataInput in) throws IOException {
		this.connectCount = in.readInt();
		this.token = in.readUTF();
	}

	public void initField(byte[] bytes) throws IOException {
		ByteArrayInput byteArrayInput = new ByteArrayInput(bytes);
		this.read(byteArrayInput);
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutput out = new ByteArrayOutput();
		write(out);
		return out.toByteArray();
	}

	public int getConnectCount() {
		return connectCount;
	}

	public void setConnectCount(int connectCount) {
		this.connectCount = connectCount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setClientSessionPool(ISessionPool clientSessionPool) {
		this.clientSessionPool = clientSessionPool;
	}

	@Override
	public String toString() {
		return "NettyForwardServiceInfo [connectCount=" + connectCount + ", token=" + token + ", serverSessionPool="
				+ serverSessionPool + ", clientSessionPool=" + clientSessionPool + "]";
	}
}
