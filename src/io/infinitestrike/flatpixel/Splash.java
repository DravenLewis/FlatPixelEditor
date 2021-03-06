package io.infinitestrike.flatpixel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

import io.infinitestrike.flatpixel.compat.Loader;
import io.infinitestrike.flatpixel.compat.Loader.LoaderResult;
import io.infinitestrike.flatpixel.components.RenderPane;
import io.infinitestrike.flatpixel.components.RenderPane.InputManager;
import io.infinitestrike.flatpixel.components.RenderPane.RenderCall;
import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.core.ResourceManager;
import io.infinitestrike.flatpixel.core.Vector.Vector2i;
import io.infinitestrike.flatpixel.logging.Console;
import io.infinitestrike.utils.ImageTools;

public class Splash {
	public static JFrame spawnSplashScreen() {
		
		RenderPane pane = new RenderPane();
		try {
			pane.addRenderCall(new RenderCall() {

				BufferedImage splashImage = ImageTools.getImage("./res/UI/splash.png");
				LoaderResult versionInfo = Loader.readFile("./version.info");
				
				String versionString = "";
				
				@Override
				public void OnInputAttach(InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					versionString += Loader.getValueString(versionInfo.valueOf("$CODENAME", "Azazel")) + " - " + Loader.getValueString(versionInfo.valueOf("$VERSION", "0.0.0"));
				}

				@Override
				public void onKeyTyped(KeyEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onKeyPressed(KeyEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onKeyReleased(KeyEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMouseWheelMoved(MouseWheelEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMouseDragged(MouseEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMouseMoved(MouseEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMouseClicked(MouseEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMousePressed(MouseEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMouseReleased(MouseEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMouseEntered(MouseEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMouseExited(MouseEvent e, InputManager m, RenderPane p) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnAttach(RenderPane c) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnTick(double delta, RenderPane c) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnRender(Graphics g, RenderPane c) {
					// TODO Auto-generated method stub
					g.drawImage(splashImage, 0,0,480,320,null);
					g.setColor(Color.white);
					g.drawString(versionString, 10, 315);
					g.setColor(new Color(0x3e3e3e));
					g.drawString(ResourceManager.step, 10, 20);
				}

				@Override
				public void OnDetatch(RenderPane c) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnInputDetatch(RenderPane p) {
					// TODO Auto-generated method stub
					
				}
				
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Console.Error("Cannot Load File", e);
		}
		pane.start();
		
		JFrame frame = Core.spawnFrame(pane, "Splash Screen", new Vector2i(480,320), null, false, false);
		frame.setUndecorated(true);
		//frame.setAlwaysOnTop(true);
		frame.setType(javax.swing.JFrame.Type.UTILITY);
		frame.setIconImage(ResourceManager.APPLICATION_ICON);
		return frame;
	}
}
