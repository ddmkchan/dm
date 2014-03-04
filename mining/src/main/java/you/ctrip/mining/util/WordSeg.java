package you.ctrip.mining.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

public class WordSeg {

	private boolean withPOS = true;
	HashSet<String> stoplist = null;
	
	public WordSeg() {
	}
	
	public WordSeg(String inputFile) {
		
		BufferedReader input = null;
		stoplist = new HashSet<String>();
		String line;
		try {
			input = new BufferedReader( new InputStreamReader( new FileInputStream(inputFile), "UTF-8" ));
			while (( line = input.readLine()) != null) {
				if(line.trim().length()>=1 && line!=null){
					stoplist.add(line.trim());
				}
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String stringSegment(String sentence){

		String resultContent = "";

		List<Term> list = ToAnalysis.parse(sentence);
		new NatureRecognition(list).recognition();

		StringBuilder sb = new StringBuilder();
		for (Term term : list) {
			String s = term.toString();
			if (stoplist != null) {//停用词表非空
				if (s.contains("/")) {
					String[] segs = s.split("\\/");

					if (!stoplist.contains(segs[0])) {
						sb.append(s+" ");	
					}
				}
			} else {
				if (s.contains("/")) {
					sb.append(s+" ");	
				}
			}
		}
		resultContent = sb.toString();
		
		return resultContent;
	}
	
	public static void main(String[] args) throws IOException {
//		WordSeg ws = new WordSeg("D:/data/Lda/stoplists/wordseg_stopwords_utf8.txt");
//		WordSeg ws = new WordSeg();
//		System.out.println(ws.stringSegment("严格.D来说，下面这个配置项，是得 地决定HDFS文件block数量的多少(也就是文件个数)，"));
		ArrayList<Integer> ll = new ArrayList<Integer>();
		ll.add(1);
		ll.add(2);
		for (int j:ll) {
			System.out.println(j);
		}
	}

}
