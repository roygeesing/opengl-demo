package org.lwjgl.demo.opengl;

import org.lwjgl.demo.util.Color4D;
import org.lwjgl.demo.util.OGLObject;

public class Side extends OGLObject {
    final static int CoordinatesPerVertex = 3;

    protected Side(Color4D color) {
        super(color);

        final int nVertices = 5;
        final int nCoordinates = nVertices*CoordinatesPerVertex;

        // allocate vertex positions and normals
        allocatePositionBuffer(nCoordinates);
        allocateNormalBuffer(nCoordinates);

        // CCW order needed in GL_QUADS
        float scale = 0.7655f;
        float c1 = (float) Math.cos(2*Math.PI/5)*scale;
        float c2 = (float) Math.cos(Math.PI/5)*scale;
        float s1 = (float) Math.sin(2*Math.PI/5)*scale;
        float s2 = (float) Math.sin(Math.PI/5)*scale;
        addVertex(0, scale, 0);
        addVertex(-s1, c1, 0);
        addVertex(-s2, -c2, 0);
        addVertex(s2, -c2, 0);
        addVertex(s1, c1, 0);

        // bin vertex positions and normals
        bindPositionBuffer();
        bindNormalBuffer();
    }

    private void addVertex(float x, float y, float z) {
        m_positions.put(m_vertexCount*CoordinatesPerVertex + 0, x);
        m_positions.put(m_vertexCount*CoordinatesPerVertex + 1, y);
        m_positions.put(m_vertexCount*CoordinatesPerVertex + 2, z);

        m_normals.put(m_vertexCount*CoordinatesPerVertex + 0, 0);
        m_normals.put(m_vertexCount*CoordinatesPerVertex + 1, 0);
        m_normals.put(m_vertexCount*CoordinatesPerVertex + 2, 1);

        m_vertexCount++;
    }

    public Side setRGBA(float r, float g, float b, float a) {
        m_color.put(0, r);
        m_color.put(1, g);
        m_color.put(2, b);
        m_color.put(3, a);
        return this;
    }
}
