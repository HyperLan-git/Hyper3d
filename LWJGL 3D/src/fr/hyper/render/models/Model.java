package fr.hyper.render.models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import fr.hyper.render.texture.Texture;
import fr.hyper.utils.Utils;

public class Model {
	protected Texture texture;
	
	private float reflectivity, shineDamper;
	private boolean useCulling;
	
	protected int vaoID, textureID, indicesID, normalsID, draw_count;

	public Model(double[] normals, double[] vertices, double[] textureCoords, int[] indices, Texture texture, float reflectivity, float shineDamper, boolean useCulling, boolean useFakeLighting) {
		this.reflectivity = reflectivity;
		this.shineDamper = shineDamper;
		this.useCulling = useCulling;
		this.texture = texture;
		
		draw_count = indices.length;

		vaoID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vaoID);
		glBufferData(GL_ARRAY_BUFFER, Utils.createBuffer(vertices), GL_STATIC_DRAW);

		textureID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, textureID);
		glBufferData(GL_ARRAY_BUFFER, Utils.createBuffer(textureCoords), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		indicesID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.createBuffer(indices), GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		normalsID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, normalsID);
		glBufferData(GL_ARRAY_BUFFER, Utils.createBuffer(useFakeLighting?createFakeLighting(normals):normals), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private double[] createFakeLighting(double[] originalNormals) {
		double[] result = new double[originalNormals.length];
		for(int i = 0; i < result.length/3; i++) {
			result[i*3] = 0;
			result[i*3+1] = 1;
			result[i*3+2] = 0;
		}
		return result;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void bind() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		if(useCulling)
			glEnable(GL_CULL_FACE);

		glBindBuffer(GL_ARRAY_BUFFER, vaoID);
		glVertexAttribPointer(0, 3, GL_DOUBLE, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, textureID);
		glVertexAttribPointer(1, 2, GL_DOUBLE, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, normalsID);
		glVertexAttribPointer(2, 3, GL_DOUBLE, false, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID);

		texture.bind(0);
	}

	public void renderWithoutBinding() {
		glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);
	}

	public void unbind() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glDisable(GL_CULL_FACE);
	}

	public void render() {
		bind();

		renderWithoutBinding();

		unbind();
	}
}
