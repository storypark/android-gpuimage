package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

/**
 * A filter that takes in the input texture of a filter group as the second texture id
 * Must be used in an {@link GPUImageFilterGroup}
 */
public class GPUImageLookBehindFilter extends GPUImageFilter {
    private static final String VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            "attribute vec4 inputTextureCoordinate2;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            "varying vec2 textureCoordinate2;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    textureCoordinate2 = inputTextureCoordinate2.xy;\n" +
            "}";

    public int mLookBehindTextureCoordinateAttribute;
    public int mLookBehindInputTextureUniform2;
    public int mLookBehindTextureId = OpenGlUtils.NO_TEXTURE;
    private ByteBuffer mLookBehindTextureCoordinatesBuffer;

    public GPUImageLookBehindFilter(String fragmentShader) {
        this(VERTEX_SHADER, fragmentShader);
    }

    public GPUImageLookBehindFilter(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
        setRotation(Rotation.NORMAL, false, false);
    }

    public void setLookBehindTexture(int textureId) {
        mLookBehindTextureId = textureId;
    }

    @Override
    public void onInit() {
        super.onInit();
        mLookBehindTextureCoordinateAttribute = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
        mLookBehindInputTextureUniform2 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
        GLES20.glEnableVertexAttribArray(mLookBehindTextureCoordinateAttribute);
    }

    @Override
    protected void onDrawArraysPre() {
        GLES20.glEnableVertexAttribArray(mLookBehindTextureCoordinateAttribute);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLookBehindTextureId);
        GLES20.glUniform1i(mLookBehindInputTextureUniform2, 3);

        mLookBehindTextureCoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mLookBehindTextureCoordinateAttribute, 2, GLES20.GL_FLOAT, false, 0, mLookBehindTextureCoordinatesBuffer);
    }

    public void setRotation(final Rotation rotation, final boolean flipHorizontal, final boolean flipVertical) {
        float[] buffer = TextureRotationUtil.getRotation(rotation, flipHorizontal, flipVertical);

        ByteBuffer bBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
        FloatBuffer fBuffer = bBuffer.asFloatBuffer();
        fBuffer.put(buffer);
        fBuffer.flip();

        mLookBehindTextureCoordinatesBuffer = bBuffer;
    }

}
