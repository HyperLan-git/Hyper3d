package fr.hyper.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import fr.hyper.render.camera.ICamera;
import fr.hyper.render.texture.CubeMapTexture;
import fr.hyper.utils.Utils;

public class SkyRenderer {
	private static final int VERTEX_COUNT = 1;
	private static final int[] INDICES = {};
	public static final float SIZE = 0.5f;
	private Shader shader;

	private CubeMapTexture texture;

	private int vaoID, indicesID;

	public SkyRenderer(Shader shader, CubeMapTexture texture) {
		this.shader = shader;
		this.texture = texture;

		vaoID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vaoID);
		glBufferData(GL_ARRAY_BUFFER, Utils.createBuffer(getVertexPositions(SIZE)), GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		indicesID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.createBuffer(INDICES), GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void render(ICamera camera) {
		shader.bind();
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		texture.bind();

		glBindBuffer(GL_ARRAY_BUFFER, vaoID);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID);

		glDrawElements(GL_TRIANGLES, VERTEX_COUNT, GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
	}

	private static float[] getVertexPositions(float size) {
		return new float[] {
				-size, size, size,
				size, size, size,
				size, -size, size,
				-size, -size, size,
				-size, size, -size,
				size, size, -size,
				size, -size, -size,
				-size, -size, -size};
	}
}
