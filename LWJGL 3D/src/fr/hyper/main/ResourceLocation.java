package fr.hyper.main;

import java.io.InputStream;

public class ResourceLocation {
	public static final String ressourceFile = "/fr/hyper/ressources/";
	
	private String path;
	
	public ResourceLocation(String file) {
		this.path = ressourceFile + file;
	}
	
	public InputStream getAsStream() {
		return ResourceLocation.class.getResourceAsStream(path);
	}
}
