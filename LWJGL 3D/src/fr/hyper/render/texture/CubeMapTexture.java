package fr.hyper.render.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;

import java.io.IOException;

import fr.hyper.main.ResourceLocation;
import fr.hyper.render.SkyRenderer;

public class CubeMapTexture extends RawTexture {
	public static final String[] TEXTURE_NAMES = {
			"right", "left",
			"top", "bottom",
			"back", "front"
	};

	private int id;

	public CubeMapTexture(ResourceLocation[] locations) throws IOException {
		id = glGenTextures();

		glBindTexture(GL_TEXTURE_CUBE_MAP, id);
		
		for(int i = 0; i < locations.length; i++) {
			TextureData data = this.load(locations[i]);
			
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, (int)SkyRenderer.SIZE, (int)SkyRenderer.SIZE,
					0, GL_RGBA, GL_UNSIGNED_INT, data.getImage());
			
			stbi_image_free(data.getImage());
		}

		glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	}

	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, id);
	}
}
