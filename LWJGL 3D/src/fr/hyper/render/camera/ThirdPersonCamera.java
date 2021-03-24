package fr.hyper.render.camera;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import fr.hyper.io.ScrollListener;
import fr.hyper.main.Game;
import fr.hyper.utils.Utils;

public class ThirdPersonCamera implements ICamera, ScrollListener {
	public static final Matrix4f MATRIX = new Matrix4f(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1);

	private float fov = 70, Znear = 0.01f, Zfar = 1000, dist = 20;

	private float rotX = 0, rotY = 0, rotZ = 0;
	double lastMouseX = 0, lastMouseY = 0, lastWheel = 0;

	private Matrix4f projectionMatrix, viewMatrix;

	private Game game;

	public ThirdPersonCamera(Game game) {
		this.game = game;
		game.window.addScrollListener(this);
	}

	@Override
	public void update() {
		if(game.window.isMousePressed(GLFW_MOUSE_BUTTON_2)) {
			glfwSetInputMode(game.window.getID(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
			DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(game.window.getID(), x, y);
			lastMouseX = x.get();
			lastMouseY = y.get();
		}

		if(game.window.isMouseReleased(GLFW_MOUSE_BUTTON_2)){
			glfwSetInputMode(game.window.getID(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}

		if(game.window.isMouseDown(GLFW_MOUSE_BUTTON_2)) {
			DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(game.window.getID(), x, y);
			double currentX = x.get(), currentY = y.get();
			double deltaX = currentX - lastMouseX, deltaY = currentY - lastMouseY;
			rotY += deltaX;
			rotX += deltaY;
			if(rotX > 90)
				rotX = 90;

			if(rotX < -90)
				rotX = -90;

			lastMouseX = currentX;
			lastMouseY = currentY;
		}

		float playerRotY = 180-game.thePlayer.getRotationY();

		this.projectionMatrix = Utils.createProjectionMatrix(game.window.getWidth()/game.window.getHeight(), fov, Zfar, Znear);
		Vector3f pos = new Vector3f(), translation = new Vector3f(0, 0, -dist);
		pos.add(game.thePlayer.getPosition());
		pos.mul(-1);
		this.viewMatrix = new Matrix4f().identity();
		if(rotX % 360f != 0) {
			viewMatrix.rotate((float)Math.toRadians(rotX), 1, 0, 0);
			translation.rotateAxis((float)Math.toRadians(this.rotX), -1, 0, 0).normalize().mul(dist);
		}
		if(playerRotY + this.rotY % 360f != 0) {
			viewMatrix.rotate((float)Math.toRadians(playerRotY + this.rotY), 0, 1, 0);
			translation.rotateAxis((float)Math.toRadians(playerRotY + this.rotY), 0, -1, 0).normalize().mul(dist);
		}
		if(rotZ % 360f != 0)
			viewMatrix.rotate((float)Math.toRadians(rotZ), 0, 0, 1);
		viewMatrix.translate(pos).translate(translation.x, translation.y-5, translation.z);
	}

	@Override
	public Matrix4f getViewMatrix() {
		return MATRIX.mul(viewMatrix, new Matrix4f());
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public void onScrolled(double xoffset, double yoffset) {
		this.dist -= yoffset*2.0;
		if(dist < 10)
			dist = 10;
		if(dist > 30)
			dist = 30;
	}

}
