package fr.hyper.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;

import fr.hyper.render.texture.Texture;
import fr.hyper.utils.Utils;

public class Gui {
	public static final DoubleBuffer TEXTURE_COORDS = Utils.createBuffer(new double[]{0,0,1,0,0,1,1,1});
	public static final IntBuffer INDICES = Utils.createBuffer(new int[]{0,1,2,3});

	protected int vaoID, textureID, indicesID, drawCount;

	protected Vector2f position = new Vector2f();

	protected Texture texture;
	
	protected Gui() {	}

	public Gui(Texture texture, float x, float y, float width, float height) {
		drawCount = 4;
		vaoID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vaoID);
		glBufferData(GL_ARRAY_BUFFER, Utils.createBuffer(new double[]{x, y + height, x, y, x + width, y + height, x + width, y}),
				GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		textureID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, textureID);
		glBufferData(GL_ARRAY_BUFFER, TEXTURE_COORDS, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);


		indicesID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, INDICES, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		this.texture = texture;
	}

	public void render() {
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vaoID);
		glVertexAttribPointer(0, 2, GL_DOUBLE, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, textureID);
		glVertexAttribPointer(1, 2, GL_DOUBLE, false, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID);

		texture.bind(0);

		glDrawElements(GL_TRIANGLE_STRIP, drawCount, GL_UNSIGNED_INT, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
	}

	public Vector2f getPosition() {
		return position;
	}

	public Gui setPosition(Vector2f position) {
		this.position = position;
		return this;
	}
}
