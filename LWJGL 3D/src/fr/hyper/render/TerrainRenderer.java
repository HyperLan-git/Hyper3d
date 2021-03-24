package fr.hyper.render;

import java.awt.Color;
import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import fr.hyper.main.ResourceLocation;
import fr.hyper.render.camera.ICamera;
import fr.hyper.render.ligthing.Light;
import fr.hyper.render.models.Model;
import fr.hyper.render.texture.Texture;
import fr.hyper.utils.Utils;
import fr.hyper.world.Terrain;
import fr.hyper.world.World;

public class TerrainRenderer {
	private Shader shader;

	private Texture rTexture, gTexture, bTexture, darkTexture;

	private Matrix4f matrix;

	public TerrainRenderer() {
		try {
			shader = new Shader(new ResourceLocation("shaders/terrain/vertexShader.txt"), new ResourceLocation("shaders/terrain/fragmentShader.txt"));
			rTexture = new Texture(new ResourceLocation("terrain/mud.png"));
			gTexture = new Texture(new ResourceLocation("terrain/grassy2.png"));
			bTexture = new Texture(new ResourceLocation("terrain/path.png"));
			darkTexture = new Texture(new ResourceLocation("textures/checker.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(World world, ICamera camera, Color fogColor, float fogDensity, float fogGradient) {
		shader.bind();
		Vector3f[] positions = new Vector3f[MasterRenderer.MAX_LIGHTS];
		Color[] colors = new Color[MasterRenderer.MAX_LIGHTS];
		float[] lightPowers = new float[MasterRenderer.MAX_LIGHTS];
		for(int i = 0; i < MasterRenderer.MAX_LIGHTS; i++) {
			Light light = world.getLights()[i];
			if(light == null)
				continue;
			positions[i] = light.getPosition();
			colors[i] = light.getColor();
			lightPowers[i] = light.getLightPower();
		}
		shader.setUniform("userMatrix", matrix);
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		shader.setUniform("lightPosition", positions);
		shader.setUniform("lightPower", lightPowers);
		shader.setUniform("blendMap", 0);
		shader.setUniform("rTexture", 1);
		shader.setUniform("gTexture", 2);
		shader.setUniform("bTexture", 3);
		shader.setUniform("darkTexture", 4);
		shader.setUniform("lightColor", colors, false);
		shader.setUniform("clearColor", fogColor, false);
		shader.setUniform("fogGradient", fogGradient);
		shader.setUniform("fogDensity", fogDensity);
		rTexture.bind(1);
		gTexture.bind(2);
		bTexture.bind(3);
		darkTexture.bind(4);
		for(Terrain terrain : world.getTerrains()) {
			Model model = terrain.getModel();

			Matrix4f transformationMatrix = Utils.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
			shader.setUniform("transformationMatrix", transformationMatrix);

			shader.setUniform("reflectivity", model.getReflectivity());
			shader.setUniform("shineDamper", model.getShineDamper());

			model.render();
		}

		Shader.unbindShader();
	}

	public Shader getShader() {
		return shader;
	}

	public void setMatrix(Matrix4f matrix4f) {
		this.matrix = matrix4f;
	}
}
