package fr.hyper.world;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector3f;

import fr.hyper.main.ResourceLocation;
import fr.hyper.render.models.Model;
import fr.hyper.render.texture.Texture;
import fr.hyper.utils.Utils;

public class Terrain {
	public static final float SIZE = 200;
	public static final int VERTEX_COUNT = 128;

	public static final float MAX_HEIGHT = 40,
			MIN_HEIGHT = -10;

	private Model model;

	private float[][] heights = new float[VERTEX_COUNT][VERTEX_COUNT];

	private float x, z;

	public Terrain(int gridX, int gridZ, Texture blendMap, ResourceLocation heightMap) throws IOException {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		BufferedImage image = ImageIO.read(heightMap.getAsStream());
		int vertexCount = image.getWidth();
		generateTerrain(blendMap, image, vertexCount);
	}

	private float getHeight(int x, int y, BufferedImage image) {
		if(x < 0 || x > image.getWidth() || y < 0 || y > image.getWidth())
			return -10;
		float height = image.getRGB(x, y);
		float maxPixelColor = 256f*256f*256f;
		height += maxPixelColor;
		height /= maxPixelColor;
		height *= (MAX_HEIGHT-MIN_HEIGHT);
		height += MIN_HEIGHT;
		return height;
	}

	private Vector3f calculateNormal(int x, int y, BufferedImage image) {
		float heightL = getHeight(x-1, y, image),
				heightR = getHeight(x+1, y, image),
				heightU = getHeight(x, y+1, image),
				heightD = getHeight(x, y-1, image);
		return new Vector3f(heightL - heightR, 2f, heightD - heightU).normalize();
	}

	public Model getModel() {
		return model;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public float getHeight(float posX, float posZ) {
		float terrainX = posX - x, terrainZ = posZ - z,
				gridSquareSize = SIZE / (heights.length - 1f);
		int gridX = (int) Math.floor(terrainX/gridSquareSize),
				gridZ = (int) Math.floor(terrainZ/gridSquareSize);
		if(gridX < 0 || gridX > heights.length || gridZ < 0 || gridZ > heights.length)
			return 0;
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize,
				zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		
		if (xCoord <= (1-zCoord)) 
			return barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
					heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		else
			return barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
					heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
	}

	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	private void generateTerrain(Texture blendMap, BufferedImage heightMap, int vertexCount) {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, heightMap);
				vertices[vertexPointer*3+1] = height;
				heights[j][i] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, heightMap);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		this.model = new Model(Utils.convert(normals), Utils.convert(vertices),
				Utils.convert(textureCoords), indices, blendMap, 0f, 10, true, false);
	}
}
