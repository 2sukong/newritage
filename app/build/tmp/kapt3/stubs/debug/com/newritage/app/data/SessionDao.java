package com.newritage.app.data;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u0014\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0014\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\b0\u0016H\'J\u001e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u001c\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\n0\b2\u0006\u0010\u0014\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u000e\u0010\u001d\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u001e\u00a8\u0006\u001f\u00c0\u0006\u0003"}, d2 = {"Lcom/newritage/app/data/SessionDao;", "", "insert", "", "session", "Lcom/newritage/app/data/Session;", "(Lcom/newritage/app/data/Session;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSessionsByDate", "", "date", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "countSessionsByDate", "", "getThreadSessionByDate", "getSessionsInRange", "start", "end", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSessionsByMonth", "monthPrefix", "getAllSessionsFlow", "Lkotlinx/coroutines/flow/Flow;", "updateEmotion", "", "id", "emotion", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDatesWithSessionsByMonth", "getTotalActiveDays", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface SessionDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.newritage.app.data.Session session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sessions WHERE date = :date ORDER BY createdAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionsByDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.newritage.app.data.Session>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM sessions WHERE date = :date")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object countSessionsByDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sessions WHERE date = :date AND hasThread = 1 LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getThreadSessionByDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.newritage.app.data.Session> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sessions WHERE date BETWEEN :start AND :end ORDER BY date ASC, createdAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionsInRange(@org.jetbrains.annotations.NotNull()
    java.lang.String start, @org.jetbrains.annotations.NotNull()
    java.lang.String end, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.newritage.app.data.Session>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sessions WHERE date LIKE :monthPrefix ORDER BY date ASC, createdAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionsByMonth(@org.jetbrains.annotations.NotNull()
    java.lang.String monthPrefix, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.newritage.app.data.Session>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sessions ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.newritage.app.data.Session>> getAllSessionsFlow();
    
    @androidx.room.Query(value = "UPDATE sessions SET emotion = :emotion WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEmotion(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String emotion, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT DISTINCT date FROM sessions WHERE date LIKE :monthPrefix ORDER BY date ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDatesWithSessionsByMonth(@org.jetbrains.annotations.NotNull()
    java.lang.String monthPrefix, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(DISTINCT date) FROM sessions")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalActiveDays(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}