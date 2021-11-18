package mvc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Objects;

public class Display {

	private static Display DISPLAY;

	private final JFrame frame;
	private final Canvas canvas;
	private final String title;
	private final int width;
	private final int height;
	private final BufferStrategy bs;


	public Display(String title, int width, int height) {
		Objects.requireNonNull(title);

		this.title = title;
		this.width = width;
		this.height = height;

		frame = new JFrame();
		frame.setTitle(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		canvas = new Canvas();
		canvas.setVisible(true);
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));

		frame.add(canvas);
		frame.pack();

		canvas.createBufferStrategy(3);
		bs = canvas.getBufferStrategy();
		DISPLAY = this;
	}

	public static Display display() {
		return DISPLAY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Graphics getGraphics() {
		return bs.getDrawGraphics();
	}

	public void render() {
		bs.show();
	}

	public void addKeyListener(KeyListener keyListener) {
		frame.addKeyListener(keyListener);
	}
}