package com.newritage.app.util;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u0015B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0013\u001a\u00020\u0014R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\bR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\b\u00a8\u0006\u0016"}, d2 = {"Lcom/newritage/app/util/ThreadColors;", "", "<init>", "()V", "LOW", "", "Lcom/newritage/app/util/ThreadColors$ThreadColor;", "getLOW", "()Ljava/util/List;", "MEDIUM", "getMEDIUM", "HIGH", "getHIGH", "ALL", "getALL", "assignColor", "avgPressure", "", "findByHex", "hex", "", "ThreadColor", "app_debug"})
public final class ThreadColors {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.newritage.app.util.ThreadColors.ThreadColor> LOW = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.newritage.app.util.ThreadColors.ThreadColor> MEDIUM = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.newritage.app.util.ThreadColors.ThreadColor> HIGH = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.newritage.app.util.ThreadColors.ThreadColor> ALL = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.newritage.app.util.ThreadColors INSTANCE = null;
    
    private ThreadColors() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.newritage.app.util.ThreadColors.ThreadColor> getLOW() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.newritage.app.util.ThreadColors.ThreadColor> getMEDIUM() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.newritage.app.util.ThreadColors.ThreadColor> getHIGH() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.newritage.app.util.ThreadColors.ThreadColor> getALL() {
        return null;
    }
    
    /**
     * Placeholder: returns a random color. Real algorithm TBD.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.newritage.app.util.ThreadColors.ThreadColor assignColor(float avgPressure) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.newritage.app.util.ThreadColors.ThreadColor findByHex(@org.jetbrains.annotations.NotNull()
    java.lang.String hex) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\'\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\t\u0010\u0015\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t\u00a8\u0006\u0016"}, d2 = {"Lcom/newritage/app/util/ThreadColors$ThreadColor;", "", "nameKr", "", "hex", "level", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getNameKr", "()Ljava/lang/String;", "getHex", "getLevel", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    public static final class ThreadColor {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String nameKr = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String hex = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String level = null;
        
        public ThreadColor(@org.jetbrains.annotations.NotNull()
        java.lang.String nameKr, @org.jetbrains.annotations.NotNull()
        java.lang.String hex, @org.jetbrains.annotations.NotNull()
        java.lang.String level) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getNameKr() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getHex() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getLevel() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.newritage.app.util.ThreadColors.ThreadColor copy(@org.jetbrains.annotations.NotNull()
        java.lang.String nameKr, @org.jetbrains.annotations.NotNull()
        java.lang.String hex, @org.jetbrains.annotations.NotNull()
        java.lang.String level) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}