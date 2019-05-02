package arizona.cs.edu.yuefengli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.RAMDirectory;

import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

public class index {
	String inputFilePath = "";
	// Directory index = new RAMDirectory();
	boolean endFlag;
	List<String> Filelist;

	public index(String inputFile) throws IOException {
		inputFilePath = inputFile;
		endFlag = false;

	}

	public void buildIndex() throws IOException {

		// Get file from resources folder

		// initialize the analyzer
		StandardAnalyzer analyzer = new StandardAnalyzer();
		// 1. create the index
		Directory index = FSDirectory.open(Paths.get("").toAbsolutePath());
		// new RAMDirectory();
		// FSDirectory.open(Paths.get("").toAbsolutePath());

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);
		String page = "";
		String context = "";
		String categories = "";
		String lastpage = "";
		// set up pipeline properties
		Properties props = new Properties();
		// set the list of annotators to run
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma");

		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		GetFileName a = new GetFileName();
		Filelist = a
				.readfile("C:/Users/vincent/Desktop/483finalobject/finalsourcecode/yuefengli/target/classes/wikitext/");
		for (String temp : Filelist) {
			//ClassLoader classLoader = getClass().getClassLoader();
			//URL filename = classLoader.getResource("wikitext/" + temp);
			//System.out.println("getResouce: " + filename);
			File file = new File(temp);
			try (Scanner inputScanner = new Scanner(file, "UTF-8")) {
				page = inputScanner.nextLine();
				while (inputScanner.hasNextLine()) {
					String line = inputScanner.nextLine();

					if (line.startsWith("[[") && line.endsWith("]]")) {
						lastpage = page;
						page = line;
						endFlag = true;
					} else {
						if (line.startsWith("CATEGORIES")) {
							categories = line;
						} else {
							if (!line.equals("") && !line.equals(" ") && !line.equals("  ")) {
								Annotation document = new Annotation(line);
								pipeline.annotate(document);
								List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
								for (CoreMap sentence : sentences) {
									for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
										context += " " + token.get(LemmaAnnotation.class).toString();
									}
								}
							}
						}
					}

					if (endFlag) {
						lastpage = lastpage.replace("[", "");
						lastpage = lastpage.replace("]", "");
						addDoc(w, lastpage, categories, context);
						endFlag = false;
						lastpage = "";
						categories = "";
						context = "";
					}
				}
				page = page.replace("[", "");
				page = page.replace("]", "");
				addDoc(w, page, categories, context);

				inputScanner.close();

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("read unsuccess!!!");
			}

		}
		w.close();
	}

	private static void addDoc(IndexWriter w, String DocId, String categories, String text) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("context", text, Field.Store.YES));
		doc.add(new TextField("categories", categories, Field.Store.YES));

		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("docid", DocId, Field.Store.YES));
		w.addDocument(doc);
	}

	/*public static void main(String[] args) {
		try {
			index in = new index("test.txt");
			in.buildIndex();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("read unsuccess213!!!");
			e.printStackTrace();
		}*/
	//}

}
