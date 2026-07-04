package com.newritage.app.ui.main.thread;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J$\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\u001a\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00162\b\u0010 \u001a\u0004\u0018\u00010\u001cH\u0016J\u0010\u0010!\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0016H\u0002J\b\u0010\"\u001a\u00020\u001eH\u0002J\b\u0010#\u001a\u00020\u001eH\u0002J\u001c\u0010$\u001a\u00020\u001e2\u0012\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\'\u0012\u0004\u0012\u00020(0&H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\f\u001a\u00020\r8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/newritage/app/ui/main/thread/ThreadStorageFragment;", "Landroidx/fragment/app/Fragment;", "<init>", "()V", "tvCurrentMonth", "Landroid/widget/TextView;", "btnPrevMonth", "Landroid/widget/ImageView;", "btnNextMonth", "currentCalendar", "Ljava/util/Calendar;", "kotlin.jvm.PlatformType", "dao", "Lcom/newritage/app/data/SessionDao;", "getDao", "()Lcom/newritage/app/data/SessionDao;", "dao$delegate", "Lkotlin/Lazy;", "monthFormat", "Ljava/text/SimpleDateFormat;", "dbMonthFormat", "onCreateView", "Landroid/view/View;", "i", "Landroid/view/LayoutInflater;", "c", "Landroid/view/ViewGroup;", "s", "Landroid/os/Bundle;", "onViewCreated", "", "view", "savedInstanceState", "initViews", "setupListeners", "updateCalendar", "renderGrid", "sessionMap", "", "", "Lcom/newritage/app/data/Session;", "app_debug"})
public final class ThreadStorageFragment extends androidx.fragment.app.Fragment {
    private android.widget.TextView tvCurrentMonth;
    private android.widget.ImageView btnPrevMonth;
    private android.widget.ImageView btnNextMonth;
    private java.util.Calendar currentCalendar;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy dao$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat monthFormat = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat dbMonthFormat = null;
    
    public ThreadStorageFragment() {
        super();
    }
    
    private final com.newritage.app.data.SessionDao getDao() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater i, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup c, @org.jetbrains.annotations.Nullable()
    android.os.Bundle s) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initViews(android.view.View view) {
    }
    
    private final void setupListeners() {
    }
    
    private final void updateCalendar() {
    }
    
    private final void renderGrid(java.util.Map<java.lang.Integer, com.newritage.app.data.Session> sessionMap) {
    }
}