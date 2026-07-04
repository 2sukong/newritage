package com.newritage.app.data;

/**
 * 사용자 설정 및 앱 상태를 SharedPreferences로 관리
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 )2\u00020\u0001:\u0001)B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\'\u001a\u00020(R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR$\u0010\u000e\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u000b\"\u0004\b\u000f\u0010\rR$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\b\u001a\u00020\u00108F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R$\u0010\u0016\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0016\u0010\u000b\"\u0004\b\u0017\u0010\rR$\u0010\u0019\u001a\u00020\u00182\u0006\u0010\b\u001a\u00020\u00188F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR$\u0010\u001e\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001f\u0010\u000b\"\u0004\b \u0010\rR$\u0010\"\u001a\u00020!2\u0006\u0010\b\u001a\u00020!8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&\u00a8\u0006*"}, d2 = {"Lcom/newritage/app/data/UserPreferences;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "prefs", "Landroid/content/SharedPreferences;", "value", "", "isOnboardingDone", "()Z", "setOnboardingDone", "(Z)V", "isLoggedIn", "setLoggedIn", "", "username", "getUsername", "()Ljava/lang/String;", "setUsername", "(Ljava/lang/String;)V", "isBaselineDone", "setBaselineDone", "", "baselinePressure", "getBaselinePressure", "()F", "setBaselinePressure", "(F)V", "autoLogin", "getAutoLogin", "setAutoLogin", "", "lastSessionId", "getLastSessionId", "()J", "setLastSessionId", "(J)V", "logout", "", "Companion", "app_debug"})
public final class UserPreferences {
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ONBOARDING_DONE = "onboarding_done";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LOGGED_IN = "logged_in";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_USERNAME = "username";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_BASELINE_DONE = "baseline_done";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_BASELINE_PRESSURE = "baseline_pressure";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_AUTO_LOGIN = "auto_login";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_SESSION_ID = "last_session_id";
    @org.jetbrains.annotations.NotNull()
    public static final com.newritage.app.data.UserPreferences.Companion Companion = null;
    
    public UserPreferences(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final boolean isOnboardingDone() {
        return false;
    }
    
    public final void setOnboardingDone(boolean value) {
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    public final void setLoggedIn(boolean value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUsername() {
        return null;
    }
    
    public final void setUsername(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final boolean isBaselineDone() {
        return false;
    }
    
    public final void setBaselineDone(boolean value) {
    }
    
    public final float getBaselinePressure() {
        return 0.0F;
    }
    
    public final void setBaselinePressure(float value) {
    }
    
    public final boolean getAutoLogin() {
        return false;
    }
    
    public final void setAutoLogin(boolean value) {
    }
    
    public final long getLastSessionId() {
        return 0L;
    }
    
    public final void setLastSessionId(long value) {
    }
    
    /**
     * 로그아웃
     */
    public final void logout() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/newritage/app/data/UserPreferences$Companion;", "", "<init>", "()V", "KEY_ONBOARDING_DONE", "", "KEY_LOGGED_IN", "KEY_USERNAME", "KEY_BASELINE_DONE", "KEY_BASELINE_PRESSURE", "KEY_AUTO_LOGIN", "KEY_LAST_SESSION_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}