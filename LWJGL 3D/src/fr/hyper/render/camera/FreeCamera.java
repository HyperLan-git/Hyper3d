package fr.hyper.render.camera;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import fr.hyper.main.Game;
import fr.hyper.utils.Utils;

public class FreeCamera implements ICamera {
	public float fov = 120, Znear = 0.1f, Zfar = 1000;

	private Vector3f position;
	private float rotX, rotY, rotZ, movespeed = 0.1f;
	private Matrix4f projectionMatrix, viewMatrix;

	private Game game;

	public FreeCamera(Game game) {
		this.game = game;
		this.position = new Vector3f(0,1,0);
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void update() {
		//TODO remove debug
		Vector3f translation = new Vector3f();
		if(game.window.isKeyDown(GLFW_KEY_D))
			translation.add(new Vector3f(1f, 0, 0));
		if(game.window.isKeyDown(GLFW_KEY_A))
			translation.add(new Vector3f(-1f, 0, 0));
		if(game.window.isKeyDown(GLFW_KEY_S))
			translation.add(new Vector3f(0, 0, 1f));
		if(game.window.isKeyDown(GLFW_KEY_W))
			translation.add(new Vector3f(0, 0, -1f));
		if(game.window.isKeyDown(GLFW_KEY_Q))
			this.rotate(0, -2f, 0);
		if(game.window.isKeyDown(GLFW_KEY_E))
			this.rotate(0, 2f, 0);
		if(game.window.isKeyDown(GLFW_KEY_Z))
			this.fov--;
		if(game.window.isKeyDown(GLFW_KEY_X))
			this.rotate(0, 0, 2);
		if(rotY != 0)
			translation.rotateAxis((float)Math.toRadians(rotY), 0, -1f, 0);
		if(translation.length() > 0)
			translation.normalize().mul(movespeed);
		if(game.window.isKeyDown(GLFW_KEY_LEFT_SHIFT))
			translation.mul(10);
		translate(translation);
		this.projectionMatrix = Utils.createProjectionMatrix(game.window.getWidth()/game.window.getHeight(), fov, Zfar, Znear);
		this.viewMatrix = Utils.createViewMatrix(this);
	}

	private void rotate(float rotX, float rotY, float rotZ) {
		this.rotX += rotX;
		this.rotY += rotY;
		this.rotZ += rotZ;
	}

	private void translate(Vector3f vector3f) {
		this.position.add(vector3f);
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}
}
