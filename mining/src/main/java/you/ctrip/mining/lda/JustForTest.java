package you.ctrip.mining.lda;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.TopicAssignment;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class JustForTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File output = new File("D:\\data\\Lda\\simplelda_model");
//		File output = new File("D:\\data\\Lda\\mylda");
		MySimpleLDA model;
		try {
			model = MySimpleLDA.read(output);

			for (int doc = 0; doc < model.data.size(); doc++) {
				FeatureSequence tokenSequence =
					(FeatureSequence) model.data.get(doc).instance.getData();
				System.out.println(tokenSequence);
			}
			
//	        InstanceList testing = new InstanceList(model.trainingset.getPipe());
//	        testing.addThruPipe(new Instance("workers union", null, "test instance", null));
//
//	        TopicInferencer inferencer = model.getInferencer();
//	        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
//	        for (double s:testProbabilities) {
//	            System.out.println(s);
//	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//    	File f = new File("D:\\data\\Lda\\ap.txt");
//    	FileInputStream input;
//		try {
//			input = new FileInputStream(f);
//	    	
//	        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
//
//	        pipeList.add( new CharSequenceLowercase() );
//	        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
//	        pipeList.add( new TokenSequenceRemoveStopwords(new File("D:\\data\\Lda\\stoplists\\en.txt"), "UTF-8", false, false, false) );
//	        pipeList.add( new TokenSequence2FeatureSequence() );
//
//	        InstanceList instances = new InstanceList (new SerialPipes(pipeList));
//
//	        Reader fileReader = new InputStreamReader(input, "UTF-8");
//	        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
//	                                               3, 2, 1));
//	        
//	        File ff = new File("D:\\data\\Lda\\my_serialize");
//	        serialize(ff, instances);
//	        
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}
	
	public static void serialize(File f, Object obj) throws Exception {
		OutputStream outputFile = new FileOutputStream(f); 
		ObjectOutputStream cout = new ObjectOutputStream(outputFile); 
		cout.writeObject(obj);
		cout.close();
	}

}
