package org.lwjgl.demo.opengl;

import org.lwjgl.demo.util.Color4D;
import org.lwjgl.demo.util.OGLApp;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Dodecahedron3D extends OGLApp<DodecahedronModel> {
    public Dodecahedron3D(DodecahedronModel model) {
        super(model);

        m_keyCallback = (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            else if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch(key) {
                    case GLFW_KEY_LEFT: model.changeYangle(0.125); break;
                    case GLFW_KEY_RIGHT: model.changeYangle(-0.125); break;
                    case GLFW_KEY_UP: model.changeXangle(0.125); break;
                    case GLFW_KEY_DOWN: model.changeXangle(-0.125); break;
                }
            }
        };
    }

    public static void main(String[] args) {
        new Dodecahedron3D(new DodecahedronModel()).run("Dodecahedron", 640, 640, new Color4D(0.7f, 0.7f, 0.7f, 1));
    }
}
