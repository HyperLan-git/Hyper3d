package fr.hyper.render;

public class FontCharacter {
	private int code, x, y, width, height, xoff, yoff, xadv;

	public FontCharacter(int code, int x, int y, int width, int height, int xoff, int yoff, int xadv) {
		this.code = code;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xoff = xoff;
		this.yoff = yoff;
		this.xadv = xadv;
	}

	public int getCode() {
		return code;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getXoff() {
		return xoff;
	}

	public int getYoff() {
		return yoff;
	}

	public int getXadv() {
		return xadv;
	}
}
