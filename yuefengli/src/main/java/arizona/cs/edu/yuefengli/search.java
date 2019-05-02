package arizona.cs.edu.yuefengli;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class search {
	boolean lemma;
	boolean BM;
	
	public search() {
		lemma=true;
		BM=true;
	}
	public static void main(String[] args) throws IOException {
		search a = new search();
		if (true/* args[0]=="context" */) {
			try {
				a.readfile();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (false/* args[0]=="both" */) {
			try {
				a.both();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
}
	public void readfile() throws IOException, ParseException {

    	
    	Properties props = new Properties();
		// set the list of annotators to run
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		
    	File file = new File("questions.txt");
    	int count=1;
    	int correctct=0;
    	String line="";
    	StandardAnalyzer analyzer = new StandardAnalyzer();
    	int hit=3;
    	String result="";
    	Query q1=null;
    	Query q2=null;
    	String question="";
    	int i=1;
    	try (Scanner inputScanner = new Scanner(file, "UTF-8")) {
    		while (inputScanner.hasNextLine()) {
    			line=inputScanner.nextLine();
    			line=line.replace('!', ' ');
    			line=line.replace('-', ' ');
    			//System.out.println(line);
    			
    			if(count==1) {
    				//System.out.println("propertyes: ");
    				result+=line;
    				try {
						q1 = new QueryParser("categories", analyzer).parse(line);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			else if(count==2) {
    				//System.out.println("context: ");
    				
    				result+=line;
    				Annotation document = new Annotation(line);
					pipeline.annotate(document);
					List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
					for (CoreMap sentence : sentences) {
						for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
							question += " " + token.get(LemmaAnnotation.class).toString();
						}
					}
					//System.out.println(question);
					try {
						if(lemma)
							q2 = new  QueryParser("context", analyzer).parse(question);
						else
							q2 = new  QueryParser("context", analyzer).parse(line);
					//System.out.println(q2.toString());
					//System.out.println(q2.equals(null));
					question="";
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				
    			}
    			else if(count==4) {
    				count=0;
    				 Directory directory = FSDirectory.open(Paths.get("").toAbsolutePath());
    			        IndexReader reader = DirectoryReader.open( directory);
    				IndexSearcher searcher = new IndexSearcher(reader);
    				if(BM) {
    					searcher.setSimilarity(new BM25Similarity());
    					}
    				TopDocs docs = searcher.search(q2, hit);
    				ScoreDoc[] hits = docs.scoreDocs;
    				if(hits.length>=1) {
    				    int docId = hits[0].doc;
    				    Document d = searcher.doc(docId);
    				    System.out.println( i + ". My search:  " + d.get("docid"));
    				    if(result.equals(d.get("docid").toString())) {
    				    	correctct++;
       					 System.out.println("correct one"+correctct);
       					 }

    				}
    				reader.close();
    				i++;
    			}
    			else {
    				 System.out.println(i+". true answer: "+ line);
    				 result=line;

    			}
    			count++;
    		}
    	}
	}
	public void both() throws IOException, ParseException {

    	
    	Properties props = new Properties();
		// set the list of annotators to run
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		
    	File file = new File("questions.txt");
    	int count=1;
    	String line="";
    	StandardAnalyzer analyzer = new StandardAnalyzer();
    	int hit=3;
    	String result="";
    	Query q1=null;
    	Query q2=null;
    	String question="";
    	String cat="";
    	int i=1;
    	try (Scanner inputScanner = new Scanner(file, "UTF-8")) {
    		while (inputScanner.hasNextLine()) {
    			line=inputScanner.nextLine();
    			line=line.replace('!', ' ');
    			line=line.replace('-', ' ');
    			//System.out.println(line);
    			
    			if(count==1) {
    				//System.out.println("propertyes: ");
    				result+=line;
    				cat=line;
    			}
    			else if(count==2) {
    				//System.out.println("context: ");
    				
    				result+=line;
    				Annotation document = new Annotation(line);
					pipeline.annotate(document);
					List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
					for (CoreMap sentence : sentences) {
						for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
							question += " " + token.get(LemmaAnnotation.class).toString();
						}
					}
					if (!lemma)
						question=line;

    				
    			}
    			else if(count==4) {
    				//System.out.println( "two feild");
    				QueryParser queryParser = new QueryParser("<default field>", analyzer);
    				question=question.replace(':', ' ');
    				String special = "categories:" + cat + " OR context:" + question;
    				count=0;
    				 Directory directory = FSDirectory.open(Paths.get("").toAbsolutePath());
    			     IndexReader reader = DirectoryReader.open( directory);
    				IndexSearcher searcher = new IndexSearcher(reader);
    				try {
    				TopDocs docs = searcher.search(queryParser.parse(special), hit);

    				ScoreDoc[] hits = docs.scoreDocs;
    				if(hits.length>=1) {
    					//System.out.println( "find");
    				    int docId = hits[0].doc;
    				    Document d = searcher.doc(docId);
    				    System.out.println( i + ". My search:  " + d.get("docid"));
    				    result+=d.get("docid");

    				}
    				reader.close();
    				i++;
    				question="";
    				}
    				catch(ParseException e) {
    					e.printStackTrace();
    				}
    			}
    			else {
    				 System.out.println(i+". true answer: "+ line);
    				 if(result==line)
    					 System.out.println(i+". true answer: "+ line);
    			}
    			count++;
    		}
    	}
	}
}
