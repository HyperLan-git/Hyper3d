package fr.hyper.main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import java.io.IOException;

import org.joml.Vector3f;

import fr.hyper.entity.Axes;
import fr.hyper.entity.Player;
import fr.hyper.entity.Stall;
import fr.hyper.io.Window;
import fr.hyper.render.MasterRenderer;
import fr.hyper.render.Shader;
import fr.hyper.render.texture.CubeMapTexture;
import fr.hyper.render.texture.Texture;
import fr.hyper.utils.ModelLoader;
import fr.hyper.world.World;

public class Game {
	public int frameCap, frames;

	private double time = getTimeSeconds(), unprocessed = 0, frameTime = 0;

	public boolean debug;
	public World theWorld;
	public Player thePlayer;

	public Window window;

	private MasterRenderer renderer;

	public Game(Window window, int frameCap, boolean debug) {
		this.frameCap = frameCap;
		this.debug = debug;
		this.window = window;
		this.theWorld = new World();
		Shader shader = null, guiShader = null, skyShader = null;
		CubeMapTexture cubeTexture = null;
		try {
			shader = new Shader(new ResourceLocation("shaders/entities/vertexShader.txt"), new ResourceLocation("shaders/entities/fragmentShader.txt"));
			guiShader = new Shader(new ResourceLocation("shaders/vertexShader.txt"), new ResourceLocation("shaders/fragmentShader.txt"));
			skyShader = new Shader(new ResourceLocation("shaders/sky/vertexShader.txt"), new ResourceLocation("shaders/sky/fragmentShader.txt"));
			shader.bindAttribute(0, "position");
			shader.bindAttribute(1, "textures");
			shader.bindAttribute(2, "normals");

			guiShader.bindAttribute(0, "position");
			guiShader.bindAttribute(1, "textures");

			skyShader.bindAttribute(0, "position");
			ResourceLocation[] locations = new ResourceLocation[6];
			for(int i = 0; i < locations.length; i++)
				locations[i] = new ResourceLocation("textures/skybox/" + CubeMapTexture.TEXTURE_NAMES[i] + ".png");
			cubeTexture = new CubeMapTexture(locations);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.renderer = new MasterRenderer(theWorld, this, shader, guiShader, skyShader, cubeTexture);
		try {
			Texture texturePlayer = new Texture(new ResourceLocation("textures/playerTexture.png")),
					textureStall = new Texture(new ResourceLocation("textures/stallTexture.png"));
			renderer.registerEntityModel(ModelLoader.loadFromOBJ(new ResourceLocation("models/player.obj"), texturePlayer, 0f, 10, true, false), Player.class);
			renderer.registerEntityModel(ModelLoader.loadFromOBJ(new ResourceLocation("models/stall.obj"), textureStall, 0.50f, 10, true, false), Stall.class);
			renderer.registerEntityModel(ModelLoader.loadFromOBJ(new ResourceLocation("models/So_guys_we_did_it.obj"), texturePlayer, 0.50f, 10, true, false), Axes.class);
			this.thePlayer = new Player(new Vector3f(0, -5, -10), 0, 0, 0, 1, theWorld);
			new Stall(new Vector3f(), theWorld);
			new Axes(new Vector3f(10, 20, 0), theWorld);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		boolean canRender = false;
		double time2 = getTimeSeconds();
		double passed = time2 - this.time;

		this.unprocessed += passed;
		this.frameTime += passed;
		this.time = time2;

		while(this.unprocessed >= (1.0/this.frameCap)) {
			canRender = true;
			this.unprocessed -= (1.0/this.frameCap);

			handleInputs();

			this.theWorld.update();

			this.window.update();

			renderer.getCamera().update();

			if(this.frameTime >= 1.0){
				this.frameTime = 0;
				System.out.println("FPS = " + this.frames);
				this.frames = 0;
			}
		}

		if(canRender) {
			renderer.render();
			this.frames++;
		}
	}

	private void handleInputs() {
		Vector3f movement = new Vector3f();
		if(this.window.isKeyDown(GLFW_KEY_D))
			movement.add(new Vector3f(-Player.MOVESPEED, 0, 0));
		if(this.window.isKeyDown(GLFW_KEY_A))
			movement.add(new Vector3f(Player.MOVESPEED, 0, 0));
		if(this.window.isKeyDown(GLFW_KEY_W))
			movement.add(new Vector3f(0, 0, Player.MOVESPEED));
		if(this.window.isKeyDown(GLFW_KEY_S))
			movement.add(new Vector3f(0, 0, -Player.MOVESPEED));

		if(this.window.isKeyDown(GLFW_KEY_Q))
			this.thePlayer.rotate(0, Player.ROTATIONSPEED, 0);
		if(this.window.isKeyDown(GLFW_KEY_E))
			this.thePlayer.rotate(0, -Player.ROTATIONSPEED, 0);

		if(this.window.isKeyPressed(GLFW_KEY_SPACE) && !thePlayer.isFlying() && thePlayer.isOnGround())
			thePlayer.getMotion().add(0, Player.JUMP_POWER, 0);
		if(this.window.isKeyDown(GLFW_KEY_SPACE) && thePlayer.isFlying())
			movement.add(new Vector3f(0, Player.MOVESPEED, 0));
		if(this.window.isKeyDown(GLFW_KEY_LEFT_SHIFT))
			movement.add(new Vector3f(0, -Player.MOVESPEED, 0));

		thePlayer.move(movement);


		if(this.window.isKeyPressed(GLFW_KEY_ESCAPE))
			glfwSetWindowShouldClose(this.window.getID(), true);
	}

	public static double getTimeSeconds() {
		return (double)System.nanoTime() / 1000000000;
	}
}
