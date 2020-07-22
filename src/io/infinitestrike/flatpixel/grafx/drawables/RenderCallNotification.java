package io.infinitestrike.flatpixel.grafx.drawables;

import java.awt.Color;
import java.awt.Graphics;

import io.infinitestrike.flatpixel.components.RenderPane;
import io.infinitestrike.flatpixel.core.ResourceManager;
import io.infinitestrike.utils.FontUtils;

public class RenderCallNotification implements RenderPane.Drawable {

	public static final int LENGTH_LONG = 1000;
	public static final int LENGTH_SHORT = 500;
	public static final int LENGTH_FOREVER = -1;
	
	private static int currentRenderCalls = 0;
	private final int callWidth = 256;
	private final int callHeight = 64;

	private int targetY = -1;
	private int x = 0, y = 0;
	private final String message;
	private int duration = 0;
	private boolean moveDirection = true;
	private boolean canMove = true;
	private float scrollIndex = 0;
	
	public RenderCallNotification(String s, int duration, RenderPane p) {
		this.message = s;
		this.duration = duration;
		this.targetY = currentRenderCalls * callHeight + 5;
		this.y = -callHeight - 5;
		p.addDrawable(this);
		currentRenderCalls++;
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub
		g.drawImage(ResourceManager.UINotification, x, y, null);
		g.setColor(Color.white);
		g.setFont(FontUtils.changeFontSize(FontUtils.VHS, 16));
		g.drawString(message, x + 15, y + 40);
		/*if(message.length() <= 16) {
			g.drawString(message, x + 15, y + 40);
		}else {
			
			int scroll = Math.round(scrollIndex);
			if(scroll + 16 <= this.message.length()) {
				g.drawString(message.substring(scroll, scroll + 15), x + 15, y + 40);
				scrollIndex+=0.02f;
			}else {
				g.drawString(message.substring(message.length() - 17, message.length()), x + 15, y + 40);
			}
		}*/
		g.drawImage(ResourceManager.UINotificationQuestion, x + 5,y + 5, null);
	}

	
	public static void MakeNotification(String message, int length, RenderPane p) {
		new RenderCallNotification(message,length,p);
	}
	
	@Override
	public void OnTick(double delta, RenderPane c) {
		// TODO Auto-generated method stub
		
		x = c.getWidth() - callWidth - 5;
		
		if (moveDirection == true && canMove) { // we are moving down...
			if(y >= targetY) {
				// reached our target
				canMove = false;
			}
			
			y += 1 * delta;
		}
		if (moveDirection == false && canMove) { // we are moving up...
			if(y < -callHeight) {
				// call remove code
				if(currentRenderCalls != 0) {
					currentRenderCalls--;
				}
				c.removeDrawable(this);
			}
			
			y -= 1 * delta;
		}
		if(canMove == false && duration != -1) {
			if(duration > 0) {
				duration--;
			}else {
				canMove = true;
				this.moveDirection = !this.moveDirection;
			}
		}
	}
}
