package io.infinitestrike.core;

public class Vector{
	public static class Vector2f{
		public float x, y;
		public Vector2f(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		public Vector2f() {
			this(0,0);
		}
		
		public String toString() {
			return "[" + this.x + "," + this.y + "]";
		}
	}
	
	public static class Vector2i{
		public int x, y;
		public Vector2i(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Vector2i() {
			this(0,0);
		}
		
		public String toString() {
			return "[" + this.x + "," + this.y + "]";
		}
	}
}
