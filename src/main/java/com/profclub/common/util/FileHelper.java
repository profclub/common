package com.profclub.common.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Utility methods for data reading/manipulation with Files.
 *
 */
public class FileHelper {

	/**
	 * Loads provided file content into list
	 * by reading/taking each line as one list element.
	 *
	 * @param file
	 * @param list
	 * @throws IOException
	 */
	public static void readFileToList(File file, List<String> list) throws IOException {
		InputStream in = new FileInputStream(file);
		String line;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		}
	}

	public static byte[] getBytes(String filePath) throws IOException {
		// ensure file exists
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException(String.format("File not found: %s", filePath));
		}

		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		for (int readNum; (readNum = fis.read(buf)) != -1;) {
			bos.write(buf, 0, readNum);
		}
		byte[] bytes = bos.toByteArray();
		bos.close();

		return bytes;
	}

	/**
	 * Gets/returns as string instance the content of provided file.
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileContentAsString(File file) throws IOException {
		InputStream in = new FileInputStream(file);
        StringBuilder builder = new StringBuilder();
		String line;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			while ((line = reader.readLine()) != null) {
                builder.append(line);
			}
		}
        return builder.toString();
	}

	/**
	 * Generates a file name based on provided file by adding a copy number (to the end)
	 * if a file with that name already exists in the specified file directory.
	 *
	 * @param file
	 * @return
	 */
	public static String generateFileName(File file) {
		String filePath = file.getAbsolutePath();
		String fileName = filePath.substring(0, filePath.lastIndexOf("."));
		String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());

		File newFile = file;
		if (!file.getName().endsWith(String.format(").%s", fileExt))) {
			newFile = new File(fileName + " (1)." + fileExt);
		}

		int index = 2;
		while (newFile.exists()) {
			fileName = newFile.getAbsolutePath();
			String next = fileName.substring(0, fileName.lastIndexOf("(") + 1);
			String name = next + index + ")." + fileExt;
			newFile = new File(name);
			index++;
		}

		return newFile.getAbsolutePath();
	}

	/**
	 * Gets the directory path the program is executed from.
	 *
	 * @return
	 */
	public static String getExecutedPath() {
		String programPath = FileHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		if (programPath.contains(File.separator)) {
			File dir = new File(programPath.substring(0, programPath.lastIndexOf(File.separator)));
			return dir.getAbsolutePath();
		}
		return null;
	}

	/**
	 * @see FileHelper#getFileExtension(String, boolean)
	 */
	public static String getFileExtension(String filePath) {
		return getFileExtension(filePath, false);
	}

	/**
	 * Gets/returns the file extension for the specified file path.
	 *
	 * @param filePath specified file path
	 * @param includeDot toggle showing if dot (".") should be included in result
	 * @return File extension or NULL if no extension exists in filename.
	 */
	public static String getFileExtension(String filePath, boolean includeDot) {
		String fileName = getFileName(filePath);

		if (fileName == null || !fileName.contains(".") || fileName.endsWith(".")) {
			return null;
		} else {
			int index = includeDot ? fileName.lastIndexOf(".") : fileName.lastIndexOf(".") + 1;
			return fileName.substring(index);
		}
	}

	/**
	 * Checks if specified file path extension matches one from the provided set.
	 *
	 * @param filePath
	 * @param extensions
	 * @return
	 */
	public static boolean compareFileExtensions(String filePath, Set<String> extensions) {
		if (extensions == null || extensions.size() == 0)
			throw new IllegalArgumentException("Provided extension set is empty");

		String fileExtension = getFileExtension(filePath);
		if (fileExtension == null)
			throw new IllegalArgumentException(String.format("Provided file does not include an extension: %s", filePath));

		for (String extension : extensions) {
			if (fileExtension.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see FileHelper#getFileName(String, boolean)
	 */
	public static String getFileName(String filePath) {
		return getFileName(filePath, true);
	}

	/**
	 * com.mobisleapps.devide.common.util.FileHelper#getFileName(java.lang.String, boolean)
	 *
	 * @param filePath
	 * @param verifyFile toggle showing if the file existence should be verified
	 * @return specified file Name or NULL.
	 */
	public static String getFileName(String filePath, boolean verifyFile) {
		if (filePath == null || filePath.endsWith(File.separator))
			return null;

		if (verifyFile) {
			if (filePath.contains(File.separator)) {
				filePath = filePath.substring(filePath.lastIndexOf(File.separator));
			}
			return filePath;
		} else {
			File file = new File(filePath);
			return (file.exists() && file.isDirectory()) ? null : file.getName();
		}
	}

	/**
	 * Gets directory object of the specified file path,
	 * otherwise if provided path does not specify/contain any folder separators
	 * returns current executing directory.
	 *
	 * @param filePath
	 * @return
	 */
	public static File getDirectory(String filePath) {
		File fileOrDir = new File(filePath);
		if (fileOrDir.exists() && fileOrDir.isDirectory())
			return fileOrDir;
		if (fileOrDir.getParent() != null) {
			return fileOrDir.getParentFile();
		} else {
			String executedPath = getExecutedPath();
			return executedPath != null ? new File(executedPath) : new File(".");
		}
	}

	/**
	 * Serializes provided object into specified file location.
	 *
	 * @param o
	 * @param filePath
	 * @throws IOException
	 */
	public static void serializeToFile(Object o, String filePath) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(  );
		ObjectOutputStream oout = new ObjectOutputStream( bout );

		oout.writeObject(o);
		oout.close();
		byte[] bytes = bout.toByteArray();

		FileOutputStream fileOuputStream = new FileOutputStream(filePath);
		fileOuputStream.write(bytes);
		fileOuputStream.close();
	}

	/**
	 * De-serializes specified file.
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserializeFromFile(String filePath) throws IOException, ClassNotFoundException {
		Path path = Paths.get(filePath);
		byte[] bytes = Files.readAllBytes(path);

		ByteArrayInputStream in = new ByteArrayInputStream(bytes, 0, bytes.length);
		ObjectInputStream oin = new ObjectInputStream(in);
		return oin.readObject();
	}

}
