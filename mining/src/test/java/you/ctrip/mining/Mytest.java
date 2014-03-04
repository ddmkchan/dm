package you.ctrip.mining;

import you.ctrip.mining.classifiers.NaiveBayesModel;
import weka.core.converters.*;

public class Mytest {
	static NaiveBayesModel messageCl=null; 
	
	
	public static void main(String[] args) {
		String modelname="NaiveBayes";
		boolean negOrPos=false;
		

		String trainSet="D:\\data\\testset";

		
		messageCl = NaiveBayesModel.trainModel(trainSet, modelname);
//		messagenbCl=NaiveBayesMultinomialModel.trainModel(trainSet, "NaiveBayesM2"); 
		
		

		}
}