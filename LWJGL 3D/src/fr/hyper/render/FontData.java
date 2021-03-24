package fr.hyper.render;

import java.io.IOException;
import java.util.HashMap;

import fr.hyper.main.ResourceLocation;
import fr.hyper.render.texture.Texture;
import fr.hyper.utils.Utils;

public class FontData {
	private String fontName;

	private HashMap<Integer, FontCharacter> charMap = new HashMap<>();
	
	private Texture texture;

	public FontData(ResourceLocation fontModel, ResourceLocation fontImage) {
		try {
			readFontModel(fontModel);
			texture = new Texture(fontImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GuiTextModel createModel(String text, double size, boolean centered) {
		return new GuiTextModel(text, this, size, centered);
	}
	
	public FontCharacter getCharacter(Integer code) {
		return charMap.get(code);
	}

	private void readFontModel(ResourceLocation fontModel) throws IOException {
		String model = Utils.read(fontModel);
		for(String line : model.split("\n")) {
			Integer currentCode = null, x = null, y = null, width = null, height = null, xoff = null, yoff = null, xadv = null;
			String[] words = line.split(" ");
			for(String word : words) {
				String[] str = word.split("=");
				if(word.startsWith("face="))
					fontName = str[1];
				if(word.startsWith("id="))
					currentCode = Integer.valueOf(str[1]);
				if(word.startsWith("x="))
					x = Integer.valueOf(str[1]);
				if(word.startsWith("y="))
					y = Integer.valueOf(str[1]);
				if(word.startsWith("width="))
					width = Integer.valueOf(str[1]);
				if(word.startsWith("height="))
					height = Integer.valueOf(str[1]);
				if(word.startsWith("xoffset="))
					xoff = Integer.valueOf(str[1]);
				if(word.startsWith("yoffset="))
					yoff = Integer.valueOf(str[1]);
				if(word.startsWith("xadvance=")) {
					xadv = Integer.valueOf(str[1]);
					charMap.put(currentCode, new FontCharacter(currentCode, x, y, width, height, xoff, yoff, xadv));
				}
			}
		}
	}

	public String getFontName() {
		return fontName;
	}

	public Texture getTexture() {
		return texture;
	}
}
