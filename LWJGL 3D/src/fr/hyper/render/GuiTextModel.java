package fr.hyper.render;

import static org.lwjgl.opengl.GL15.*;

import java.util.ArrayList;
import java.util.List;

import fr.hyper.utils.Utils;

public class GuiTextModel extends Gui {
    public static final double LINE_HEIGHT = 0.03f;

	public GuiTextModel(String text, FontData data, double size, boolean centered) {
		super();
		createModel(text, data, centered, size);
		this.texture = data.getTexture();
	}

	private void createModel(String text, FontData data, boolean centered, double size) {
		float x = 0, y = 0;
		ArrayList<Float> vertices = new ArrayList<>(), textures = new ArrayList<>();
		for(String line : text.split("\n")) {
			for(Character c : line.toCharArray()) {
				FontCharacter character = data.getCharacter((int)c);
				addCharacter(x, y, character, size, vertices, textures);
				x += character.getXoff() * size;
			}
			y += LINE_HEIGHT * size;
		}
		vaoID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vaoID);
		glBufferData(GL_ARRAY_BUFFER, Utils.createBuffer(vertices.toArray(new Float[vertices.size()])), GL_STATIC_DRAW);

		textureID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, textureID);
		glBufferData(GL_ARRAY_BUFFER, Utils.createBuffer(textures.toArray(new Float[textures.size()])), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		int[] indices = new int[vertices.size()];
		for(int i = 0; i < indices.length; i++)
			indices[i] = i;
		indicesID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.createBuffer(indices), GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	private void addCharacter(double curserX, double curserY, FontCharacter character, double fontSize,
			List<Float> vertices, List<Float> textures) {
		double x = curserX + (character.getXoff() * fontSize);
		double y = curserY + (character.getYoff() * fontSize);
		double maxX = x + (character.getWidth() * fontSize);
		double maxY = y + (character.getHeight() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		addCoords(vertices, properX, properY, properMaxX, properMaxY);
		addCoords(textures, character.getX(), character.getY(),
				character.getWidth(), character.getHeight());
	}

	private static void addCoords(List<Float> array, double x, double y, double maxX, double maxY) {
		array.add((float) x);
		array.add((float) y);
		array.add((float) x);
		array.add((float) maxY);
		array.add((float) maxX);
		array.add((float) maxY);
		array.add((float) maxX);
		array.add((float) maxY);
		array.add((float) maxX);
		array.add((float) y);
		array.add((float) x);
		array.add((float) y);
	}
}
