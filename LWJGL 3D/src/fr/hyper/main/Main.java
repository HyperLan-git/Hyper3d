package fr.hyper.main;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL13;

import fr.hyper.io.Window;

public class Main {
	public static void main(String[] args) {
		if(!glfwInit()) {
			System.err.println("Error initializing GLFW");
			System.exit(1);
		}

		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		Window window = new Window(1280, 850, "Gayme");
		
		
		GL.createCapabilities();

		glClearColor(0.5f, 0.5f, 1, 1);

		Game game = new Game(window, 60, true);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL13.GL_TEXTURE_CUBE_MAP);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		while(!window.shouldClose())
			game.update();

		GL.destroy();

		glfwTerminate();
	}
}
