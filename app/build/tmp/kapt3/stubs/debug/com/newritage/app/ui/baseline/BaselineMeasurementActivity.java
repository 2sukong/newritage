package com.newritage.app.ui.baseline;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001 B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0013H\u0002J\b\u0010\u0017\u001a\u00020\u0013H\u0002J\b\u0010\u001b\u001a\u00020\u0013H\u0002J\u0010\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u0013H\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u001a\u00a8\u0006!"}, d2 = {"Lcom/newritage/app/ui/baseline/BaselineMeasurementActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "binding", "Lcom/newritage/app/databinding/ActivityBaselineMeasurementBinding;", "prefs", "Lcom/newritage/app/data/UserPreferences;", "handler", "Landroid/os/Handler;", "measuring", "", "elapsedSeconds", "", "measureDuration", "pressureReadings", "", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "setupUI", "startMeasurement", "measureLoop", "Ljava/lang/Runnable;", "Ljava/lang/Runnable;", "completeMeasurement", "showScreen", "screen", "Lcom/newritage/app/ui/baseline/BaselineMeasurementActivity$Screen;", "onDestroy", "Screen", "app_debug"})
public final class BaselineMeasurementActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.newritage.app.databinding.ActivityBaselineMeasurementBinding binding;
    private com.newritage.app.data.UserPreferences prefs;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler handler = null;
    private boolean measuring = false;
    private int elapsedSeconds = 0;
    private final int measureDuration = 30;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Float> pressureReadings = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Runnable measureLoop = null;
    
    public BaselineMeasurementActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupUI() {
    }
    
    private final void startMeasurement() {
    }
    
    private final void completeMeasurement() {
    }
    
    private final void showScreen(com.newritage.app.ui.baseline.BaselineMeasurementActivity.Screen screen) {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/newritage/app/ui/baseline/BaselineMeasurementActivity$Screen;", "", "<init>", "(Ljava/lang/String;I)V", "GUIDE", "MEASURING", "COMPLETE", "app_debug"})
    public static enum Screen {
        /*public static final*/ GUIDE /* = new GUIDE() */,
        /*public static final*/ MEASURING /* = new MEASURING() */,
        /*public static final*/ COMPLETE /* = new COMPLETE() */;
        
        Screen() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.newritage.app.ui.baseline.BaselineMeasurementActivity.Screen> getEntries() {
            return null;
        }
    }
}