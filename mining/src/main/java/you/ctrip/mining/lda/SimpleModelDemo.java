package you.ctrip.mining.lda;

import cc.mallet.util.*;
import cc.mallet.types.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.topics.*;

import java.util.*;
import java.util.regex.*;
import java.io.*;

public class SimpleModelDemo {

    public static void main(String[] args) throws Exception {

    	File f = new File("D:\\data\\Lda\\ap.txt");
    	FileInputStream input = new FileInputStream(f);
    	
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add( new CharSequenceLowercase() );
        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
        pipeList.add( new TokenSequenceRemoveStopwords(new File("D:\\data\\Lda\\stoplists\\en.txt"), "UTF-8", false, false, false) );
        pipeList.add( new TokenSequence2FeatureSequence() );

        InstanceList instances = new InstanceList (new SerialPipes(pipeList));

//        Reader fileReader = new InputStreamReader(new FileInputStream(new File(args[0])), "UTF-8");
        Reader fileReader = new InputStreamReader(input, "UTF-8");
        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                                               3, 2, 1)); // data, label, name fields

//    	File f = new File("D:\\data\\Lda\\my_serialize");
//    	InputStream inputFile = new FileInputStream(f);
//    	ObjectInputStream cin = new ObjectInputStream(inputFile);
//    	InstanceList instances = (InstanceList) cin.readObject();
        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = 20;
        MySimpleLDA model = new MySimpleLDA(numTopics, 1.0, 0.01);

        model.addInstances(instances);
        model.sample(50);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
//        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only, 
        //  for real applications, use 1000 to 2000 iterations)
//        model.setNumIterations(50);
//        model.estimate();

        // Show the words and topics in the first instance

        // The data alphabet maps word IDs to strings
//        Alphabet dataAlphabet = instances.getDataAlphabet();
//        
//        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
//        LabelSequence topics = model.getData().get(0).topicSequence;
//        
//        Formatter out = new Formatter(new StringBuilder(), Locale.US);
//        for (int position = 0; position < tokens.getLength(); position++) {
//            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
//        }
//        System.out.println(out);
        
        File output = new File("D:\\data\\Lda\\simplelda_model");
        model.write(output);
        // Estimate the topic distribution of the first instance, 
        //  given the current Gibbs state.
        
        // Create a new instance with high probability of topic 0
//        StringBuilder topicZeroText = new StringBuilder();
//        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();
//
//        int rank = 0;
//        while (iterator.hasNext() && rank < 10) {
//            IDSorter idCountPair = iterator.next();
//            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
//            rank++;
//        }
//
//        // Create a new instance named "test instance" with empty target and source fields.
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance("workers union", null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        for (double s:testProbabilities) {
            System.out.println(s);
        }

    }

}
