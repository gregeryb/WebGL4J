/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.shc.webgl4j.examples.client;

import com.google.gwt.canvas.client.Canvas;
import com.shc.webgl4j.client.WebGL10;
import com.shc.webgl4j.client.WebGLContext;
import javax.vecmath.Matrix4f;

/**
 * The abstract class that represents an example. Contains the context, the
 * canvas, and a simple animation loop.
 *
 * @author Sri Harsha Chilakapati
 */
public abstract class Example {
 protected WebGLContext context;
 protected Canvas canvas;

 public Example(Canvas canvas) {
  this(canvas, WebGL10.createContext(canvas));
 }

 public Example(Canvas canvas, WebGLContext context) {
  this.canvas = canvas;
  this.context = context;
 }

 /**
  * Initializes the WebGL resources, and also the example.
  */
 public abstract void init();

 /**
  * Called every frame to draw the example onto the canvas.
  */
 public abstract void render();

 /**
  * Calculate a perspective projection matrix. This is the same as
  * gluPerspective.
  *
  * @param r
  *         matrix where the result is stored
  * @param fovy
  *         the vertical field of view in radians.
  * @param aspect
  *         the aspect ratio of width/height.
  * @param zNear
  *         distance to the near clipping plane.
  * @param zFar
  *         distance the far clipping plane.
  */
 public static void perspective(Matrix4f r, float fovy, float aspect, float zNear,
   float zFar) {
  float depth = zNear - zFar;
  float f = 1.0f / (float) Math.tan(fovy / 2.0f);
  r.m00 = f / aspect;
  r.m01 = 0;
  r.m02 = 0;
  r.m03 = 0;
  r.m10 = 0;
  r.m11 = f;
  r.m12 = 0;
  r.m13 = 0;
  r.m20 = 0;
  r.m21 = 0;
  r.m22 = (zFar + zNear) / depth;
  r.m23 = (2.0f * zFar * zNear) / depth;
  r.m30 = 0;
  r.m31 = 0;
  r.m32 = -1;
  r.m33 = 0;
 }
}
