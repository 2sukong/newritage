package com.newritage.app.ui.main.analysis;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J$\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u001a\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020\u000e2\b\u0010*\u001a\u0004\u0018\u00010&H\u0016J\b\u0010+\u001a\u00020(H\u0002J\b\u0010,\u001a\u00020(H\u0002J\u0016\u0010-\u001a\u00020(2\f\u0010.\u001a\b\u0012\u0004\u0012\u0002000/H\u0002J\u001e\u00101\u001a\b\u0012\u0004\u0012\u0002020/2\u0006\u00103\u001a\u0002002\u0006\u00104\u001a\u000205H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0014\u001a\n \u0016*\u0004\u0018\u00010\u00150\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0017\u001a\u00020\u00188BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u0019\u0010\u001aR\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00066"}, d2 = {"Lcom/newritage/app/ui/main/analysis/DailyAnalysisFragment;", "Landroidx/fragment/app/Fragment;", "<init>", "()V", "graphColors", "", "tvDate", "Landroid/widget/TextView;", "tvAvg", "tvMax", "tvTime", "lineChart", "Lcom/github/mikephil/charting/charts/LineChart;", "layoutComment", "Landroid/view/View;", "tvCommentTitle", "tvCommentContent", "btnPrev", "Landroid/widget/ImageButton;", "btnNext", "currentDate", "Ljava/util/Calendar;", "kotlin.jvm.PlatformType", "dao", "Lcom/newritage/app/data/SessionDao;", "getDao", "()Lcom/newritage/app/data/SessionDao;", "dao$delegate", "Lkotlin/Lazy;", "sdf", "Ljava/text/SimpleDateFormat;", "displaySdf", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "s", "Landroid/os/Bundle;", "onViewCreated", "", "view", "savedInstanceState", "setupChart", "loadData", "updateUI", "sessions", "", "Lcom/newritage/app/data/Session;", "generateSessionEntries", "Lcom/github/mikephil/charting/data/Entry;", "session", "timeOffsetSecs", "", "app_debug"})
public final class DailyAnalysisFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.NotNull()
    private final int[] graphColors = null;
    private android.widget.TextView tvDate;
    private android.widget.TextView tvAvg;
    private android.widget.TextView tvMax;
    private android.widget.TextView tvTime;
    private com.github.mikephil.charting.charts.LineChart lineChart;
    private android.view.View layoutComment;
    private android.widget.TextView tvCommentTitle;
    private android.widget.TextView tvCommentContent;
    private android.widget.ImageButton btnPrev;
    private android.widget.ImageButton btnNext;
    private java.util.Calendar currentDate;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy dao$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat sdf = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat displaySdf = null;
    
    public DailyAnalysisFragment() {
        super();
    }
    
    private final com.newritage.app.data.SessionDao getDao() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle s) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupChart() {
    }
    
    private final void loadData() {
    }
    
    private final void updateUI(java.util.List<com.newritage.app.data.Session> sessions) {
    }
    
    private final java.util.List<com.github.mikephil.charting.data.Entry> generateSessionEntries(com.newritage.app.data.Session session, int timeOffsetSecs) {
        return null;
    }
}