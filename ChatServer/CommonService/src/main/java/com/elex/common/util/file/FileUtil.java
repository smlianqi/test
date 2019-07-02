package com.elex.common.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.elex.common.util.PathNameBean;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class FileUtil {
	protected static final ILogger logger = XLogUtil.logger();

	public static String separator = File.separator;

	/**
	 * 创建 Properties
	 * 
	 * @param file
	 * @return
	 */
	public static Properties createProperties(File file) {
		InputStream in = null;
		Properties p = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			p = new Properties();
			p.load(in);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return p;
	}

	/**
	 * 将string 写入到文件中
	 */
	public static void write2File(String fileName, String content, String path) {
		if (fileName.contains(".")) {
			fileName = fileName.substring(0, fileName.indexOf("."));
		}
		String filePath = path + fileName + ".json";
		File file = new File(filePath);
		try {
			org.apache.commons.io.FileUtils.writeStringToFile(file, content, "utf-8");
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 读取文件内容
	 */
	public static String readFileContent(File file, boolean isNewline) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			StringBuilder stringBuilder = new StringBuilder();
			for (String str = in.readLine(); str != null;) {
				stringBuilder.append(str);
				if (isNewline) {
					stringBuilder.append("\n");
				}
				str = in.readLine();
			}
			return stringBuilder.toString();
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 读取文件内容
	 */
	public static List<String> readFileLineContent(File file) {
		List<String> strs = new LinkedList<String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			for (String str = in.readLine(); str != null;) {
				strs.add(str);
				str = in.readLine();
			}
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return strs;
	}

	/**
	 * 读取路径下后缀为endsWith文件内容
	 */
	public static List<String> readFileContentFromDirectory(String path, String endsWith) {
		List<String> contents = new LinkedList<String>();
		File dirFile = new File(path);

		if (!dirFile.exists()) {
			return contents;
		}

		if (!dirFile.isDirectory()) {
			String content = readFileContent(dirFile, false);
			if (contents != null && !contents.equals("")) {
				contents.add(content);
			}
			return contents;
		}

		File[] files = dirFile.listFiles();
		for (File file : files) {
			if (!file.isFile() || !file.getName().endsWith(endsWith)) {
				continue;
			}
			String sqlStr = readFileContent(file, false);
			if (sqlStr == null || sqlStr.equals("")) {
				continue;
			}
			contents.add(sqlStr);
		}
		return contents;
	}

	/**
	 * 获得文件夹下所有文件的名字
	 */
	public static List<String> getAllFileName(String workPath) {
		List<String> names = new ArrayList<String>();
		File dir = new File(workPath);
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			names.add(file[i].getName());
		}
		return names;

	}

	/**
	 * 获得文件夹下所有制定后缀名文件的名字(不包括后缀名)
	 */
	public static List<String> getAllFileName(String workPath, String extensionName) {
		List<String> names = new ArrayList<String>();
		File dir = new File(workPath);
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].getName().contains(extensionName)) {
				String name = file[i].getName().replace(extensionName, "");
				names.add(name);
			}
		}
		return names;
	}

	/**
	 * 获得文件夹下所有制定后缀名文件的路径和名字
	 */
	public static List<String> getAllFilePath(String workPath, String extensionName) {
		List<String> paths = new ArrayList<String>();
		File dir = new File(workPath);
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].getName().contains(extensionName)) {
				String path = file[i].getPath();
				paths.add(path);
			}
		}
		return paths;
	}

	/**
	 * 递归获得文件夹下所有 指定后缀名的 指定文件名带有特定字符串的 文件的路径和名字
	 */
	public static List<String> getRecursionAllFilePath(String workPath, String extensionName, String middleStr,
			List<String> paths) {
		File dir = new File(workPath);
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {
				getRecursionAllFilePath(file[i].getPath(), extensionName, middleStr, paths);
			} else {
				if (file[i].getName().contains(extensionName) && file[i].getName().contains(middleStr)) {
					String path = file[i].getPath();
					paths.add(path);
				}
			}
		}
		return paths;
	}

	/**
	 * 递归获得文件夹下所有制定后缀名文件的名字(不包括后缀名)
	 */
	public static List<PathNameBean> getRecursionAllFileName(String workPath, String extensionName,
			List<PathNameBean> names) {
		File dir = new File(workPath);
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {
				getRecursionAllFileName(file[i].getPath(), extensionName, names);
			} else {
				if (file[i].getName().contains(extensionName)) {
					PathNameBean bean = new PathNameBean();
					String name = file[i].getName().replace(extensionName, "");
					String path = file[i].getParent();
					bean.setName(name);
					bean.setPath(path);
					names.add(bean);
				}
			}
		}
		return names;
	}

	/**
	 * 获得文件夹下所有指定后缀名文件的名字(不包括后缀名),然后将指定子文件夹中的指定后缀名文件的名字也加入进来
	 */
	public static List<PathNameBean> getRecursionAllFileName(String workPath, String extensionName,
			List<PathNameBean> names, String folderName) {
		File dir = new File(workPath);
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory() && file[i].getName().equals(folderName)) {
				getRecursionAllFileName(file[i].getPath(), extensionName, names);
			} else {
				if (file[i].getName().contains(extensionName)) {
					PathNameBean bean = new PathNameBean();
					String name = file[i].getName().replace(extensionName, "");
					String path = file[i].getParent();
					bean.setName(name);
					bean.setPath(path);
					names.add(bean);
				}
			}
		}
		return names;
	}

	public static void main(String[] args) {
		List<PathNameBean> names = new ArrayList<PathNameBean>();
		getRecursionAllFileName("E:\\work@cwa2\\cwa_tool_resource\\wow\\newmessage", ".proto", names, "battle");
		for (PathNameBean pathNameBean : names) {
			System.out.println(pathNameBean.getName());

			System.out.println(pathNameBean.getPath());
		}

	}

	/**
	 * 不递归，只删除当前目录下所有指定后缀名名的文件
	 */
	public static void deleteAll(File file, String extensionName) {
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (!fileList[i].getName().contains(extensionName)) {
				fileList[i].delete();
			}
		}
	}

	/**
	 * 递归删除指定文件夹下的所有文件（不删除任何文件夹）
	 */
	public static void deleteAll(File file) {
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteAll(f);// 递归删除每一个文件
			}
		}
	}

	/**
	 * 获得文件名（不带后缀）
	 */
	public static String getSubFileName(String s, String extensionName) {
		if (s.contains(extensionName)) {
			s = s.replace(extensionName, "");
		}
		return s;
	}

	/**
	 * 创建文件夹
	 */
	public static void createDirectory(String filePaht) {
		File path = new File(filePaht);
		if (!path.exists()) {
			path.mkdirs();
		}
	}

	/**
	 * 删除文件夹的所有文件及其文件夹（不包括本文件夹）
	 */
	public static boolean removeAllFileFromDirectory(String filePaht) {
		File path = new File(filePaht);
		if (!path.exists()) {
			return false;
		}

		if (!path.isDirectory()) {
			// 如果不是路径就删除文件
			path.delete();
			return false;
		}

		File[] files = path.listFiles();
		for (File f : files) {
			removeFile(f);
		}
		return true;
	}

	/**
	 * 递归删除文件或文件夹，及其下所有文件夹文件（包括本文件夹）
	 */
	private static void removeFile(File file) {
		if (!file.isDirectory()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (File f : files) {
				removeFile(f);
			}
			file.delete();
		}
	}

	public static void copyDectionary(File srcDir, File destDir) {
		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getCurrentParentPath() {
		File directory = new File("..");// 设定为当前文件夹
		String workspacePath = null;
		try {
			workspacePath = directory.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workspacePath;
	}
}
