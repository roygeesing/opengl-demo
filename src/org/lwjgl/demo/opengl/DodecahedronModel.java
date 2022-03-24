package org.lwjgl.demo.opengl;

import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.demo.util.Color4D;
import org.lwjgl.demo.util.OGLModel3D;

import java.nio.FloatBuffer;

import static org.joml.Math.PI;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL20C.glUniform3fv;
import static org.lwjgl.opengl.GL20C.glUniform4fv;
import static org.lwjgl.opengl.GL20C.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;

public class DodecahedronModel extends OGLModel3D {
    final static double deg2rad = PI/180;

    private final Matrix3d m_vm = new Matrix3d();
    private final Vector3d m_light  = new Vector3d();
    private final FloatBuffer m_vec3f = BufferUtils.createFloatBuffer(3);
    private final FloatBuffer m_mat3f = BufferUtils.createFloatBuffer(3*3);
    private final FloatBuffer m_mat4f = BufferUtils.createFloatBuffer(4*4);

    private Side m_side;
    private double m_startTime = System.currentTimeMillis()/1000.0;
    private double m_distance = 10.0f;	// camera distance
    private double m_dxAngle = 0;		// degrees
    private double m_dyAngle = 0; 		// degrees
    private double m_xAngle = 0;		// degrees
    private double m_yAngle = 0;		// degrees
    private double m_zAngle = 0;		// degrees
    private long   m_count;				// fps

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        m_side = new Side(new Color4D(0, 0, 0, 1));
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // VIEW
        V.translation(0.0, 0.0, -m_distance).rotateX(m_xAngle*deg2rad).rotateY(m_yAngle*deg2rad).rotateZ(m_zAngle*deg2rad); // V = T*Rx*Ry*Rz

        // LIGHT
        glUniform3fv(u_LIGHT, m_light.set(0.0, 0.0, 10.0).normalize().get(m_vec3f)); // V * m_light

        // front
        M.translation(0, 0, 1); // translation = identity.translate
        drawSide(m_side.setRGBA(1, 0, 0, 1));

        M.rotationX(180*deg2rad).translate(0, 0, 1);
        drawSide(m_side.setRGBA(1, 0, 0, 1));

        M.rotationX(63.3*deg2rad).translate(0, 0, 1).rotateZ(180*deg2rad);
        drawSide(m_side.setRGBA(0, 1, 0, 1));

        M.rotationX(-(180-63.3)*deg2rad).translate(0, 0, 1).rotateZ(180*deg2rad);
        drawSide(m_side.setRGBA(0, 1, 0, 1));

        M.rotationX(-58.2*deg2rad).rotateY(-31.65*deg2rad).translate(0, 0, 1).rotateZ(90*deg2rad);
        drawSide(m_side.setRGBA(0, 0, 1, 1));

        M.rotationX(-(180+58.2)*deg2rad).rotateY(31.65*deg2rad).translate(0, 0, 1).rotateZ(-90*deg2rad);
        drawSide(m_side.setRGBA(0, 0, 1, 1));

        M.rotationX(-58.2*deg2rad).rotateY(31.65*deg2rad).translate(0, 0, 1).rotateZ(-90*deg2rad);
        drawSide(m_side.setRGBA(0, 1, 1, 1));

        M.rotationX(-(180+58.2)*deg2rad).rotateY(-31.65*deg2rad).translate(0, 0, 1).rotateZ(90*deg2rad);
        drawSide(m_side.setRGBA(0, 1, 1, 1));

        M.rotationX(31.65*deg2rad).rotateY(58.2*deg2rad).translate(0, 0, 1).rotateZ(18*deg2rad);
        drawSide(m_side.setRGBA(1, 1, 0, 1));

        M.rotationX(31.65*deg2rad).rotateY((180+58.2)*deg2rad).translate(0, 0, 1).rotateZ(18*deg2rad);
        drawSide(m_side.setRGBA(1, 1, 0, 1));

        M.rotationX((180+31.65)*deg2rad).rotateY(58.2*deg2rad).translate(0, 0, 1).rotateZ(18*deg2rad);
        drawSide(m_side.setRGBA(1, 0, 1, 1));

        M.rotationX((180+31.65)*deg2rad).rotateY((180+58.2)*deg2rad).translate(0, 0, 1).rotateZ(18*deg2rad);
        drawSide(m_side.setRGBA(1, 0, 1, 1));

        // fps
        m_count++;

        double theTime = System.currentTimeMillis()/1000.0;
        if (theTime >= m_startTime + 1.0) {
            System.out.format("%d fps\n", m_count);
            m_startTime = theTime;
            m_count = 0;
        }

        // animation
        m_xAngle -= m_dxAngle;
        m_yAngle -= m_dyAngle;
    }

    public void changeXangle(double delta) {
        m_dxAngle += delta;
    }

    public void changeYangle(double delta) {
        m_dyAngle += delta;
    }

    private void drawSide(Side side) {
        // set geometric transformation matrices for all vertices of this model
        glUniformMatrix3fv(u_VM, false, V.mul(M, VM).normal(m_vm).get(m_mat3f));
        glUniformMatrix4fv(u_PVM, false, P.mul(VM, PVM).get(m_mat4f)); // get: stores in and returns m_mat4f

        // set color for all vertices of this model
        glUniform4fv(u_COLOR, side.getColor());

        // draw a quad
        side.setupPositions(m_POSITIONS);
        side.setupNormals(m_NORMALS);
        glDrawArrays(GL_POLYGON, 0, side.getVertexCount());
    }
}
