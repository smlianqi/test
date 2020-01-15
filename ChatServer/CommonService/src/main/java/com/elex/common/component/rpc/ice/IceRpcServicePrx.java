package com.elex.common.component.rpc.ice;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.elex.common.component.clsloader.ServerClassLoader;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.rpc.IRpcFServicePrx;
import com.elex.common.component.rpc.IRpcServicePrx;
import com.elex.common.component.rpc.config.ScRpc;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

import Ice.ObjectPrx;
import Ice.ObjectPrxHelperBase;

/**
 * ice rpc 代理服务
 * 
 * @author mausmars
 *
 */
public class IceRpcServicePrx extends AbstractService<ScRpc> implements IRpcServicePrx {
	protected static final String Cast = "uncheckedCast";

	// 对象id:tcp -h ip -p port
	private static final String IceUrl_Template = "%s:%s -h %s -p %d";

	// 包名 接口名 PrxHelper，这里对应ice的路径
	private static final String PrxHelperClassName_Template = "%s.%sPrxHelper";

	private static final String IceConfig = "--Ice.Config=propertiesconfig/client_ice.properties";

	private volatile Ice.Communicator communicator;

	// TODO 这里可以传进来
	private final String LocalPrxClassName_Template = "com.elex.im.module.server%s.rpcservice.%sIcePrx";

	public IceRpcServicePrx(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	public void initService() throws Exception {
		if (communicator == null) {
			communicator = Ice.Util.initialize(new String[] { IceConfig });
		}
	}

	@Override
	public void startupService() throws Exception {
	}

	@Override
	public void shutdownService() throws Exception {
		communicator.destroy();
		if (logger.isInfoEnabled()) {
			logger.info("stop Ice Prx service over!");
		}
	}

	public IRpcFServicePrx createServicePrx(String objectId, String netProtocol, String host, int port,
			String packageName, String className, FunctionType functionType) {
		String url = String.format(IceUrl_Template, objectId, netProtocol, host, port);
		IRpcFServicePrx servicePrx = createServicePrx(packageName, className, functionType,
				communicator.stringToProxy(url));
		return servicePrx;
	}

	private IRpcFServicePrx createServicePrx(String packageName, String interfaceName, FunctionType ftype,
			Object... args) {
		String className = String.format(PrxHelperClassName_Template, packageName, interfaceName);
		try {
			Class<ObjectPrxHelperBase> caster = (Class<ObjectPrxHelperBase>) ServerClassLoader.getClass(className);
			if (caster == null) {
				return null;
			}
			Method method = caster.getMethod(Cast, Ice.ObjectPrx.class);// checkedCast
			Ice.ObjectPrx prx = (Ice.ObjectPrx) method.invoke(null, args);
			if (prx == null) {
				logger.error("ObjectPrx prx is null! className=" + className);
				return null;
			}
			// 反射初始化ice封装类
			String localPrxClassName = String.format(LocalPrxClassName_Template, ftype.name(),
					interfaceName.substring(1));
			Class<IRpcFServicePrx> rpcServicePrx = (Class<IRpcFServicePrx>) ServerClassLoader
					.getClass(localPrxClassName);
			Constructor<IRpcFServicePrx> con = rpcServicePrx.getConstructor(ObjectPrx.class);
			return con.newInstance(prx);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
		}
	}
}
