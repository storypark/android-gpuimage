package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GPUImageGLSurfaceView extends GLSurfaceView implements GPUImageView.OnForcedSizingListener {

    private GPUImageView.Size mForcedSize;

    public GPUImageGLSurfaceView(Context context) {
        super(context);
    }

    public GPUImageGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onForceSize(GPUImageView.Size forcedSize) {
        this.mForcedSize = forcedSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mForcedSize != null) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(mForcedSize.width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mForcedSize.height, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}

