package com.newritage.app.ui.util;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u001d\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tJ\u000e\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\u0018J\u0010\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020!H\u0014J \u0010\"\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020!2\u0006\u0010#\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u0018H\u0002J \u0010%\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020!2\u0006\u0010#\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u0018H\u0002JH\u0010&\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020!2\u0006\u0010#\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u00182\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\'\u001a\u00020\u00182\u0006\u0010(\u001a\u00020\u000e2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\u0018H\u0002J\u0006\u0010,\u001a\u00020\u000bJ\u0006\u0010-\u001a\u00020\u000bJ\b\u0010.\u001a\u00020\u000bH\u0014R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001c\u001a\n \u001e*\u0004\u0018\u00010\u001d0\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/newritage/app/ui/util/WaveView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "waveStyle", "Lcom/newritage/app/ui/util/WaveStyle;", "setWaveStyle", "", "style", "backgroundPaint", "Landroid/graphics/Paint;", "waveBackPaint", "waveFrontPaint", "borderPaint", "clipPath", "Landroid/graphics/Path;", "wavePath", "arcRect", "Landroid/graphics/RectF;", "phase", "", "fillFactor", "setPressure", "p", "animator", "Landroid/animation/ValueAnimator;", "kotlin.jvm.PlatformType", "onDraw", "canvas", "Landroid/graphics/Canvas;", "drawMeasuringWave", "w", "h", "drawCompleteWave", "drawWave", "fill", "paint", "frequency", "", "amplitude", "startWave", "stopWave", "onDetachedFromWindow", "app_debug"})
public final class WaveView extends android.view.View {
    @org.jetbrains.annotations.NotNull()
    private com.newritage.app.ui.util.WaveStyle waveStyle = com.newritage.app.ui.util.WaveStyle.MEASURING;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint backgroundPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint waveBackPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint waveFrontPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint borderPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Path clipPath = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Path wavePath = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.RectF arcRect = null;
    private float phase = 0.0F;
    private float fillFactor = 0.5F;
    private final android.animation.ValueAnimator animator = null;
    
    @kotlin.jvm.JvmOverloads()
    public WaveView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public WaveView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    public final void setWaveStyle(@org.jetbrains.annotations.NotNull()
    com.newritage.app.ui.util.WaveStyle style) {
    }
    
    public final void setPressure(float p) {
    }
    
    @java.lang.Override()
    protected void onDraw(@org.jetbrains.annotations.NotNull()
    android.graphics.Canvas canvas) {
    }
    
    private final void drawMeasuringWave(android.graphics.Canvas canvas, float w, float h) {
    }
    
    private final void drawCompleteWave(android.graphics.Canvas canvas, float w, float h) {
    }
    
    private final void drawWave(android.graphics.Canvas canvas, float w, float h, float phase, float fill, android.graphics.Paint paint, double frequency, float amplitude) {
    }
    
    public final void startWave() {
    }
    
    public final void stopWave() {
    }
    
    @java.lang.Override()
    protected void onDetachedFromWindow() {
    }
}