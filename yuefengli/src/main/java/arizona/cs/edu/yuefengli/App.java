package arizona.cs.edu.yuefengli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Hello world!
 *
 */
public class App 
{	
	String write;
	public App() {
		write="";
	}

    public void readfile() throws IOException {
    	//ClassLoader classLoader = getClass().getClassLoader(); 
    	//URL filename = classLoader.getResource("question/questions.txt"); 
    	//System.out.println("getResouce: " + filename); 
    	File file = new File("questions.txt");
        System.out.println( "Hello World!" );
        System.out.println(Paths.get("").toAbsolutePath());
        try (Scanner inputScanner = new Scanner(file,"UTF-8")){
        	while(inputScanner.hasNextLine()) {
        		//System.out.println( inputScanner.nextLine() );
        		write+=inputScanner.nextLine();
        	}
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Directory directory = FSDirectory.open(Paths.get("").toAbsolutePath());
        IndexReader r = DirectoryReader.open( directory);

        System.out.println( r.document(1001).getField("docid"));
        System.out.println();
        System.out.print( r.document(1001).getField("context"));
        System.out.println();
        System.out.println( r.document(1001).getField("categories"));
        r.close();
    }
}
