package fr.hyper.render;

import static org.lwjgl.opengl.GL20.*;

import java.awt.Color;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import fr.hyper.main.ResourceLocation;
import fr.hyper.utils.Utils;

public class Shader {
	private int program, vs, fs;

	public Shader(ResourceLocation vertexShader, ResourceLocation fragmentShader) throws IOException {
		program = glCreateProgram();
		vs = glCreateShader(GL_VERTEX_SHADER);
		fs = glCreateShader(GL_FRAGMENT_SHADER);

		glShaderSource(vs, Utils.read(vertexShader));
		glShaderSource(fs, Utils.read(fragmentShader));

		glCompileShader(vs);
		glCompileShader(fs);

		if(glGetShaderi(vs, GL_COMPILE_STATUS) != 1)
			throw new IllegalStateException("Shader not compiled ! Reason : " + glGetShaderInfoLog(vs));

		if(glGetShaderi(fs, GL_COMPILE_STATUS) != 1)
			throw new IllegalStateException("Shader not compiled ! Reason : " + glGetShaderInfoLog(fs));

		glAttachShader(program, vs);
		glAttachShader(program, fs);

		glLinkProgram(program);
		if(glGetProgrami(program, GL_LINK_STATUS) != 1)
			throw new IllegalStateException("Shader not linked ! Reason : " + glGetProgramInfoLog(program));

		glValidateProgram(program);
		if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1)
			throw new IllegalStateException("Shader not linked ! Reason : " + glGetProgramInfoLog(program));
	}


	public void bindAttribute(int attribute, String variableName) {
		glBindAttribLocation(program, attribute, variableName);
	}

	public void bind() {
		glUseProgram(program);
	}

	public static void unbindShader() {
		glUseProgram(0);
	}

	public void delete() {
		unbindShader();
		glDetachShader(program, vs);
		glDetachShader(program, fs);
		glDeleteShader(vs);
		glDeleteShader(fs);
		glDeleteProgram(program);
	}

	public void setUniform(String name, int value) {
		int location = glGetUniformLocation(program, name);
		if(location != -1)
			glUniform1i(location, value);
	}

	public void setUniform(String name, boolean value) {
		setUniform(name, value?1:0);
	}

	public void setUniform(String name, float value) {
		int location = glGetUniformLocation(program, name);
		if(location != -1)
			glUniform1f(location, value);
	}

	public void setUniform(String name, Vector3f value) {
		int location = glGetUniformLocation(program, name);
		if(location != -1)
			glUniform3f(location, value.x, value.y, value.z);
	}

	public void setUniform(String name, Vector4f value) {
		int location = glGetUniformLocation(program, name);
		if(location != -1)
			glUniform4f(location, value.x, value.y, value.z, value.w);
	}

	public void setUniform(String name, Color value, boolean withAlpha) {
		if(withAlpha)
			setUniform(name, new Vector4f(value.getRed()/255.0f, value.getGreen()/255.0f,
					value.getBlue()/255.0f, value.getAlpha()/255.0f));
		else
			setUniform(name, new Vector3f(value.getRed()/255.0f, value.getGreen()/255.0f,
					value.getBlue()/255.0f));
	}

	public void setUniform(String name, Matrix4f value) {
		int location = glGetUniformLocation(program, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		value.get(buffer);
		if(location != -1)
			glUniformMatrix4fv(location, false, buffer);
	}


	public void setUniform(String name, Vector3i value) {
		int location = glGetUniformLocation(program, name);
		if(location != -1)
			glUniform3i(location, value.x, value.y, value.z);
	}


	public void setUniform(String name, Color[] values, boolean withAlpha) {
		for(int i = 0; i < values.length; i++) {
			Color color = values[i];
			String str = name + "[" + i + "]";
			if(color == null)
				continue;
			if(withAlpha)
				setUniform(str, new Vector4f(color.getRed()/255.0f, color.getGreen()/255.0f,
						color.getBlue()/255.0f, color.getAlpha()/255.0f));
			else
				setUniform(str, new Vector3f(color.getRed()/255.0f, color.getGreen()/255.0f,
						color.getBlue()/255.0f));
		}
	}


	public void setUniform(String name, Vector3f[] values) {
		for(int i = 0; i < values.length; i++) {
			Vector3f value = values[i];
			if(value == null)
				continue;
			int location = glGetUniformLocation(program, name + "[" + i + "]");
			if(location != -1)
				glUniform3f(location, value.x, value.y, value.z);
		}
	}


	public void setUniform(String name, float[] values) {
		for(int i = 0; i < values.length; i++) {
			float value = values[i];
			int location = glGetUniformLocation(program, name + "[" + i + "]");
			if(location != -1)
				glUniform1f(location, value);
		}
	}
}
