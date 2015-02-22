package com.att.libs.executor.utils;

import com.google.gson.Gson;

/**
 * JsonUtils
 * 
 * @author aq728y
 *
 */
public class JsonUtils {

	private static Gson gson;
	
	public static Gson getGson(){
		if(gson == null){
			gson = new Gson();
		}
		return gson;
	}
}
