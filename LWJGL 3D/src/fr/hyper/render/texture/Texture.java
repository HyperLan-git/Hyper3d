package fr.hyper.render.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;

import java.io.IOException;
import java.nio.ByteBuffer;

import fr.hyper.main.ResourceLocation;

public class Texture extends RawTexture {
	private int id;

	public Texture(ResourceLocation location) throws IOException {
		this.id = glGenTextures();

		TextureData data = this.load(location);
		ByteBuffer image = data.getImage();

		glBindTexture(GL_TEXTURE_2D, id);

		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		stbi_image_free(image);
	}

	public void bind(int sampler) {
		if(sampler < 0 || sampler > 31)
			throw new IllegalArgumentException("Invalid sampler id !");
		glActiveTexture(GL_TEXTURE0 + sampler);
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public int getID() {
		return this.id;
	}
}
