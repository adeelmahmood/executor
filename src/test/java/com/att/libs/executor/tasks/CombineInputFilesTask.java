package com.att.libs.executor.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

public class CombineInputFilesTask extends ExecutableTask {
	
	String folder = "input";
	String files;
	String combinedFile;
	
	@Override
	public void init(Map<String, Object> data) {
		files = data.get("files").toString();
		combinedFile = data.get("combinedFile").toString();

		if(!new File(folder).exists()){
			new File(folder).mkdir();
		}
		if(new File(folder + "/" + combinedFile).exists()){
			new File(folder + "/" + combinedFile).delete();
		}
	}

	@Override
	public void execute() throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(folder + "/" + combinedFile));
		String[] filesTokens = files.split("_");
		for(String file : filesTokens){
			BufferedReader reader = new BufferedReader(new FileReader(folder + "/" + file));
			String line = reader.readLine();
			while(line != null){
				writer.write(line);
				writer.newLine();
				line = reader.readLine();
			}
			reader.close();
		}
		writer.close();
	}

}
