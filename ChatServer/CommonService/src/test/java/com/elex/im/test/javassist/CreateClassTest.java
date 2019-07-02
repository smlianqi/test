package com.elex.im.test.javassist;

import java.lang.reflect.Constructor;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * 动态创建class测试
 * 
 * @author mausmars
 *
 */
public class CreateClassTest {
	public static void main(String[] args) throws Exception {
		CreateClassTest test = new CreateClassTest();
		test.test();
	}

	private String interfaceName = "com.elex.im.test.javassist.IAnimal";

	public void test() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		// 创建Programmer类

		// 保存生成的字节码
		// cc.writeFile("d://temp");

		// 获取动态生成的class
		Class c1 = createHorse1(pool);
		// 获取构造器
		Constructor constructor1 = c1.getConstructor();
		// 通过构造器实例化
		IAnimal o = (IAnimal) constructor1.newInstance();
		o.walk();

		Class c2 = createHorse2(pool);
		Constructor constructor2 = c2.getConstructor();
		IAnimal o2 = (IAnimal) constructor2.newInstance();

		o = o2;
		o.walk();
	}

	private Class createHorse1(ClassPool pool) throws Exception {
		CtClass cc = pool.makeClass("Horse_1");

		CtClass interface1 = pool.get(interfaceName);
		cc.setInterfaces(new CtClass[] { interface1 });

		// 定义code方法
		CtMethod method = CtNewMethod.make("public void walk(){}", cc);
		// 插入方法代码
		method.insertBefore("System.out.println(\"Horse奔跑1\");");
		cc.addMethod(method);

		return cc.toClass();
	}

	private Class createHorse2(ClassPool pool) throws Exception {
		CtClass cc = pool.makeClass("Horse_2");

		CtClass interface1 = pool.get(interfaceName);
		cc.setInterfaces(new CtClass[] { interface1 });

		// 定义code方法
		CtMethod method = CtNewMethod.make("public void walk(){}", cc);
		// 插入方法代码
		method.insertBefore("System.out.println(\"Horse奔跑2\");");
		cc.addMethod(method);

		return cc.toClass();
	}
}
