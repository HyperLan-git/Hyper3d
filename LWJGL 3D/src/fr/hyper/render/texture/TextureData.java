package fr.hyper.render.texture;

import java.nio.ByteBuffer;

public class TextureData {
	private int width, height;
	
	private ByteBuffer image;

	public TextureData(int width, int height, ByteBuffer image) {
		this.width = width;
		this.height = height;
		this.image = image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getImage() {
		return image;
	}
}
