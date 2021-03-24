package fr.hyper.utils;

import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3i;

import fr.hyper.main.ResourceLocation;
import fr.hyper.render.models.Model;
import fr.hyper.render.texture.Texture;

public class ModelLoader {
	public static final Model loadFromOBJ(ResourceLocation location, Texture texture, float reflectivity, float shineDamper, boolean useCulling, boolean useFakeLighting) throws IOException {
		String file = Utils.read(location);
		String[] str = file.split("\n");

		ArrayList<Vector3d> vertices = new ArrayList<>();
		ArrayList<Vector2d> textureCoords = new ArrayList<>();
		ArrayList<Vector3d> normals = new ArrayList<>();
		ArrayList<Vector3i> indices = new ArrayList<>();

		double[] finalVertices = null, finalTextureCoords = null, finalNormals = null;

		for(int i = 0; i < str.length; i++) {
			String line = str[i];
			String[] numbers = line.split(" ");
			if(line.startsWith("v "))
				//We have a vertex line !
				vertices.add(new Vector3d(Double.valueOf(numbers[1]), Double.valueOf(numbers[2]), Double.valueOf(numbers[3])));
			else if(line.startsWith("vt "))
				//We have a texture coordinate !
				textureCoords.add(new Vector2d(Double.valueOf(numbers[1]), Double.valueOf(numbers[2])));
			else if(line.startsWith("vn "))
				//We have a normal vector !
				normals.add(new Vector3d(Double.valueOf(numbers[1]), Double.valueOf(numbers[2]), Double.valueOf(numbers[3])));
			else if(line.startsWith("f ")) {
				if(finalTextureCoords == null) {
					finalTextureCoords = new double[vertices.size()*4];
					finalNormals = new double[vertices.size()*6];
					finalVertices = new double[vertices.size()*6];
				}
				for(int j = 1; j < numbers.length; j++) {
					int[] n = new int[3];
					String[] values = numbers[j].split("/");
					for(int k = 0; k < 3; k++)
						n[k] = Integer.valueOf(values[k]);
					int l = n[0]-1;
					Vector2d texCoords = textureCoords.get(n[1]-1);
					Vector3d vCoords = vertices.get(n[0]-1), normal = normals.get(n[2]-1);

					finalVertices[l*3] = vCoords.x;
					finalVertices[l*3+1] = vCoords.y;
					finalVertices[l*3+2] = vCoords.z;

					finalTextureCoords[l*2] = texCoords.x;
					finalTextureCoords[l*2+1] = 1 - texCoords.y;

					finalNormals[l*3] = normal.x;
					finalNormals[l*3+1] = normal.y;
					finalNormals[l*3+2] = normal.z;

					indices.add(new Vector3i(n[0]-1, n[1]-1, n[2]-1));
				}
			}
		}
		int[] finalIndices = new int[indices.size()];
		for(int i = 0; i < indices.size(); i++)
			finalIndices[i] = indices.get(i).x;
		return new Model(finalNormals, finalVertices, finalTextureCoords, finalIndices, texture, reflectivity, shineDamper, useCulling, useFakeLighting);
	}
}
