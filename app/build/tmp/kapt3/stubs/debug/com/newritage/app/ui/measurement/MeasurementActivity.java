package com.newritage.app.ui.measurement;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u00002\u00020\u0001:\u00013B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0014J\b\u0010(\u001a\u00020%H\u0002J\b\u0010)\u001a\u00020%H\u0002J\b\u0010*\u001a\u00020%H\u0002J\b\u0010+\u001a\u00020%H\u0002J\b\u0010,\u001a\u00020%H\u0002J\b\u0010-\u001a\u00020%H\u0002J\u0010\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u00020\u001bH\u0002J\b\u00100\u001a\u00020%H\u0002J\b\u00101\u001a\u00020%H\u0002J\b\u00102\u001a\u00020%H\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\u001d\u001a\n \u001f*\u0004\u0018\u00010\u001e0\u001e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\"\u0010#\u001a\u0004\b \u0010!\u00a8\u00064"}, d2 = {"Lcom/newritage/app/ui/measurement/MeasurementActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "screenWaiting", "Landroid/view/View;", "screenMeasuring", "waveView", "Lcom/newritage/app/ui/util/WaveView;", "tvPressureValue", "Landroid/widget/TextView;", "tvSessionTime", "tvFeedbackStatus", "lineChart", "Lcom/github/mikephil/charting/charts/LineChart;", "btnComplete", "Landroid/widget/Button;", "handler", "Landroid/os/Handler;", "countdownLeft", "", "elapsedSeconds", "deviationCount", "pressureEntries", "", "Lcom/github/mikephil/charting/data/Entry;", "currentPressure", "", "allPressures", "startTimeStr", "", "kotlin.jvm.PlatformType", "getStartTimeStr", "()Ljava/lang/String;", "startTimeStr$delegate", "Lkotlin/Lazy;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "bindViews", "showWaiting", "showMeasuring", "startCountdown", "setupChart", "startMeasuring", "feedbackText", "p", "updateChart", "finishSession", "onDestroy", "Screen", "app_debug"})
public final class MeasurementActivity extends androidx.appcompat.app.AppCompatActivity {
    private android.view.View screenWaiting;
    private android.view.View screenMeasuring;
    private com.newritage.app.ui.util.WaveView waveView;
    private android.widget.TextView tvPressureValue;
    private android.widget.TextView tvSessionTime;
    private android.widget.TextView tvFeedbackStatus;
    private com.github.mikephil.charting.charts.LineChart lineChart;
    private android.widget.Button btnComplete;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler handler = null;
    private int countdownLeft = 10;
    private int elapsedSeconds = 0;
    private int deviationCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.github.mikephil.charting.data.Entry> pressureEntries = null;
    private float currentPressure = 32.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Float> allPressures = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy startTimeStr$delegate = null;
    
    public MeasurementActivity() {
        super();
    }
    
    private final java.lang.String getStartTimeStr() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void bindViews() {
    }
    
    private final void showWaiting() {
    }
    
    private final void showMeasuring() {
    }
    
    private final void startCountdown() {
    }
    
    private final void setupChart() {
    }
    
    private final void startMeasuring() {
    }
    
    private final java.lang.String feedbackText(float p) {
        return null;
    }
    
    private final void updateChart() {
    }
    
    private final void finishSession() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/newritage/app/ui/measurement/MeasurementActivity$Screen;", "", "<init>", "(Ljava/lang/String;I)V", "WAITING", "MEASURING", "app_debug"})
    static enum Screen {
        /*public static final*/ WAITING /* = new WAITING() */,
        /*public static final*/ MEASURING /* = new MEASURING() */;
        
        Screen() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.newritage.app.ui.measurement.MeasurementActivity.Screen> getEntries() {
            return null;
        }
    }
}