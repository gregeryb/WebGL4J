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
package com.shc.webgl4j.client;

import com.google.gwt.core.client.JavaScriptObject;
import static com.shc.webgl4j.client.WebGL10.GL_ARRAY_BUFFER;
import static com.shc.webgl4j.client.WebGL10.GL_ELEMENT_ARRAY_BUFFER;
import static com.shc.webgl4j.client.WebGL10.GL_RENDERBUFFER;
import static com.shc.webgl4j.client.WebGL10.GL_TEXTURE_2D;
import static com.shc.webgl4j.client.WebGL10.GL_TEXTURE_CUBE_MAP;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

/**
 * A class used to convert WebGL objects (WebGLTexture, WebGLBuffer etc.,) to integers just like the
 * desktop OpenGL. This class is package-private since there is no direct use of this class for
 * users. It is simply expected to work in the background. That is the reason this class is declared
 * as package private.
 *
 * @author Sri Harsha Chilakapati
 */
final class WebGLObjectMap {

 private final static Map<WebGLContext, WebGLObjectMap> contexts = new HashMap<>();

 public static WebGLObjectMap get() {
  WebGLObjectMap map = contexts.get(WebGLContext.getCurrent());
  if (map == null) {
   map = new WebGLObjectMap();
   contexts.put(WebGLContext.getCurrent(), map);
  }
  return map;
 }
 // WebGL10 objects
 private final Map<Integer, JavaScriptObject> shaders = new HashMap<>();
 private final Map<Integer, JavaScriptObject> buffers = new HashMap<>();
 private final Map<Integer, JavaScriptObject> programs = new HashMap<>();
 private final Map<Integer, JavaScriptObject> textures = new HashMap<>();
 private final Map<Integer, JavaScriptObject> frameBuffers = new HashMap<>();
 private final Map<Integer, JavaScriptObject> renderBuffers = new HashMap<>();
 private final Map<Integer, Map<String, SimpleEntry<Integer, JavaScriptObject>>> uniform_names = new HashMap<>();
 // WebGL20 objects
 private final Map<Integer, JavaScriptObject> queries = new HashMap<>();
 private final Map<Integer, JavaScriptObject> samplers = new HashMap<>();
 private final Map<Integer, JavaScriptObject> syncs = new HashMap<>();
 private final Map<Integer, JavaScriptObject> transformFeedbacks = new HashMap<>();
 private final Map<Integer, JavaScriptObject> vertexArrayObjects = new HashMap<>();
 private final Map<Integer, Integer> query_bindings = new HashMap<>();
 // Field to store the current program
 int currentProgram = 0;
 // Fields for last generated IDs of WebGL objects
 private int lastShaderID = 0;
 private int lastBufferID = 0;
 private int lastProgramID = 0;
 private int lastTextureID = 0;
 private int lastFramebufferID = 0;
 private int lastRenderbufferID = 0;
 private int lastUniformID = 0;
 private int lastQueryID = 0;
 private int lastSamplerID = 0;
 private int lastSyncID = 0;
 private int lastTransformFeedbackID = 0;
 private int lastVertexArrayObjectID = 0;
 int currentFrameBuffer;
 int currentElementArrayBinding;
 int currentArrayBufferBinding;
 int currentRenderBufferBinding;
 int currentTexture2DBinding;
 int currentTextureCubeMapBInding;

 private WebGLObjectMap() {
 }

 int createShader(JavaScriptObject shader) {
  if (shader == null) {
   return 0;
  }
  shaders.put(++lastShaderID, shader);
  return lastShaderID;
 }

 JavaScriptObject toShader(int shaderID) {
  return shaders.get(shaderID);
 }

 int createBuffer(JavaScriptObject buffer) {
  if (buffer == null) {
   return 0;
  }
  buffers.put(++lastBufferID, buffer);
  return lastBufferID;
 }

 JavaScriptObject toBuffer(int bufferID) {
  return buffers.get(bufferID);
 }

 int createProgram(JavaScriptObject program) {
  if (program == null) {
   return 0;
  }
  programs.put(++lastProgramID, program);
  return lastProgramID;
 }

 JavaScriptObject toProgram(int programID) {
  return programs.get(programID);
 }

 int createTexture(JavaScriptObject texture) {
  if (texture == null) {
   return 0;
  }
  textures.put(++lastTextureID, texture);
  return lastTextureID;
 }

 JavaScriptObject toTexture(int textureID) {
  return textures.get(textureID);
 }

 int createFramebuffer(JavaScriptObject frameBuffer) {
  if (frameBuffer == null) {
   return 0;
  }
  frameBuffers.put(++lastFramebufferID, frameBuffer);
  return lastFramebufferID;
 }

 JavaScriptObject toFramebuffer(int frameBuffer) {
  return frameBuffers.get(frameBuffer);
 }

 int createRenderBuffer(JavaScriptObject renderBuffer) {
  if (renderBuffer == null) {
   return 0;
  }
  renderBuffers.put(++lastRenderbufferID, renderBuffer);
  return lastRenderbufferID;
 }

 JavaScriptObject toRenderBuffer(int renderBuffer) {
  return renderBuffers.get(renderBuffer);
 }

 int createUniform(int programID, String name, JavaScriptObject uniform) {
  if (uniform == null) {
   return -1;
  }
  Map<String, SimpleEntry<Integer, JavaScriptObject>> progUniforms = uniform_names.get(programID);
  if (progUniforms == null) {
   progUniforms = new HashMap<>();
   uniform_names.put(programID, progUniforms);
  }
  SimpleEntry<Integer, JavaScriptObject> uniform_map = progUniforms.get(name);
  if (uniform_map != null) {
   return uniform_map.getKey();
  } else {
   ++lastUniformID;
   progUniforms.put(name, new SimpleEntry<>(lastUniformID, uniform));
   return lastUniformID;
  }
 }

 JavaScriptObject toUniform(int programID, int uniform) {
  Map<String, SimpleEntry<Integer, JavaScriptObject>> progUniforms = uniform_names.get(programID);
  for (SimpleEntry<Integer, JavaScriptObject> map : progUniforms.values()) {
   if (map.getKey() == uniform) {
    return map.getValue();
   }
  }
  return null;
 }

 JavaScriptObject toUniform(int uniform) {
  return toUniform(currentProgram, uniform);
 }

 void deleteShader(int shader) {
  if (shader != 0) {
   shaders.remove(shader);
  }
 }

 int getCurrentProgram() {
  return currentProgram;
 }

 void deleteBuffer(int buffer) {
  if (buffer != 0) {
   buffers.remove(buffer);
  }
 }

 void deleteProgram(int program) {
  if (program != 0) {
   programs.remove(program);
   uniform_names.remove(program);
  }
 }

 void deleteTexture(int texture) {
  if (texture != 0) {
   textures.remove(texture);
  }
 }

 void deleteFramebuffer(int frameBuffer) {
  if (frameBuffer != 0) {
   frameBuffers.remove(frameBuffer);
  }
 }

 void deleteRenderBuffer(int renderBuffer) {
  if (renderBuffer != 0) {
   renderBuffers.remove(renderBuffer);
  }
 }

 int createQuery(JavaScriptObject query) {
  if (query == null) {
   return 0;
  }
  queries.put(++lastQueryID, query);
  return lastQueryID;
 }

 JavaScriptObject toQuery(int query) {
  return queries.get(query);
 }

 int createSampler(JavaScriptObject sampler) {
  if (sampler == null) {
   return 0;
  }
  samplers.put(++lastSamplerID, sampler);
  return lastSamplerID;
 }

 JavaScriptObject toSampler(int sampler) {
  return samplers.get(sampler);
 }

 int createSync(JavaScriptObject sync) {
  if (sync == null) {
   return 0;
  }
  syncs.put(++lastSyncID, sync);
  return lastSyncID;
 }

 JavaScriptObject toSync(int sync) {
  return syncs.get(sync);
 }

 int createTransformFeedback(JavaScriptObject transformFeedback) {
  if (transformFeedback == null) {
   return 0;
  }
  transformFeedbacks.put(++lastTransformFeedbackID, transformFeedback);
  return lastTransformFeedbackID;
 }

 JavaScriptObject toTransformFeedback(int transformFeedback) {
  return transformFeedbacks.get(transformFeedback);
 }

 int createVertexArrayObject(JavaScriptObject vertexArrayObject) {
  if (vertexArrayObject == null) {
   return 0;
  }
  vertexArrayObjects.put(++lastVertexArrayObjectID, vertexArrayObject);
  return lastVertexArrayObjectID;
 }

 JavaScriptObject toVertexArrayObject(int vertexArrayObject) {
  return vertexArrayObjects.get(vertexArrayObject);
 }

 void deleteQuery(int query) {
  if (query != 0) {
   queries.remove(query);
  }
 }

 void deleteSampler(int sampler) {
  if (sampler != 0) {
   samplers.remove(sampler);
  }
 }

 void deleteSync(int sync) {
  if (sync != 0) {
   syncs.remove(sync);
  }
 }

 void deleteTransformFeedback(int transformFeedback) {
  if (transformFeedback != 0) {
   transformFeedbacks.remove(transformFeedback);
  }
 }

 void deleteVertexArrayObject(int vertexArrayObject) {
  if (vertexArrayObject != 0) {
   vertexArrayObjects.remove(vertexArrayObject);
  }
 }

 void bindFrameBuffer(int frameBuffer) {
  currentFrameBuffer = frameBuffer;
 }

 int getCurrentFrameBuffer() {
  return currentFrameBuffer;
 }

 void bindBuffer(int target, int buffer) {
  switch (target) {
   case GL_ARRAY_BUFFER:
    currentArrayBufferBinding = buffer;
    break;
   case GL_ELEMENT_ARRAY_BUFFER:
    currentElementArrayBinding = buffer;
  }
 }

 int getCurrentElementArrayBinding() {
  return currentElementArrayBinding;
 }

 int getCurrentArrayBufferBinding() {
  return currentArrayBufferBinding;
 }

 void bindRenderBuffer(int target, int renderBuffer) {
  switch (target) {
   case GL_RENDERBUFFER:
    currentRenderBufferBinding = renderBuffer;
  }
 }

 int getCurrentRenderBufferBinding() {
  return currentRenderBufferBinding;
 }

 void bindTexture(int target, int textureID) {
  switch (target) {
   case GL_TEXTURE_2D:
    currentTexture2DBinding = textureID;
    break;
   case GL_TEXTURE_CUBE_MAP:
    currentTextureCubeMapBInding = textureID;
    break;
  }
 }

 int getCurrentTexture2DBinding() {
  return currentTexture2DBinding;
 }

 void setCurrentTexture2DBinding(int currentTexture2DBinding) {
  this.currentTexture2DBinding = currentTexture2DBinding;
 }

 public int getCurrentTextureCubeMapBInding() {
  return currentTextureCubeMapBInding;
 }

 public void setCurrentTextureCubeMapBInding(int currentTextureCubeMapBInding) {
  this.currentTextureCubeMapBInding = currentTextureCubeMapBInding;
 }

 void bindQuery(int target, int query) {
  query_bindings.put(target, query);
 }

 int getQuery(int target) {
  Integer r = query_bindings.get(target);
  if (r != null) {
   return r;
  } else {
   return 0;
  }
 }
}
