package fr.hyper.render.texture;

import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import fr.hyper.main.ResourceLocation;

public abstract class RawTexture {
	protected TextureData load(ResourceLocation location) throws IOException {
		IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1), comp = BufferUtils.createIntBuffer(1);

		InputStream img = location.getAsStream();
		int size = 1;
		while(img.read() != -1)
			size++;
		img = location.getAsStream();
		byte[] imageData = new byte[size];
		img.read(imageData);
		ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
		imageBuffer.put(imageData);
		imageBuffer.flip();

		ByteBuffer image = stbi_load_from_memory(imageBuffer, width, height, comp, 4);

		int w = width.get(), h = height.get();
		return new TextureData(w, h, image);
	}
}
