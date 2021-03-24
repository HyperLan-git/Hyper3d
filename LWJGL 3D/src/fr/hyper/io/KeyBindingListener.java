package fr.hyper.io;

public interface KeyBindingListener {
	public void onKeyPressed(String bindingName);

	public void onKeyDown(String bindingName);

	public void onKeyReleased(String bindingName);
}
