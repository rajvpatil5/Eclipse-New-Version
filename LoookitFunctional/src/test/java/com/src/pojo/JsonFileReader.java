package com.src.pojo;

import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileReader {
	public Settings userValues;
	public NodeSettings nodeValues;

	public void readJson() {
		try {
			// create object mapper instance
			ObjectMapper mapper = new ObjectMapper();
			// convert JSON file to map
			userValues = mapper.readValue(Paths.get("Settings.json").toFile(), Settings.class);
			nodeValues = mapper.readValue(Paths.get("Nodes.json").toFile(), NodeSettings.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
