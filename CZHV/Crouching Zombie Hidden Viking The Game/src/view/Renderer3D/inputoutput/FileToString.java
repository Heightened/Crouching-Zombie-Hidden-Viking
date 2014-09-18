package view.renderer3D.inputoutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class FileToString {
	public static ArrayList<String> read(String filename){
		File f = new File(filename);
		ArrayList<String> stringList = new ArrayList<String>();
		try{
			BufferedReader b = new BufferedReader(new FileReader(f));
			String line = "";
			while ((line = b.readLine()) != null){
				stringList.add(line);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return stringList;
	}
}
