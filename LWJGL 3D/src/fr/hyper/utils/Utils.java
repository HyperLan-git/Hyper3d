package fr.hyper.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import fr.hyper.main.ResourceLocation;
import fr.hyper.render.camera.FreeCamera;

public class Utils {
	public static final Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rx), 1.0f, 0.0f, 0.0f);
		matrix.rotate((float) Math.toRadians(ry), 0.0f, 1.0f, 0.0f);
		matrix.rotate((float) Math.toRadians(rz), 0.0f, 0.0f, 1.0f);
		matrix.scale(scale);
		return matrix;
	}
	
	public static final Matrix4f createProjectionMatrix(float aspectRatio, float fov, float Zfar, float Znear) {
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = Zfar - Znear;
        
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((Zfar + Znear) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * Znear * Zfar) / frustum_length));
        projectionMatrix.m33(0);
		return projectionMatrix;
	}
	
	public static final Matrix4f createViewMatrix(FreeCamera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float)Math.toRadians(camera.getRotX()), 1.0f, 0, 0);
        viewMatrix.rotate((float)Math.toRadians(camera.getRotY()), 0, 1.0f, 0);
        viewMatrix.rotate((float)Math.toRadians(camera.getRotZ()), 0, 0, 1.0f);
        viewMatrix.translate(new Vector3f(camera.getPosition().x()*-1, camera.getPosition().y()*-1, camera.getPosition().z()*-1));
		return viewMatrix;
	}
	
	public static final double[] convert(float[] array) {
		double[] result = new double[array.length];
		for(int i = 0; i < array.length; i++)
			result[i] = (double)array[i];
		return result;
	}

	public static final String read(ResourceLocation file) throws IOException {
		String str = "";
		InputStream is = file.getAsStream();
		StringBuffer buf = new StringBuffer();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			if (is != null)                
				while ((str = reader.readLine()) != null)   
					buf.append(str + "\n" );
		} finally {
			is.close();
		}
		return buf.toString();
	}

	public static IntBuffer createBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static DoubleBuffer createBuffer(double[] data) {
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer createBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer createBuffer(Float[] data) {
		float[] clone = new float[data.length];
		for(int i = 0; i < data.length; i++)
			clone[i] = data[i];
		return createBuffer(clone);
	}
}
