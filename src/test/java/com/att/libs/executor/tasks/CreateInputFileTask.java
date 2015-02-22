package com.att.libs.executor.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class CreateInputFileTask extends ExecutableTask {
	
	String folder = "input";
	String file;
	int start;
	int end;
	
	@Override
	public void init(Map<String, Object> data) {
		file = data.get("file").toString();
		start = Integer.parseInt(data.get("start").toString());
		end = Integer.parseInt(data.get("end").toString());
		
		if(!new File(folder).exists()){
			new File(folder).mkdir();
		}
		if(new File(folder + "/" + file).exists()){
			new File(folder + "/" + file).delete();
		}
	}

	@Override
	public void execute() throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(folder + "/" + file));
		for(int i=start; i<=end; i++){
			writer.write("Line " + i);
			writer.newLine();
		}
		writer.close();
	}

}
