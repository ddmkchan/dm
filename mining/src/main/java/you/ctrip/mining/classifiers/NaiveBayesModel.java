package you.ctrip.mining.classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.stemmers.NullStemmer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class NaiveBayesModel implements Serializable {
	private Instances instances = null;
	private StringToWordVector filter = new StringToWordVector();
	private FastVector attributes = null;
	private Classifier classifier = new NaiveBayesMultinomial();
	private Attribute msg_att = null;
	
	private String kind = "";

	public String Getkind() {
		return kind;
	}

	/**
	 * 构造分类器，主要及时对数据格式，类标，类别数目等进行说明
	 */
	public NaiveBayesModel() throws Exception {
		System.out.println("--------构造函数-------------");
//		String strPath = "D:\\testLDA\\stoplists\\cn.txt";
		filter.setStemmer(new NullStemmer());
//		filter.setStopwords(new File(strPath));
		// filter.setUseStoplist(true);
		// filter.setTFTransform(true);

//		filter.setIDFTransform(true);
		filter.setOutputWordCounts(true);

//		filter.setMinTermFreq(50);
//		filter.setWordsToKeep(3);

		System.out.println("words to keep ----------- "+filter.getWordsToKeep());
//		System.out.println(filter.getc);
		String nameOfDataset = "StaticMethod";
		attributes = new FastVector(2);
//		msg_att = new Attribute("Message", (FastVector) null);
		attributes.addElement(new Attribute("Message", (FastVector) null));
		FastVector classValues = new FastVector(2);// 类标向量，共有两类
		classValues.addElement("neg");
		classValues.addElement("pos");
		classValues.addElement("others");
		attributes.addElement(new Attribute("Class", classValues));
		instances = new Instances(nameOfDataset, attributes, 10);// 可以把instance认为是行，attribute认为是列
		instances.setClassIndex(instances.numAttributes() - 1);// 类表在instance中的那列

	}
	
	/**
	 * 添加数据到训练集中
	 */
	public void updateData(String message, String classValue) throws Exception {
		Instance instance = makeInstance(message, instances);
		instance.setClassValue(classValue);
		instances.add(instance);
	}

	public void evaluateCross() {

		try {
			filter.setInputFormat(instances);
			Instances filteredData = Filter.useFilter(instances, filter);// 这才真正产生符合weka算法输入格式的数据集

			filteredData.setClassIndex(filteredData.numAttributes() - 1);

			Evaluation eval = new Evaluation(filteredData);
			eval.crossValidateModel(classifier, filteredData, 3, new Random(1)); // 十次交叉检验的方法来评价分类器*/
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toMatrixString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * 文本分类要特别一点，因为在使用StringToWordVector对象计算文本中词项(attribute)权重的时候需要用到全局变量，比如DF，
	 * 所以这里需要批量处理 在weka中要注意有些机器学习算法是批处理有些不是
	 */
	public void finishBatch() throws Exception // methodNaiveBayesM
	{
		filter.setInputFormat(instances);
		System.out.println("numInstances "+instances.numInstances()+"\t");
		Instances filteredData = Filter.useFilter(instances, filter);// 这才真正产生符合weka算法输入格式的数据集

		classifier.buildClassifier(filteredData);// 真正的训练分类器
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream("trainSet.arff"), "UTF-8"), true);
		out.print(filteredData.toString());
		out.close();

		Evaluation eval = new Evaluation(filteredData);
		eval.crossValidateModel(classifier, filteredData, 2, new Random(1)); // 十次交叉检验的方法来评价分类器
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toSummaryString());
		System.out.println(eval.toMatrixString());
	}

	/**
	 * 分类过程
	 */
	public String classifyMessage(String message) throws Exception {
		filter.input(makeInstance(message, instances.stringFreeStructure()));
		Instance filteredInstance = filter.output();// 必须使用原来的filter

		double predicted = classifier.classifyInstance(filteredInstance);// (int)predicted是类标索引
		kind = instances.classAttribute().value((int) predicted);
		// System.out.println("Message classified as : "
		// + instances.classAttribute().value((int) predicted));

		return instances.classAttribute().value((int) predicted);

	}

	private Instance makeInstance(String text, Instances data) {
		Instance instance = new Instance(2);
		Attribute messageAtt = data.attribute("Message");
		instance.setValue(messageAtt, messageAtt.addStringValue(text));
		instance.setDataset(data);
		return instance;
	}

	public static String getStringFromFile(File file) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file.getPath()), "UTF-8"));
			String line;
			while (true) {
				if ((line = br.readLine()) == null)
					break;
				sb.append(line.trim());
			}
			br.close();
		} catch (Exception e) {
		}
		return sb.toString();
	}

	/*
	 * static String modelname="weka.message"; public static void main(String[]
	 * options) { try { StaticMethod messageCl=null; if(new
	 * File(modelname).exists()) messageCl=loadModel(modelname); else {
	 * messageCl=trainModel(); try { ObjectOutputStream modelOutObjectFile = new
	 * ObjectOutputStream(new FileOutputStream(modelname));
	 * modelOutObjectFile.writeObject(messageCl); modelOutObjectFile.close(); }
	 * catch (Exception e){} } String testPath="C:\\test\\7.txt";
	 * messageCl.classifyMessage(getStringFromFile(new File(testPath))); } catch
	 * (Exception e){} }
	 */
	/**
	 * 训练分类器
	 */
	public static NaiveBayesModel trainModel(String trainDir, String modelname) {

		NaiveBayesModel mc = null;
		try {
			mc = new NaiveBayesModel();
			// String basePath="C:/Program Files/Weka-3-6/stockmix";
			String basePath = trainDir;
			File base = new File(basePath);
			for (File dir : base.listFiles()) // 文件夹遍历neg pos
			{
				for (File file : dir.listFiles()) // neg pos下面的文件遍历
				{
					String message = getStringFromFile(file);
					String classValue = dir.getName(); // 得到文件夹名
					mc.updateData(message, classValue);// 添加一条训练样本，classvalue就是类标
				}
			}
			mc.finishBatch();// 训练过程

			ObjectOutputStream modelOutObjectFile = new ObjectOutputStream(
					new FileOutputStream(modelname)); // 把对象保存到相应的文件名中
			modelOutObjectFile.writeObject(mc);
			modelOutObjectFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mc;
	}

	public static NaiveBayesModel loadModel(String modelname) {
		NaiveBayesModel mc = null;
		try {
			ObjectInputStream modelInObjectFile = new ObjectInputStream(
					new FileInputStream(modelname));
			mc = (NaiveBayesModel) modelInObjectFile.readObject();
			modelInObjectFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mc;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(filter);
		out.writeObject(classifier);

		out.writeObject(attributes);
		out.writeObject(instances.relationName());
		out.writeInt(instances.classIndex());

	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		filter = (StringToWordVector) in.readObject();
		classifier = (Classifier) in.readObject();

		FastVector attr = (FastVector) in.readObject();
		attributes = attr;
		String relationname = (String) in.readObject();
		int classnidex = in.readInt();
		instances = new Instances(relationname, attributes, 100);// 可以把instance认为是行，attribute认为是列
		instances.setClassIndex(classnidex);// 类表在instance中的那列
	}
}