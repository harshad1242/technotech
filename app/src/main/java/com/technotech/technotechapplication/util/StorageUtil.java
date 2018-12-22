package com.technotech.technotechapplication.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class StorageUtil {

	private static String DB_PATH = "";

	
	//,"/mnt/sdcard/"
	private static final String[] possiblePahts = new String[] {
			System.getenv("SECONDARY_STORAGE") + File.separator,
			"/storage/sdcard1/", "/mnt/external_sd/", "/storage/external_sd/",
			"/storage/extSdCard/", "/storage/sdcard_ext/",
			"/storage/external/", "/mnt/extsd/" };

	public static final ArrayList<String> getAvailableStorages() {
		ArrayList<String> list = new ArrayList<String>();

		String internal_path = getInternalStoragePath();
		String external_path = getExternalStoragePath();
		String usb_path = getUSBStoragePath();

		if (internal_path.trim().length() != 0) {
			list.add(internal_path);
		}
		if (external_path.trim().length() != 0) {
			list.add(external_path);
		}
		if (usb_path.trim().length() != 0) {
			list.add(usb_path);
		}

		return list;
	}

	public static String getInternalStoragePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/";
	}

	private static final boolean isExternalStorageAvailable() {

		String state = Environment.getExternalStorageState();
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (mExternalStorageAvailable == true
				&& mExternalStorageWriteable == true) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressLint("NewApi")
	public static String getExternalStoragePath() {

		boolean mExternalStorageAvailable = isExternalStorageAvailable();
		if (!mExternalStorageAvailable) {
			return null;
		}

		String file_path = null;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
		{
			file_path = getExternalDBPath() + File.separator;
		}
		else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
		{
			HashSet<String> s1 = getExternalMounts();
			String[] s2 = s1.toArray(new String[s1.size()]);
			

			if(s2.length > 0)
			{
			String[] path=s2[0].split("/");
			
			String pth=File.separator+"storage"+File.separator+path[path.length - 1]+"/";
			
			

				
				File file=new File(pth);
				
				if(file.exists())
				{
					file_path=pth;
				}
				else
				{
					File f;
				
					if (file_path == null || file_path.length() == 0) {
						for (String path1 : possiblePahts) {
							f = new File(path1);
							if (f.exists() ) {
								if (!path1.contains(getInternalStoragePath())) {
									file_path = path1;
									break;
									
									
									
								}
							}
						}
				}
				}	
			
				
			}
			
			else
			{
				File f;
			
				if (file_path == null || file_path.length() == 0) {
					for (String path1 : possiblePahts) {
						f = new File(path1);
						if (f.exists() ) {
							if (!path1.contains(getInternalStoragePath())) {
								file_path = path1;
								break;
								
								
								
							}
						}
					}
			}
			}	
			
			
			
		}

		
//		file_path = getExternalDBPath() + File.separator;
//		File f;
//
//		if (file_path == null || file_path.length() == 0) {
//			for (String path : possiblePahts) {
//				f = new File(path);
//				if (f.exists() && f.canWrite()) {
//					if (!path.contains(getInternalStoragePath())) {
//						file_path = path;
//						break;
//					}
//				}
//			}
//		}

		return file_path;

		// try {
		// File gpxfile = new File(file_path + "IdealCheck" + File.separator,
		// "temp.txt");
		// Log.d("PATHS", file_path + "IdealCheck" + File.separator
		// + "temp.txt");
		// BufferedWriter bW;
		// if (gpxfile.exists()) {
		// gpxfile.delete();
		// } else {
		// gpxfile.getParentFile().mkdirs();
		// }
		//
		// // gpxfile.createNewFile();
		// bW = new BufferedWriter(new FileWriter(gpxfile));
		// bW.write("test");
		// bW.newLine();
		// bW.flush();
		// bW.close();
		//
		// File file = new File(getInternalStoragePath() + "IdealCheck"
		// + File.separator + "temp.txt");
		// if (file.exists()) {
		// file_path = null;
		// }
		// gpxfile.delete();
		// file.delete();
		//
		// return file_path;
		//
		// } catch (IOException ignore) {
		// Log.e("IGNORE", "**IGNORE**");
		// ignore.printStackTrace();
		// Log.e("IGNORE", "**IGNORE**");
		// return file_path;
		// }

	}
	
	
	
	public static HashSet<String> getExternalMounts() {
		final HashSet<String> out = new HashSet<String>();
		String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
		String s = "";
		try {
			final Process process = new ProcessBuilder().command("mount")
					.redirectErrorStream(true).start();
			process.waitFor();
			final InputStream is = process.getInputStream();
			final byte[] buffer = new byte[1024];
			while (is.read(buffer) != -1) {
				s = s + new String(buffer);
			}
			is.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// parse output
		final String[] lines = s.split("\n");
		for (String line : lines) {
			if (!line.toLowerCase(Locale.US).contains("asec")) {
				if (line.matches(reg)) {
					String[] parts = line.split(" ");
					for (String part : parts) {
						if (part.startsWith("/"))
							if (!part.toLowerCase(Locale.US).contains("vold")) {
								// String[] temp=part.split("/");
								//
								// part="/"+temp[1]+"/"+temp[3]+"/";

								out.add(part);
							}
					}
				}
			}
		}
		return out;
	}

	public static String getUSBStoragePath() {
		String file_path = null;
		File f;

		f = new File("/storage/usbcard/");
		if (f.exists() && f.canWrite()) {
			file_path = "/storage/usbcard/";
		}
		f = new File("/storage/usbotg/");
		if (f.exists() && f.canWrite()) {
			file_path = "/storage/usbotg/";
		}
		f = new File("/storage/UsbDriveA/");
		if (f.exists() && f.canWrite()) {
			file_path = "/storage/UsbDriveA/";
		}
		f = new File("/storage/usbcard1/");
		if (f.exists() && f.canWrite()) {
			file_path = "/storage/usbcard1/";
		}
		f = new File("/storage/usbdisk/");
		if (f.exists() && f.canWrite()) {
			file_path = "/storage/usbdisk/";
		}
		f = new File("/storage/usbdisk0/");
		if (f.exists() && f.canWrite()) {
			file_path = "/storage/usbdisk0/";
		}
		return file_path;
	}

	private static String getExternalDBPath() {

		File file = new File("/system/etc/vold.fstab");
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			if (fr != null) {
				br = new BufferedReader(fr);
				String s = br.readLine();
				while (s != null) {
					if (s.startsWith("dev_mount")) {
						String[] tokens = s.split("\\s");
						DB_PATH = tokens[2]; // mount_point
						if (!Environment.getExternalStorageDirectory()
								.getAbsolutePath().equals(DB_PATH)) {
							break;
						}
					}
					s = br.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return DB_PATH;
	}
	// kale hu avano 6u tyare have solve karis ok
}
