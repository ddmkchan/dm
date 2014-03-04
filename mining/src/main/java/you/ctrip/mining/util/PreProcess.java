package you.ctrip.mining.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PreProcess {
	
//	static WordSeg ws = new WordSeg();
	static WordSeg ws = new WordSeg("D:/data/Lda/stoplists/wordseg_stopwords_utf8.txt");

	public static List<File> getAllFile(String dir){
		File DIR = new File(dir);
		File[] fileList = DIR.listFiles();
		List<File> segFileList = new ArrayList<File>();
		for(File file:fileList){
			if(file.isDirectory()){
				segFileList.addAll(getAllFile(file.getAbsolutePath()));
			}
			else{
				segFileList.add(file);
			}
		}
		return segFileList;
	}
	
	public static void segmentFile(String inputFile, String outputFile){

		try {
			PrintWriter out = new PrintWriter(outputFile,"UTF-8");

			BufferedReader input = null;
			input = new BufferedReader( new InputStreamReader( new FileInputStream(inputFile), "UTF-8" ));
		
			String line;
			while (( line = input.readLine()) != null) {
				if(line.trim().length()>1 && line!=null){
					String rs = ws.stringSegment(line);
					out.println(rs);
				}
			}
			input.close();
			out.close();
		} catch (IOException e) {
		
		}
	}
	
	public static void segmentDir(String inputDir, String outputDir){//把inputDir经过分词后放入outputDir中
		List<File> segFileList=getAllFile(inputDir);

		for (File file:segFileList){
			String dircetname = outputDir+file.getAbsolutePath().substring(inputDir.length());
			File newFile = new File(dircetname);
			File newdir = newFile.getParentFile();
			if (!newdir.exists())
				newdir.mkdirs();
			segmentFile(file.getAbsolutePath(), dircetname);    //分词
		}
	}
	
	public static String readToString(String fileName) {
//		String encoding = "ISO-8859-1";
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		List<File> files = PreProcess.getAllFile("D:\\data\\游记\\shanghai");
		try {
			PrintWriter out = new PrintWriter("D:\\data\\游记\\output\\trainset", "UTF-8");
			for (File f:files) {
				String[] segs = f.getName().split("\\.");
				String content = readToString(f.getAbsolutePath());
				String[] lines = content.split("\n");
				try {
					out.println(segs[0]+"\t"+"X"+"\t"+ws.stringSegment(lines[1]));
				} catch (Exception e) {
					
				}

			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
