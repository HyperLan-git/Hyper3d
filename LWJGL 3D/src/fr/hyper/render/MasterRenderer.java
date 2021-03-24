package fr.hyper.render;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;

import java.awt.Color;

import javax.swing.JFrame;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import fr.hyper.entity.Entity;
import fr.hyper.main.Game;
import fr.hyper.main.MatrixEditor;
import fr.hyper.render.camera.ICamera;
import fr.hyper.render.camera.ThirdPersonCamera;
import fr.hyper.render.ligthing.Light;
import fr.hyper.render.models.Model;
import fr.hyper.render.texture.CubeMapTexture;
import fr.hyper.world.World;

public class MasterRenderer {
	public static final int MAX_LIGHTS = 6;
	public static final double TIME_TO_CHANGE = 3;

	private ICamera camera;

	private EntityRenderer entityRenderer;
	private TerrainRenderer terrainRenderer;
	private SkyRenderer skyRenderer;

	private Shader shader, guiShader;

	private World world;

	private float fogDensity = 0.007f, fogGradient = 1.5f;

	private Color fogColor = new Color(127, 127, 255);

	private MatrixEditor editor = new MatrixEditor();

	public MasterRenderer(World world, Game game, Shader shader, Shader guiShader, Shader skyShader, CubeMapTexture texture) {
		this.shader = shader;
		this.guiShader = guiShader;
		this.world = world;
		this.entityRenderer = new EntityRenderer();
		this.terrainRenderer = new TerrainRenderer();
		this.skyRenderer = new SkyRenderer(skyShader, texture);
		this.camera = new ThirdPersonCamera(game);
		JFrame window = new JFrame("Editor");
		window.setContentPane(editor);
		window.setVisible(true);
	}

	public void render() {
		renderWorld(world);
		skyRenderer.render(camera);
		guiShader.bind();
	}

	protected void renderWorld(World world) {
		prepare();
		entityRenderer.render(world.getEntities(), camera, shader);
		terrainRenderer.render(world, camera, fogColor, fogDensity, fogGradient);
	}

	private void prepare() {
		Matrix3f m = editor.getMatrix();
		double lastTransformTime = editor.getLastChangeTime();
		Matrix3f currMatrix = new Matrix3f(m);
		if(((double)System.currentTimeMillis())/1000d - lastTransformTime < TIME_TO_CHANGE) {
			double progress = (((double)System.currentTimeMillis())/1000d - lastTransformTime)/TIME_TO_CHANGE;
			m.scale((float)progress, currMatrix);
			Matrix3f temp = new Matrix3f();
			editor.getLastMatrix().scale((float)(1d-progress), temp);
			System.out.println((float)(1d-progress));
			currMatrix.add(temp);
		}
		m = currMatrix;
		Matrix4f uMatrix = new Matrix4f(
				m.m00, 	m.m01, 	m.m02,	0,
				m.m10, 	m.m11, 	m.m12,	0,
				m.m20,	m.m21,	m.m22,	0,
				0, 		0, 		0, 		1
				);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glCullFace(GL_BACK);
		shader.bind();
		shader.setUniform("userMatrix", uMatrix);
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		Vector3f[] positions = new Vector3f[MAX_LIGHTS];
		Color[] colors = new Color[MAX_LIGHTS];
		float[] lightPower = new float[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS; i++) {
			Light light = world.getLights()[i];
			if(light == null)
				continue;
			positions[i] = light.getPosition();
			colors[i] = light.getColor();
			lightPower[i] = light.getLightPower();
		}
		shader.setUniform("lightPower", lightPower);
		shader.setUniform("lightPosition", positions);
		shader.setUniform("fogGradient", fogGradient);
		shader.setUniform("fogDensity", fogDensity);
		shader.setUniform("clearColor", fogColor, false);
		shader.setUniform("lightColor", colors, false);
		

		terrainRenderer.setMatrix(uMatrix);
	}

	public ICamera getCamera() {
		return camera;
	}

	public SkyRenderer getSkyRenderer() {
		return skyRenderer;
	}

	public void registerEntityModel(Model model, Class<? extends Entity> entity) {
		this.entityRenderer.registerEntityModel(model, entity);
	}
}
