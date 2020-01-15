package com.elex.common.util.netaddress;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 网络地址
 * 
 * @author mausmars
 *
 */
public class NetAddressInfo {
	protected static final ILogger logger = XLogUtil.logger();

	private String ip; // ip
	private String mac;// mac

	private String name;
	private NetAddressTypeEnum netAddressType;

	public void init(NetworkInterface ni) {
		Enumeration<InetAddress> address = ni.getInetAddresses();
		name = ni.getName();
		if (logger.isInfoEnabled()) {
			logger.info("### " + name);
		}
		while (address.hasMoreElements()) {
			InetAddress inetAddress = address.nextElement();
			if (logger.isInfoEnabled()) {
				logger.info("" + inetAddress.getHostAddress());
			}
			if (inetAddress.getHostAddress().indexOf(":") != -1) {
				this.mac = inetAddress.getHostAddress();
				continue;
			}
			this.ip = inetAddress.getHostAddress();

			if (inetAddress.isLoopbackAddress()) {
				netAddressType = NetAddressTypeEnum.LoopbackAddress;
			} else if (!inetAddress.isSiteLocalAddress()) {
				// 外网
				netAddressType = NetAddressTypeEnum.OutsideAddress;
			} else {
				// 本机包含虚拟
				netAddressType = NetAddressTypeEnum.LoacalAddress;
			}
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public NetAddressTypeEnum getNetAddressType() {
		return netAddressType;
	}

	public void setNetAddressType(NetAddressTypeEnum netAddressType) {
		this.netAddressType = netAddressType;
	}

	@Override
	public String toString() {
		return "NetAddressInfo [name=" + name + ",ip=" + ip + ", mac=" + mac + ", netAddressType=" + netAddressType
				+ "]";
	}
}
