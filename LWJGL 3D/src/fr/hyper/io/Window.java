package fr.hyper.io;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFWScrollCallback;

public class Window {
	private long id;
	private int width, height;

	private boolean[] keys = new boolean[GLFW_KEY_LAST-32], mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];

	private HashMap<String, ArrayList<KeyBindingListener>> listeners = new HashMap<>();
	
	private ArrayList<ScrollListener> scrollListeners = new ArrayList<>();

	public Window(int width, int height, String title) {
		this(width, height, title, 0l, 0l);
	}

	public Window(String title) {
		this(glfwGetVideoMode(glfwGetPrimaryMonitor()).width(), glfwGetVideoMode(glfwGetPrimaryMonitor()).height(), title, glfwGetPrimaryMonitor(), 0l);
	}

	private Window(int width, int height, String title, long monitor, long share) {
		this.id = glfwCreateWindow(width, height, title, monitor, share);
		this.width = width;
		this.height = height;

		if(id == 0)
			throw new IllegalStateException("Failed to create a window !");

		glfwSwapInterval(1);

		glfwMakeContextCurrent(id);

		glfwShowWindow(id);

		for(int i = 0; i < GLFW_KEY_LAST-32; i++)
			keys[i] = false;

		for(int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++)
			mouseButtons[i] = false;
		
		glfwSetScrollCallback(id, new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				if(window == id)
					onScrolled(xoffset, yoffset);
			}
		});
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(id);
	}

	public long getID(){
		return id;
	}

	public void update() {
		glfwSwapBuffers(id);
		for(int i = 32; i < GLFW_KEY_LAST; i++)
			this.keys[i-32] = isKeyDown(i);

		for(int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++)
			mouseButtons[i] = isMouseDown(i);
		glfwPollEvents();

		for(String binding : this.listeners.keySet()) {
			ArrayList<KeyBindingListener> a = this.listeners.get(binding);
			for(Integer key : KeyBindings.getAllKeysFor(binding)) {
				if(isKeyPressed(key)) {
					for(KeyBindingListener listener : a)
						listener.onKeyPressed(binding);
					break;
				}
				if(isKeyDown(key)) {
					for(KeyBindingListener listener : a)
						listener.onKeyDown(binding);
					break;
				}
				if(isKeyReleased(key)) {
					for(KeyBindingListener listener : a)
						listener.onKeyReleased(binding);
					break;
				}
			}
		}
	}

	public boolean isKeyPressed(int key) {
		return isKeyDown(key) && !keys[key-32];
	}

	public boolean isKeyDown(int key) {
		return glfwGetKey(id, key) == GLFW_TRUE;
	}

	public boolean isKeyReleased(int key) {
		return !isKeyDown(key) && keys[key-32];
	}

	public boolean isMouseDown(int mouseButton) {
		return glfwGetMouseButton(id, mouseButton) == GLFW_TRUE;
	}

	public boolean isMousePressed(int mouseButton) {
		return glfwGetMouseButton(id, mouseButton) == GLFW_TRUE && !mouseButtons[mouseButton];
	}

	public boolean isMouseReleased(int mouseButton) {
		return !isMouseDown(mouseButton) && mouseButtons[mouseButton];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	private void onScrolled(double xoffset, double yoffset) {
		for(ScrollListener listener : scrollListeners)
			listener.onScrolled(xoffset, yoffset);
	}
	
	public void addScrollListener(ScrollListener listener) {
		scrollListeners.add(listener);
	}

	public static class KeyBindings {
		private static HashMap<Integer, String> keyMap = new HashMap<>();

		public static void registerKeyBinding(Integer key, String bindingName) {
			keyMap.put(key, bindingName);
		}

		public static void deleteKeyBinding(Integer key) {
			keyMap.remove(key);
		}

		public static void deleteAllKeyBindings(String bindingName) {
			if(!keyMap.containsValue(bindingName))
				return;
			for(int i : getAllKeysFor(bindingName))
				deleteKeyBinding(i);
		}

		public static Integer[] getAllKeysFor(String bindingName) {
			ArrayList<Integer> result = new ArrayList<>();
			for(Entry<Integer, String> entry : keyMap.entrySet())
				if(entry.getValue().contentEquals(bindingName))
					result.add(entry.getKey());
			return result.toArray(new Integer[result.size()]);
		}
	}
}
