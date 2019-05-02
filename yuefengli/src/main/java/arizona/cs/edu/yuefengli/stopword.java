package arizona.cs.edu.yuefengli;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class stopword {
	List<String> result;
	final String SAMPLE_TEXT="This is baeldung.com Lucene Analyzers test";
	public stopword() {
		
	}
	

	 public List<String> analyze(String text, Analyzer analyzer) throws IOException{
		    List<String> result = new ArrayList<String>();
		    TokenStream tokenStream = analyzer.tokenStream("cs", text);
		    CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
		    tokenStream.reset();
		    while(tokenStream.incrementToken()) {
		       result.add(attr.toString());
		       System.out.println(result);
		    }       
		    return result;
		}
	 public void remove() throws IOException {
		    List<String> result = analyze(SAMPLE_TEXT, new StandardAnalyzer());
		 

		}
}
