package com.att.libs.executor.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

public class EvenOddFileTask extends ExecutableTask {
	
	String folder = "input";
	String inputFile;
	String outputFile;
	String type;
	
	@Override
	public void init(Map<String, Object> data) {
		inputFile = data.get("inputFile").toString();
		outputFile = data.get("outputFile").toString();
		type = data.get("type").toString();
		
		if(!new File(folder).exists()){
			new File(folder).mkdir();
		}
		if(new File(folder + "/" + outputFile).exists()){
			new File(folder + "/" + outputFile).delete();
		}
	}

	@Override
	public void execute() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(folder + "/" + inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(folder + "/" + outputFile));

		String line = reader.readLine();
		while(line != null){
			int val = Integer.parseInt(line.split(" ")[1]);
			if(type.equals("even") && val%2==0){
				writer.write(line);
				writer.newLine();
			}
			else if(type.equals("odd") && val%2!=0){
				writer.write(line);
				writer.newLine();
			}
			line = reader.readLine();
		}
		reader.close();
		writer.close();
	}

}
