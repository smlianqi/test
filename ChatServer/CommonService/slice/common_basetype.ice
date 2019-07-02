#define COMMON_BASETYPE

//这个定义了基本的集合类型，避免多次定义数据结构
module baseice{
module basetype{
		//----------------------------
		//基本类型Wrap包装，用于基本类型的Ice.Object的传递
		class ByteWrap{
			byte value;
		};
		class ShortWrap{
			short value;
		};
		class IntWrap{
			int value;
		};
		class LongWrap{
			long value;
		};
		class FloatWrap{
			float value;
		};
		class DoubleWrap{
			double value;
		};
		class StringWrap{
			string value;
		};
		//----------------------------
		//数组类型
		sequence<byte> ByteArray;
		sequence<short> ShortArray;
		sequence<int> IntArray;
		sequence<long> LongArray;
		sequence<float> FloatArray;
		sequence<double> DoubleArray;
		sequence<string> StringArray;
		
		//Object 数组 
		sequence<Object> ObjectArray;
		sequence<Object*> ObjectPrxArray;
		//-------------------------------------
		//LinkedList类型
		["java:type:java.util.LinkedList<Byte>"] sequence<byte> ByteLList;
		["java:type:java.util.LinkedList<Short>"] sequence<short> ShortLList;
		["java:type:java.util.LinkedList<Integer>"] sequence<int> IntLList;
		["java:type:java.util.LinkedList<Long>"] sequence<long> LongLList;
		["java:type:java.util.LinkedList<Float>"] sequence<float> FloatLList;
		["java:type:java.util.LinkedList<Double>"] sequence<double> DoubleLList;
		["java:type:java.util.LinkedList<String>"] sequence<string> StringLList;
		["java:type:java.util.LinkedList<Ice.Object>"] sequence<Object> ObjectLList;
		["java:type:java.util.LinkedList<Ice.ObjectPrx>"] sequence<Object*> ObjectPrxLList;
		//-------------------------------------
		//ArrayList类型
		["java:type:java.util.ArrayList<Byte>"] sequence<byte> ByteAList;
		["java:type:java.util.ArrayList<Short>"] sequence<short> ShortAList;
		["java:type:java.util.ArrayList<Integer>"] sequence<int> IntAList;
		["java:type:java.util.ArrayList<Long>"] sequence<long> LongAList;
		["java:type:java.util.ArrayList<Float>"] sequence<float> FloatAList;
		["java:type:java.util.ArrayList<Double>"] sequence<double> DoubleAList;
		["java:type:java.util.ArrayList<String>"] sequence<string> StringAList;
		["java:type:java.util.ArrayList<Ice.Object>"] sequence<Object> ObjectAList;
		["java:type:java.util.ArrayList<Ice.ObjectPrx>"] sequence<Object*> ObjectPrxAList;
		//-------------------------------------
		//HashMap类型
		dictionary<string, Object> StringObjectHMap;
		dictionary<int, Object> IntObjectHMap;
		dictionary<long, Object> LongObjectHMap;
		
		dictionary<string, Object*> StringObjectPrxHMap;
		dictionary<int, Object*> IntObjectPrxHMap;
		dictionary<long, Object*> LongObjectPrxHMap;
		//-------------------------------------
		//ConcurrentHashMap类型
		["java:type:java.util.concurrent.ConcurrentHashMap<String,Ice.Object>:java.util.Map<String, Ice.Object>"] dictionary<string, Object> StringObjectCMap;
		["java:type:java.util.concurrent.ConcurrentHashMap<Long,Ice.Object>:java.util.Map<Long, Ice.Object>"] dictionary<long, Object> LongObjectCMap;
		["java:type:java.util.concurrent.ConcurrentHashMap<Integer,Ice.Object>:java.util.Map<Integer, Ice.Object>"] dictionary<int, Object> IntObjectCMap;
		
		["java:type:java.util.concurrent.ConcurrentHashMap<String,Ice.ObjectPrx>:java.util.Map<String, Ice.ObjectPrx>"] dictionary<string, Object*> StringObjectPrxCMap;
		["java:type:java.util.concurrent.ConcurrentHashMap<Long,Ice.ObjectPrx>:java.util.Map<Long, Ice.ObjectPrx>"] dictionary<long, Object*> LongObjectPrxCMap;
		["java:type:java.util.concurrent.ConcurrentHashMap<Integer,Ice.ObjectPrx>:java.util.Map<Integer, Ice.ObjectPrx>"] dictionary<int, Object*> IntObjectPrxCMap;
		//-------------------------------------
};
};
