package com.elex.common.component.rpc.ice;

import Ice.ObjectFactory;

/**
 * ice对象适配工厂
 * 
 * @author mausmars
 *
 */
public class IceFactoryAdapter implements ObjectFactory {
	@Override
	public Ice.Object create(String type) {
		// if (type.equals(ServiceAddress.ice_staticId())) {
		// return new FunctionAddressIce();
		// } else if (type.equals(FunctionId.ice_staticId())) {
		// return new FunctionIdIce();
		// } else if (type.equals(FunctionMenu.ice_staticId())) {
		// return new FunctionMenuIce();
		// } else if (type.equals(ServiceInfo.ice_staticId())) {
		// return new ServiceInfoIce();
		// }
		return null;
	}

	@Override
	public void destroy() {
	}

}
