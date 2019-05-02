package arizona.cs.edu.yuefengli;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GetFileName

{
	private static ArrayList<String> listname = new ArrayList<String>();

	public GetFileName() {

	}

	public ArrayList<String> readfile(String filepath) throws IOException {
		File file = new File(filepath);
		if (!file.isDirectory()) {
			listname.add(file.getName());
		} else if (file.isDirectory()) {
			System.out.println("文件");
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath);
				if (!readfile.isDirectory()) {
					listname.add(readfile.getName());
				} else if (readfile.isDirectory()) {
					readfile(filepath + "\\" + filelist[i]);// 递归
				}
			}
		}
		for (int i = 0; i < listname.size(); i++) {
			// System.out.println(listname.get(i));
		}
		return listname;
	}

	/*public static void main(String[] args) throws IOException {
		GetFileName a = new GetFileName();
		a.readfile("C:/Users/vincent/Desktop/483finalobject/finalsourcecode/yuefengli/target/classes/wikitext/");
	}*/
}
